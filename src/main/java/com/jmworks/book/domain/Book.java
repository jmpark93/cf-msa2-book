package com.jmworks.book.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userID;

    private String title;

    @ColumnDefault("'Not Yet'")
    private String status;

    @ColumnDefault("0")
    private Long currentPage;
    private Long totalPage;

    @Basic
    @Temporal(TemporalType.DATE)
    private java.util.Date purchaseDate;

    @Basic
    @Temporal(TemporalType.DATE)
    private java.util.Date publishDate;

    @Size(max = 120)
    private String imageURL;
}
