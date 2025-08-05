package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.BookRequestDTO;
import com.daniyal.bookstore.dto.BookResponseDTO;
import com.daniyal.bookstore.dto.BookUpdateDTO;
import com.daniyal.bookstore.entity.Book;
import com.daniyal.bookstore.exceptions.BookAlreadyExistsException;
import com.daniyal.bookstore.exceptions.BookNotExistsException;
import com.daniyal.bookstore.exceptions.DataPersistenceException;
import com.daniyal.bookstore.exceptions.ImmutableFieldException;
import com.daniyal.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Override
    public BookResponseDTO createBook(BookRequestDTO bookRequest) {
        Optional<Book> optionalBook=bookRepository.findByIsbn(bookRequest.getIsbn());
        if(optionalBook.isPresent())
        {
            throw new BookAlreadyExistsException("A Book with same ISBN already exists.");
        }

        optionalBook=bookRepository.findByAuthorAndTitle(bookRequest.getAuthor(), bookRequest.getTitle());
        if(optionalBook.isPresent())
        {
            throw new BookAlreadyExistsException("A Book with same title and author already exists.");
        }

        Book book=Book.builder()
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .isbn(bookRequest.getIsbn())
                .description(bookRequest.getDescription())
                .price(bookRequest.getPrice())
                .quantity(bookRequest.getQuantity())
                .build();
        Book savedBook=bookRepository.save(book);
        if(savedBook==null)
        {
            throw new DataPersistenceException("Failed to save book");
        }
        return BookResponseDTO.builder()
                .id(savedBook.getId())
                .title(savedBook.getTitle())
                .author(savedBook.getAuthor())
                .isbn(savedBook.getIsbn())
                .description(savedBook.getDescription())
                .price(savedBook.getPrice())
                .quantity(savedBook.getQuantity())
                .build();
    }

    @Override
    public Optional<BookResponseDTO> getBookById(Long id) {
        return Optional.empty();
    }

    @Override
    public Page<BookResponseDTO> getAllBooks(Pageable pageable) {
        return null;
    }

    @Override
    public BookResponseDTO fullUpdateBook(Long id, BookRequestDTO bookRequest) {
        // check if book exists or not
        Optional<Book> optionalBook=bookRepository.findById(id);
        if(!optionalBook.isPresent())
        {
            throw new BookNotExistsException("Book does not exists");
        }

        // check if user is trying to update ISBN throw exception
        Book existingBook=optionalBook.get();

        if(!existingBook.getIsbn().equals(bookRequest.getIsbn()))
        {
           throw new ImmutableFieldException("ISBN cannot be updated");
        }

        //  Same author multiple times without same title OK
        // Same title multiple times without same author OK
        // but same author with same title and vice versa duplicacy case

        optionalBook=bookRepository.findByAuthorAndTitle(bookRequest.getAuthor(),bookRequest.getTitle());
        if(optionalBook.isPresent()) {
            Book b;
            b = optionalBook.get();
            if (!id.equals(b.getId())) {
                throw new BookAlreadyExistsException("A Book with same title and author already exists.");
            }
        }

        Book book=Book.builder()
                .id(id)
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .isbn(bookRequest.getIsbn())
                .description(bookRequest.getDescription())
                .price(bookRequest.getPrice())
                .quantity(bookRequest.getQuantity())
                .build();


        Book savedBook=bookRepository.save(book);
        if(savedBook==null)
        {
            throw new DataPersistenceException("Failed to update book");
        }
        return BookResponseDTO.builder()
                .id(savedBook.getId())
                .title(savedBook.getTitle())
                .author(savedBook.getAuthor())
                .isbn(savedBook.getIsbn())
                .description(savedBook.getDescription())
                .price(savedBook.getPrice())
                .quantity(savedBook.getQuantity())
                .build();
    }

    @Override
    public BookResponseDTO partialUpdateBook(Long id, BookUpdateDTO bookRequest) {
        // check if book exists or not
        Optional<Book> optionalBook=bookRepository.findById(id);
        if(!optionalBook.isPresent())
        {
            throw new BookNotExistsException("Book does not exists");
        }

        Book existingBook=optionalBook.get();

        //  Same author multiple times without same title OK
        // Same title multiple times without same author OK
        // but same author with same title and vice versa duplicacy case

        optionalBook=bookRepository.findByAuthorAndTitle(bookRequest.getAuthor(),bookRequest.getTitle());
        if(optionalBook.isPresent()) {
            Book b;
            b = optionalBook.get();
            if (!id.equals(b.getId())) {
                throw new BookAlreadyExistsException("A Book with same title and author already exists.");
            }
            }

        if(bookRequest.getAuthor()!=null)
        {
            existingBook.setAuthor(bookRequest.getAuthor());
        }
        if(bookRequest.getTitle()!=null)
        {
            existingBook.setTitle(bookRequest.getTitle());
        }
        if(bookRequest.getDescription()!=null)
        {
            existingBook.setDescription(bookRequest.getDescription());
        }
        if(bookRequest.getPrice()!=null)
        {
            existingBook.setPrice(bookRequest.getPrice());
        }if(bookRequest.getQuantity()!=null)
        {
            existingBook.setQuantity(bookRequest.getQuantity());
        }

        Book savedBook=bookRepository.save(existingBook);
        if(savedBook==null)
        {
            throw new DataPersistenceException("Failed to update book");
        }
        return BookResponseDTO.builder()
                        .id(savedBook.getId())
                        .title(savedBook.getTitle())
                        .author(savedBook.getAuthor())
                        .isbn(savedBook.getIsbn())
                        .description(savedBook.getDescription())
                        .price(savedBook.getPrice())
                        .quantity(savedBook.getQuantity())
                        .build();
    }

    @Override
    public void deleteBook(Long id) {

        if(!bookRepository.findById(id).isPresent())
        {
            throw new BookNotExistsException("Book not exists.")
        }


    }
}
