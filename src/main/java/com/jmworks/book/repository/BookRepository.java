package com.jmworks.book.repository;

import com.jmworks.book.domain.Book;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@RepositoryRestResource
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

    List<Book> findByUserID(@Param("userID") Long userID);

    boolean existsBookByTitle(@Param("title") String title);

    @Transactional
    void deleteAllByUserID(@Param("userID") Long userID);

}
