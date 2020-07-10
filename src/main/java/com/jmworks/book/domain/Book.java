package com.jmworks.book.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userID;

    private String title;
}
