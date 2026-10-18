package com.noSQL.claseJava.service;

import com.noSQL.claseJava.dto.AuthorRequest;
import com.noSQL.claseJava.entity.Author;
import com.noSQL.claseJava.entity.Book;
import com.noSQL.claseJava.entity.Favorite;
import com.noSQL.claseJava.entity.Review;
import com.noSQL.claseJava.repository.AuthorRepository;
import com.noSQL.claseJava.repository.BookRepository;
import com.noSQL.claseJava.repository.FavoriteRepository;
import com.noSQL.claseJava.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository  bookRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;

    public Author createAuthor(AuthorRequest request) {
        Author author = Author.builder()
                .fullName(request.getFullName())
                .nationality(request.getNationality())
                .biography(request.getBiography())
                .books(request.getBooks())
                .build();
        return authorRepository.save(author);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorById(String id) {
        return authorRepository.findById(id);
    }

    public Optional<Author> getAuthorByName(String fullName) {
        return authorRepository.findByFullName(fullName);
    }

    public List<Author.BookReference> getAuthorBooks(String authorId) {
        Optional<Author> author = authorRepository.findById(authorId);
        if (author.isPresent() && author.get().getBooks() != null) {
            return author.get().getBooks();
        }
        return List.of();
    }

    public Author updateAuthor(String id, AuthorRequest request) {
        Optional<Author> existingAuthor = authorRepository.findById(id);
        if (existingAuthor.isPresent()) {

            Author author = existingAuthor.get();

            String oldFullName = author.getFullName();
            String newFullName =  request.getFullName();

            author.setFullName(request.getFullName());
            author.setNationality(request.getNationality());
            author.setBiography(request.getBiography());
            Author auth = authorRepository.save(author);

            if (!oldFullName.equals(newFullName)) {

                if(author.getBooks() != null){

                    for (Author.BookReference book : author.getBooks()) {
                        // Actualizar fullName en Book
                        updateFullNameAuthorInBook(book.getBookId(), newFullName);

                        // Actualizar fullName en Favorites
                        updateFullNameAuthorInFavorites(author.getId(), newFullName);

                        // Actualizar en Reviews
                        updateAuthorInfoInReviews(author.getId(), newFullName, request.getNationality());
                    }
                }

            }

            return auth;

        }
        return null;
    }


    // Metodos auxiliares de actualizacion

    public void updateFullNameAuthorInBook(String bookId, String newFullName) {
        Optional<Book> book = bookRepository.findById(bookId);

        if (book.isPresent()) {

            Book book1 = book.get();
            if(book1.getAuthor() != null){
                book1.getAuthor().setFullName(newFullName);
            }
            bookRepository.save(book1);
        }
    }


    public void updateFullNameAuthorInFavorites(String authorId, String newName){
        favoriteRepository.findAll().forEach(favorite -> {
            if (favorite.getFavorite() != null && favorite.getFavorite().getAuthor() != null && favorite.getFavorite().getAuthor().getId().equals(authorId)) {
                favorite.getFavorite().getAuthor().setFullName(newName);
                favoriteRepository.save(favorite);
            }
        });
    }

    public void updateAuthorInfoInReviews(String authorId, String newName, String newNationality) {
        reviewRepository.findAll().forEach(review -> {
            if (review.getBook() != null && review.getBook().getAuthor() != null && review.getBook().getAuthor().getAuthorId().equals(authorId)) {
                review.getBook().getAuthor().setName(newName);
                review.getBook().getAuthor().setNationality(newNationality);
                reviewRepository.save(review);
            }
        });
    }

    public boolean deleteAuthor(String id) {

        Optional<Author> author = authorRepository.findById(id);

        if (author.isPresent()) {

            Author author1 = author.get();

            // Remove from Book
            if(author1.getBooks() != null){
                for (Author.BookReference book : author1.getBooks()) {
                    deleteAuthorFromBook(book.getBookId());
                }
            }

            // Remove from Review
            reviewRepository.findAll().forEach(review -> {
                if (review.getBook() != null && review.getBook().getAuthor() != null && review.getBook().getAuthor().getAuthorId().equals(id)) {
                    deleteAuthorFromReview(review.getId());
                }
            });

            //Remove from Favorites
            deleteAuthorFromFavorites(author1.getId());

            authorRepository.delete(author1);
            return true;
        }
        return false;
    }

    //Metodos auxiliares para eliminacion
    public void deleteAuthorFromBook(String idBook) {
        Optional<Book> book = bookRepository.findById(idBook);

        if (book.isPresent()) {
            Book book1 = book.get();
            if (book1.getAuthor() != null) {
                book1.setAuthor(null);
                bookRepository.save(book1);
            }
        }
    }

    public void deleteAuthorFromReview(String idReview){
        Optional<Review> review = reviewRepository.findById(idReview);

        if (review.isPresent()) {
            Review rev1 = review.get();
            if (rev1.getBook().getAuthor() != null) {
                rev1.getBook().setAuthor(null);
                reviewRepository.save(rev1);
            }
        }
    }

    public void deleteAuthorFromFavorites(String idBook){
        List<Favorite> favorite = favoriteRepository.findAll();

        favorite.forEach(favorite1 -> {
            favorite1.getFavorite().setAuthor(null);
            favoriteRepository.save(favorite1);
        });
    }

}
