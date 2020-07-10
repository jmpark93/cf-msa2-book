package com.jmworks.book.repository;

import com.jmworks.book.domain.Book;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

}
