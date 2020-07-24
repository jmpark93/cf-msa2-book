package com.jmworks.book.controller;

import com.jmworks.book.domain.Book;
import com.jmworks.book.exception.BookNotFoundException;
import com.jmworks.book.payload.MessageResponse;
import com.jmworks.book.repository.BookRepository;
import com.jmworks.book.service.S3Service;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@CrossOrigin
@RequestMapping("/api")
@RestController
public class BookController {

    @Value("${s3.bucket}")
    String bucket;

    @Value("${s3.public}")
    String s3URL;

    private final S3Service s3Service;
    private final BookRepository bookRepository;


    public BookController(BookRepository bookRepository, S3Service s3Service) {
        this.bookRepository = bookRepository;
        this.s3Service = s3Service;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(value = "/books/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> AddBook(
            @RequestParam(value = "bookItem", required = true) @Valid String jsonBookItem,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) throws IOException {
        String accessURL = "";

        System.out.println( jsonBookItem );
        ObjectMapper objectMapper = new ObjectMapper();
        Book bookItem = objectMapper.readValue(jsonBookItem, Book.class);

        if (bookRepository.existsBookByTitle(bookItem.getTitle())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Book is already exist ..."));
        }

        Book savedBook = bookRepository.save(bookItem);

        // 사용자 프로파일 이미지 --> Upload (minio : S3)
        if (imageFile != null) {

            System.out.println("User ID : " + savedBook.getUserID()) ;

            accessURL = s3Service.uploadFile(imageFile, savedBook.getUserID() + "-book-" + savedBook.getId() );
            savedBook.setImageURL(s3URL + "/" + bucket + "/" + accessURL);

            String sourceFileName = imageFile.getOriginalFilename();
            System.out.println("Original File Name : " + sourceFileName);
            System.out.println("Access URL : " + accessURL);

            bookRepository.save(savedBook);
        }
        return ResponseEntity.ok(new MessageResponse("Book registered successfully!"));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping(value = "/books/image/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> AddBook(
            @PathVariable("id") Long bookId,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) throws IOException {
        String accessURL = "";

        Book bookItem = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        // 사용자 프로파일 이미지 --> Upload (minio : S3)
        if (imageFile != null) {

            accessURL = s3Service.uploadFile(imageFile, bookItem.getUserID() + "-book-" + bookItem.getId() );
            bookItem.setImageURL(s3URL + "/" + bucket + "/" + accessURL);

            String sourceFileName = imageFile.getOriginalFilename();
            System.out.println("Original File Name : " + sourceFileName);
            System.out.println("Access URL : " + accessURL);

            bookRepository.save(bookItem);
        }
        return ResponseEntity.ok(new MessageResponse("Book(" + bookId + ") image updated successfully!"));
    }
}