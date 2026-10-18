package com.noSQL.claseJava.service;

import com.noSQL.claseJava.dto.BookRequest;
import com.noSQL.claseJava.entity.Book;
import com.noSQL.claseJava.entity.Author;
import com.noSQL.claseJava.entity.Editorial;
import com.noSQL.claseJava.entity.Category;
import com.noSQL.claseJava.repository.BookRepository;
import com.noSQL.claseJava.repository.AuthorRepository;
import com.noSQL.claseJava.repository.EditorialRepository;
import com.noSQL.claseJava.repository.CategoryRepository;
import com.noSQL.claseJava.repository.LoanRepository;
import com.noSQL.claseJava.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final EditorialRepository editorialRepository;
    private final CategoryRepository categoryRepository;
    private final LoanRepository loanRepository;
    private final ReviewRepository reviewRepository;

    @CacheEvict(value = "books", allEntries = true)
    public Book createBook(BookRequest request) {
        // Find existing Author by name or create new one
        Book.Author authorData = request.getAuthor();
        Author author = authorRepository.findByFullName(authorData.getFullName())
                .orElseGet(() -> authorRepository.save(Author.builder()
                        .fullName(authorData.getFullName())
                        .nationality(authorData.getNationality())
                        .biography(authorData.getBiography())
                        .build()));
        authorData.setId(author.getId());
        authorData.setFullName(author.getFullName());
        authorData.setNationality(author.getNationality());
        authorData.setBiography(author.getBiography());

        // Find existing Editorial by name or create new one
        Book.Editorial editorialData = request.getEditorial();
        Editorial editorial = editorialRepository.findByName(editorialData.getName())
                .orElseGet(() -> editorialRepository.save(Editorial.builder()
                        .name(editorialData.getName())
                        .country(editorialData.getCountry())
                        .website(editorialData.getWebsite())
                        .build()));
        editorialData.setId(editorial.getId());
        editorialData.setName(editorial.getName());
        editorialData.setCountry(editorial.getCountry());
        editorialData.setWebsite(editorial.getWebsite());

        // Find existing Category by name or create new one
        if (request.getCategories() != null) {
            for (Book.Category catData : request.getCategories()) {
                Category category = categoryRepository.findByName(catData.getName())
                        .orElseGet(() -> categoryRepository.save(Category.builder()
                                .name(catData.getName())
                                .description(catData.getDescription())
                                .build()));
                catData.setId(category.getId());
                catData.setName(category.getName());
                catData.setDescription(category.getDescription());
            }
        }

        // Create Book
        Book book = Book.builder()
                .uniqueIdentifier(request.getUniqueIdentifier())
                .title(request.getTitle())
                .synopsis(request.getSynopsis())
                .publicationYear(request.getPublicationYear())
                .available(request.getAvailable())
                .author(authorData)
                .editorial(editorialData)
                .categories(request.getCategories())
                .averageCalifications(request.getAverageCalifications())
                .isLent(request.getIsLent())
                .reviewsTotal(request.getReviewsTotal())
                .build();
        book = bookRepository.save(book);

        // Add book reference to Author
        addBookToAuthor(book.getAuthor().getId(), book.getId(), book.getTitle());

        // Add book reference to Editorial
        addBookToEditorial(book.getEditorial().getId(), book.getId(), book.getTitle());

        // Add book reference to Categories
        if (request.getCategories() != null) {
            for (Book.Category cat : request.getCategories()) {
                addBookToCategory(cat.getId(), book.getId(), book.getTitle());
            }
        }

        return book;
    }

    private void addBookToAuthor(String authorId, String bookId, String bookTitle) {
        Optional<Author> author = authorRepository.findById(authorId);
        if (author.isPresent()) {
            Author a = author.get();
            if (a.getBooks() == null) {
                a.setBooks(new java.util.ArrayList<>());
            }
            a.getBooks().add(Author.BookReference.builder()
                    .bookId(bookId)
                    .title(bookTitle)
                    .build());
            authorRepository.save(a);
        }
    }

    private void addBookToEditorial(String editorialId, String bookId, String bookTitle) {
        Optional<Editorial> editorial = editorialRepository.findById(editorialId);
        if (editorial.isPresent()) {
            Editorial e = editorial.get();
            if (e.getBooks() == null) {
                e.setBooks(new java.util.ArrayList<>());
            }
            e.getBooks().add(Editorial.BookReference.builder()
                    .bookId(bookId)
                    .title(bookTitle)
                    .build());
            editorialRepository.save(e);
        }
    }

    private void addBookToCategory(String categoryId, String bookId, String bookTitle) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            Category c = category.get();
            if (c.getBooks() == null) {
                c.setBooks(new java.util.ArrayList<>());
            }
            c.getBooks().add(Category.BookReference.builder()
                    .bookId(bookId)
                    .title(bookTitle)
                    .build());
            categoryRepository.save(c);
        }
    }

    @Cacheable(value = "books")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    public List<Book> getBooksByAuthor(String authorFullName) {
        return bookRepository.findByAuthorFullName(authorFullName);
    }

    public List<Book> getBooksByEditorial(String editorialName) {
        return bookRepository.findByEditorialName(editorialName);
    }

    public List<Book> getBooksByCategory(String categoryName) {
        return bookRepository.findByCategoriesName(categoryName);
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailable(true);
    }

    @CacheEvict(value = "books", allEntries = true)
    public Book updateBook(String id, BookRequest request) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            String oldTitle = book.getTitle();
            String newTitle = request.getTitle();

            book.setUniqueIdentifier(request.getUniqueIdentifier());
            book.setTitle(newTitle);
            book.setSynopsis(request.getSynopsis());
            book.setPublicationYear(request.getPublicationYear());
            book.setAvailable(request.getAvailable());
            book.setAuthor(request.getAuthor());
            book.setEditorial(request.getEditorial());
            book.setCategories(request.getCategories());
            book = bookRepository.save(book);

            if (!oldTitle.equals(newTitle)) {
                // Actualizar en Author
                updateBookTitleInAuthor(book.getAuthor().getId(), id, newTitle);

                // Actualizar en Editorial
                updateBookTitleInEditorial(book.getEditorial().getId(), id, newTitle);

                // Actualizar en todas las Categorías
                if (book.getCategories() != null) {
                    for (Book.Category cat : book.getCategories()) {
                        updateBookTitleInCategory(cat.getId(), id, newTitle);
                    }
                }

                // Actualizar en Loans
                updateBookTitleInLoans(id, newTitle);

                // Actualizar en Reviews
                updateBookInfoInReviews(id, newTitle);
            }

            return book;
        }
        return null;
    }

    private void updateBookTitleInLoans(String bookId, String newTitle) {
        loanRepository.findByBookLoanId(bookId).forEach(loan -> {
            if (loan.getBookLoan() != null) {
                loan.getBookLoan().setTitle(newTitle);
                loanRepository.save(loan);
            }
        });
    }

    private void updateBookInfoInReviews(String bookId, String newTitle) {
        reviewRepository.findByBookBookId(bookId).forEach(review -> {
            if (review.getBook() != null) {
                review.getBook().setTitle(newTitle);
                reviewRepository.save(review);
            }
        });
    }



    private void updateBookTitleInAuthor(String authorId, String bookId, String newTitle) {
        Optional<Author> author = authorRepository.findById(authorId);
        if (author.isPresent()) {
            Author a = author.get();
            if (a.getBooks() != null) {
                a.getBooks().stream()
                    .filter(br -> br.getBookId().equals(bookId))
                    .forEach(br -> br.setTitle(newTitle));
                authorRepository.save(a);
            }
        }
    }

    private void updateBookTitleInEditorial(String editorialId, String bookId, String newTitle) {
        Optional<Editorial> editorial = editorialRepository.findById(editorialId);
        if (editorial.isPresent()) {
            Editorial e = editorial.get();
            if (e.getBooks() != null) {
                e.getBooks().stream()
                    .filter(br -> br.getBookId().equals(bookId))
                    .forEach(br -> br.setTitle(newTitle));
                editorialRepository.save(e);
            }
        }
    }

    private void updateBookTitleInCategory(String categoryId, String bookId, String newTitle) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            Category c = category.get();
            if (c.getBooks() != null) {
                c.getBooks().stream()
                    .filter(br -> br.getBookId().equals(bookId))
                    .forEach(br -> br.setTitle(newTitle));
                categoryRepository.save(c);
            }
        }
    }


    @CacheEvict(value = "books", allEntries = true)
    public boolean deleteBook(String id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book b = book.get();
            // Remove from Author
            removeBookFromAuthor(b.getAuthor().getId(), id);
            // Remove from Editorial
            removeBookFromEditorial(b.getEditorial().getId(), id);
            // Remove from Categories
            if (b.getCategories() != null) {
                for (Book.Category cat : b.getCategories()) {
                    removeBookFromCategory(cat.getId(), id);
                }
            }
            // Delete loans associated with this book
            loanRepository.deleteByBookLoanId(id);
            // Delete reviews associated with this book
            reviewRepository.deleteByBookBookId(id);

            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void removeBookFromAuthor(String authorId, String bookId) {
        Optional<Author> author = authorRepository.findById(authorId);
        if (author.isPresent()) {
            Author a = author.get();
            if (a.getBooks() != null) {
                a.getBooks().removeIf(br -> br.getBookId().equals(bookId));
                authorRepository.save(a);
            }
        }
    }

    private void removeBookFromEditorial(String editorialId, String bookId) {
        Optional<Editorial> editorial = editorialRepository.findById(editorialId);
        if (editorial.isPresent()) {
            Editorial e = editorial.get();
            if (e.getBooks() != null) {
                e.getBooks().removeIf(br -> br.getBookId().equals(bookId));
                editorialRepository.save(e);
            }
        }
    }

    private void removeBookFromCategory(String categoryId, String bookId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            Category c = category.get();
            if (c.getBooks() != null) {
                c.getBooks().removeIf(br -> br.getBookId().equals(bookId));
                categoryRepository.save(c);
            }
        }
    }
}
