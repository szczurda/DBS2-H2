package com.projekt.dbs.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class BookRating {

    @Id
    @GeneratedValue
    @NotNull
    private int bookReviewId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User reviewer;

    @NotNull
    private int rating;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "bookIsbn", referencedColumnName = "isbn")
    private Book ratedBook;

    LocalDate date;

    public BookRating() {

    }

    public BookRating(User reviewer, int rating, Book ratedBook) {
        this.date = LocalDate.now();
        this.reviewer = reviewer;
        this.rating = rating;
        this.ratedBook = ratedBook;
    }


}
