package com.daniyal.bookstore.service;

import com.daniyal.bookstore.dto.BookRequestDTO;
import com.daniyal.bookstore.dto.BookResponseDTO;
import com.daniyal.bookstore.dto.BookUpdateDTO;
import com.daniyal.bookstore.entity.Author;
import com.daniyal.bookstore.entity.Book;
import com.daniyal.bookstore.exceptions.*;
import com.daniyal.bookstore.repository.AuthorRepository;
import com.daniyal.bookstore.repository.BookRepository;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    //@Transactional
    @Override
    public BookResponseDTO createBook(BookRequestDTO bookRequest) {
        Optional<Book> optionalBook=bookRepository.findByIsbn(bookRequest.getIsbn());
        if(optionalBook.isPresent())
        {
            throw new BookAlreadyExistsException("A Book with same ISBN already exists.");
        }

        // perform duplicacy check
        // only when Book with same title + same Set of Authors already exists
        //List<Book> existingBooks=bookRepository.findByTitleWithAuthors(bookRequest.getTitle());
        List<Book> existingBooks=bookRepository.findByTitle(bookRequest.getTitle());

        Set<Long> authorIdsRequest=bookRequest.getAuthorIds();
        for(Book existingBook:existingBooks)
        {
            //Hibernate.initialize(existingBook.getAuthors());
            //Set<Author> authorSafeCopy=new HashSet<>(existingBook.getAuthors());
            Set<Author> authorSafeCopy=existingBook.getAuthors();
            System.out.println("Authors loaded: " + existingBook.getAuthors().size());
            Set<Long> authorIdsDB=authorSafeCopy
                    .stream()
                    .map(Author::getId)
                    .collect(Collectors.toSet());

            if(authorIdsDB.equals(authorIdsRequest))
            {
                throw new BookAlreadyExistsException("Book with the same title and authors already exists.");
            }
        }

        Set<Author> authorSet=new HashSet<>(authorRepository.findAllById(bookRequest.getAuthorIds()));
        if(authorSet.size()!=bookRequest.getAuthorIds().size()) {
            throw new AuthorNotFoundException("One or more authors not found");
        }

        Book book=Book.builder()
                .title(bookRequest.getTitle())
                .authors(authorSet)
                .genre(bookRequest.getGenre())
                .isbn(bookRequest.getIsbn())
                .description(bookRequest.getDescription())
                .price(bookRequest.getPrice())
                .quantity(bookRequest.getQuantity())
                .imageUrl(bookRequest.getImageUrl())
                .build();
        Book savedBook=bookRepository.save(book);

        Set<String> authors=authorSet.stream()
                .map(Author::getName)
                .collect(Collectors.toSet());
        return BookResponseDTO.builder()
                .id(savedBook.getId())
                .title(savedBook.getTitle())
                .authors(authors)
                .genre(savedBook.getGenre())
                .isbn(savedBook.getIsbn())
                .description(savedBook.getDescription())
                .price(savedBook.getPrice())
                .quantity(savedBook.getQuantity())
                .imageUrl(savedBook.getImageUrl())
                .build();
    }

    @Override
    public BookResponseDTO getBookById(Long id) {

        Optional<Book> optionalBook=bookRepository.findById(id);
        if(optionalBook.isEmpty())
        {
            throw new BookNotFoundException("Book not exits");
        }
// also can do with this style
//        Book dbBook = bookRepository.findById(id)
//                .orElseThrow(() -> new BookNotExistsException("Book not exists"));

        Book dbBook=optionalBook.get();

        Set<Author> authorSet=dbBook.getAuthors();
        Set<String> authors=authorSet.stream()
                .map(Author::getName)
                .collect(Collectors.toSet());

        return BookResponseDTO.builder()
                .id(dbBook.getId())
                .title(dbBook.getTitle())
                .authors(authors)
                .genre(dbBook.getGenre())
                .isbn(dbBook.getIsbn())
                .description(dbBook.getDescription())
                .price(dbBook.getPrice())
                .quantity(dbBook.getQuantity())
                .imageUrl(dbBook.getImageUrl())
                .build();
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {
        List<Book> books=bookRepository.findAll();
        return books.stream()
                .map(book -> BookResponseDTO.builder()
                        .id(book.getId())
                        .authors(
                                book.getAuthors().stream()
                                        .map(Author::getName)
                                        .collect(Collectors.toSet())
                        )
                        .title(book.getTitle())
                        .description(book.getDescription())
                        .price(book.getPrice())
                        .quantity(book.getQuantity())
                        .isbn(book.getIsbn())
                        .genre(book.getGenre())
                        .imageUrl(book.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookResponseDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(book -> BookResponseDTO.builder()
                        .id(book.getId())
                        .authors(
                                book.getAuthors().stream()
                                        .map(Author::getName)
                                        .collect((Collectors.toSet()))
                        )
                        .title(book.getTitle())
                        .description(book.getDescription())
                        .price(book.getPrice())
                        .quantity(book.getQuantity())
                        .isbn(book.getIsbn())
                        .build());
    }

    @Override
    public BookResponseDTO fullUpdateBook(Long id, BookRequestDTO bookRequest) {
        // check if book exists or not
        Optional<Book> optionalBook=bookRepository.findById(id);
        if(optionalBook.isEmpty())
        {
            throw new BookNotFoundException("Book does not exists");
        }

        // check if user is trying to update ISBN throw exception
        Book existingBook=optionalBook.get();

        if(!existingBook.getIsbn().equals(bookRequest.getIsbn()))
        {
           throw new ImmutableFieldException("ISBN cannot be updated");
        }


        // perform duplicacy check
        // only when Book with same title + same Set of Authors already exists against another record
        // not with this one, skip if it's record comes
        List<Book> existingBooks=bookRepository.findByTitle(bookRequest.getTitle());

        Long updateBookId=existingBook.getId();

        Set<Long> authorIdsRequest=bookRequest.getAuthorIds();
        for(Book _existingBook:existingBooks)
        {
            if(_existingBook.getId().equals(updateBookId)) continue;
            Set<Author> authorSafeCopy=new HashSet<>(_existingBook.getAuthors());
            Set<Long> authorIdsDB=authorSafeCopy
                    .stream()
                    .map(Author::getId)
                    .collect(Collectors.toSet());
            if(authorIdsDB.equals(authorIdsRequest))
            {
                throw new BookAlreadyExistsException("Book with the same title and authors already exists.");
            }
        }


        //  Same author multiple times without same title OK
        // Same title multiple times without same author OK
        // but same author with same title and vice versa duplicacy case

//        optionalBook=bookRepository.findByAuthorAndTitle(bookRequest.getAuthor(),bookRequest.getTitle());
//        if(optionalBook.isPresent()) {
//            Book b;
//            b = optionalBook.get();
//            if (!id.equals(b.getId())) {
//                throw new BookAlreadyExistsException("A Book with same title and author already exists.");
//            }
//        }


        Set<Author> authorSet=new HashSet<>(authorRepository.findAllById(authorIdsRequest));

        existingBook.setTitle(bookRequest.getTitle());
        existingBook.setAuthors(authorSet);
        existingBook.setGenre(bookRequest.getGenre());
        existingBook.setDescription(bookRequest.getDescription());
        existingBook.setPrice(bookRequest.getPrice());
        existingBook.setQuantity(bookRequest.getQuantity());
        existingBook.setImageUrl(bookRequest.getImageUrl());

        Book savedBook=bookRepository.save(existingBook);

        Set<String> authors=authorSet.stream()
                .map(Author::getName)
                .collect(Collectors.toSet());
        return BookResponseDTO.builder()
                .id(savedBook.getId())
                .title(savedBook.getTitle())
                .authors(authors)
                .genre(savedBook.getGenre())
                .isbn(savedBook.getIsbn())
                .description(savedBook.getDescription())
                .price(savedBook.getPrice())
                .quantity(savedBook.getQuantity())
                .imageUrl(savedBook.getImageUrl())
                .build();
    }

    @Override
    public BookResponseDTO partialUpdateBook(Long id, BookUpdateDTO bookRequest) {
        // check if book exists or not
        Optional<Book> optionalBook=bookRepository.findById(id);
        if(optionalBook.isEmpty())
        {
            throw new BookNotFoundException("Book does not exists");
        }

        Book existingBook=optionalBook.get();

        Set<Author> authorSet= Collections.emptySet();

        if(bookRequest.getAuthorIds()!=null && !bookRequest.getAuthorIds().isEmpty())
        {
            authorSet=new HashSet<>(authorRepository.findAllById(bookRequest.getAuthorIds()));
            if(authorSet.size()!=bookRequest.getAuthorIds().size())
            {
                throw new AuthorNotFoundException("One or more author not found");
            }
            existingBook.setAuthors(authorSet);
        }
        if(bookRequest.getTitle()!=null)
        {
            existingBook.setTitle(bookRequest.getTitle());
        }
        if(bookRequest.getGenre()!=null)
        {
            existingBook.setGenre(bookRequest.getGenre());
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
        if(bookRequest.getImageUrl()!=null)
        {
            existingBook.setImageUrl(bookRequest.getImageUrl());
        }
        Book savedBook=bookRepository.save(existingBook);

        Set<String> authors=authorSet.stream()
                .map(Author::getName)
                .collect(Collectors.toSet());

        return BookResponseDTO.builder()
                .id(savedBook.getId())
                .title(savedBook.getTitle())
                .authors(authors)
                .genre(savedBook.getGenre())
                .isbn(savedBook.getIsbn())
                .description(savedBook.getDescription())
                .price(savedBook.getPrice())
                .quantity(savedBook.getQuantity())
                .imageUrl(savedBook.getImageUrl())
                .build();
    }

    @Override
    public void deleteBook(Long id) {

        if(bookRepository.findById(id).isEmpty())
        {
            throw new BookNotFoundException("Book not exists.");
        }
        bookRepository.deleteById(id);
    }
}
