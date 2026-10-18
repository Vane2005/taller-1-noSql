package com.noSQL.claseJava.service;

import com.noSQL.claseJava.dto.CategoryRequest;
import com.noSQL.claseJava.entity.Book;
import com.noSQL.claseJava.entity.Category;
import com.noSQL.claseJava.repository.BookRepository;
import com.noSQL.claseJava.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public Category createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .books(request.getBooks())
                .build();
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    public List<Category.BookReference> getCategoryBooks(String categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent() && category.get().getBooks() != null) {
            return category.get().getBooks();
        }
        return List.of();
    }

    public Category updateCategory(String id, CategoryRequest request) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();

            String oldName = category.getName();
            String newName = request.getName();

            String oldDescription = category.getDescription();
            String newDescription = request.getDescription();

            category.setName(request.getName());
            category.setDescription(request.getDescription());
            category.setBooks(request.getBooks());
            Category cat= categoryRepository.save(category);

            if (category.getBooks() != null) {
                for(Category.BookReference book : category.getBooks()) {
                    if (!oldName.equals(newName)) {
                        updateNameCategoryInBook(book.getBookId(), category.getId(), newName);
                    }
                    if(!oldDescription.equals(newDescription)) {
                        updateDescriptionCategoryInBook(book.getBookId(), category.getId(), newDescription);
                    }
                }
            }

            return cat;
        }
        return null;
    }

    // Funcion auxiliar para actualizar Name en Book
    public void updateNameCategoryInBook(String idBook, String idCategory, String newNameCategory) {
        Optional<Book> book = bookRepository.findById(idBook);

        if (book.isPresent()) {
            Book bookToUpdate = book.get();
            if (bookToUpdate.getCategories() != null){

                bookToUpdate.getCategories().stream()
                        .filter(ct -> ct.getId().equals(idCategory))
                        .forEach(ct -> ct.setName(newNameCategory));
                bookRepository.save(bookToUpdate);
            }
        }
    }

    // Funcion auxiliar para actualizar Description en Book
    public void updateDescriptionCategoryInBook(String idBook, String idCategory, String newDescriptionCategory) {
        Optional<Book> book = bookRepository.findById(idBook);

        if (book.isPresent()) {
            Book bookToUpdate = book.get();
            if (bookToUpdate.getCategories() != null){
                bookToUpdate.getCategories().stream()
                        .filter(ct -> ct.getId().equals(idCategory))
                        .forEach(ct -> ct.setDescription(newDescriptionCategory));
                bookRepository.save(bookToUpdate);
            }
        }
    }

    public boolean deleteCategory(String id) {
        if (categoryRepository.existsById(id)) {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent()) {
                Category categoryToDelete = category.get();
                if(categoryToDelete.getBooks() != null) {
                    for(Category.BookReference book : categoryToDelete.getBooks()) {
                        deleteCategoryFromBook(book.getBookId(), categoryToDelete.getId());
                    }
                }
            }

            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Funcion auxiliar para eliminar categoria de Books
    public void deleteCategoryFromBook(String idBook, String idCategory) {

        Optional<Book> book =  bookRepository.findById(idBook);

        if(book.isPresent()){
            Book bookToDelete = book.get();
            if (bookToDelete.getCategories() != null){
                bookToDelete.getCategories().removeIf(ct -> ct.getId().equals(idCategory));
                bookRepository.save(bookToDelete);
            }
        }
    }
}
