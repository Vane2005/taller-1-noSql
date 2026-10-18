package com.noSQL.claseJava.service;

import com.noSQL.claseJava.dto.EditorialRequest;
import com.noSQL.claseJava.entity.Book;
import com.noSQL.claseJava.entity.Editorial;
import com.noSQL.claseJava.repository.BookRepository;
import com.noSQL.claseJava.repository.EditorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EditorialService {

    private final EditorialRepository editorialRepository;
    private final BookRepository bookRepository;

    public Editorial createEditorial(EditorialRequest request) {
        Editorial editorial = Editorial.builder()
                .name(request.getName())
                .country(request.getCountry())
                .website(request.getWebsite())
                .books(request.getBooks())
                .build();
        return editorialRepository.save(editorial);
    }

    public List<Editorial> getAllEditorials() {
        return editorialRepository.findAll();
    }

    public Optional<Editorial> getEditorialById(String id) {
        return editorialRepository.findById(id);
    }

    public Optional<Editorial> getEditorialByName(String name) {
        return editorialRepository.findByName(name);
    }

    public List<Editorial.BookReference> getEditorialBooks(String editorialId) {
        Optional<Editorial> editorial = editorialRepository.findById(editorialId);
        if (editorial.isPresent() && editorial.get().getBooks() != null) {
            return editorial.get().getBooks();
        }
        return List.of();
    }

    public Editorial updateEditorial(String id, EditorialRequest request) {
        Optional<Editorial> existingEditorial = editorialRepository.findById(id);
        if (existingEditorial.isPresent()) {

            Editorial editorial = existingEditorial.get();

            String newName = request.getName();
            String oldName = editorial.getName();
            String newCountry = request.getCountry();
            String oldCountry =  editorial.getCountry();
            String newWebsite = request.getWebsite();
            String oldWebsite = editorial.getWebsite();

            editorial.setName(request.getName());
            editorial.setCountry(request.getCountry());
            editorial.setWebsite(request.getWebsite());

            for (Editorial.BookReference book : editorial.getBooks()) {
                if(!newName.equals(oldName)){
                    updateNameEditorialInBook(book.getBookId(), newName);
                }
                if(!oldCountry.equals(newCountry)){
                    updateCountryEditorialInBook(book.getBookId(), newName);
                }
                if(!oldWebsite.equals(newWebsite)){
                    updateWebsiteEditorialInBook(book.getBookId(), newWebsite);
                }
            }

            return editorialRepository.save(editorial);
        }
        return null;
    }

    // Funcion auxiliar para actualizar nameEditorial en Book
    public void updateNameEditorialInBook(String idBook, String newName){
        Optional<Book> book = bookRepository.findById(idBook);
        if(book.isPresent()){
            Book book1 = book.get();
            if(book1.getEditorial() != null){
                book1.getEditorial().setName(newName);
                bookRepository.save(book1);
            }
        }
    }

    // Funcion auxiliar para actualizar CountryEditorial en Book
    public void updateCountryEditorialInBook(String idBook, String newCountry){
        Optional<Book> book = bookRepository.findById(idBook);
        if(book.isPresent()){
            Book book1 = book.get();
            if(book1.getEditorial() != null){
                book1.getEditorial().setCountry(newCountry);
                bookRepository.save(book1);
            }
        }
    }

    // Funcion auxiliar para actualizar CountryEditorial en Book
    public void updateWebsiteEditorialInBook(String idBook, String newWebsite){
        Optional<Book> book = bookRepository.findById(idBook);
        if(book.isPresent()){
            Book book1 = book.get();
            if(book1.getEditorial() != null){
                book1.getEditorial().setWebsite(newWebsite);
                bookRepository.save(book1);
            }
        }
    }

    public boolean deleteEditorial(String id) {
        if (editorialRepository.existsById(id)) {

            Optional<Editorial> editorial = editorialRepository.findById(id);

            if (editorial.isPresent()) {
                Editorial  editorial1 = editorial.get();
                if(editorial1.getBooks() != null) {
                    for(Editorial.BookReference book : editorial1.getBooks()){
                        deleteEditorialFromBook(book.getBookId());
                    }
                }
            }

            editorialRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //funcion para eliminar editorial de Book
    public void deleteEditorialFromBook(String idBook){
        Optional<Book> book = bookRepository.findById(idBook);

        if(book.isPresent()){

            Book book1 = book.get();
            if(book1.getEditorial() != null){
                book1.setAuthor(null);
                bookRepository.save(book1);
            }

        }
    }
}
