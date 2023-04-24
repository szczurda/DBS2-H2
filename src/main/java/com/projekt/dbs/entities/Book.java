package com.projekt.dbs.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Book {


    @Id
    @NotNull
    @Column(name = "isbn")
    private Integer isbn;

    @NotNull
    private String name;

    @NotNull
    private int yearOfPublishing;

    @NotNull
    private String publisher;

    @NotNull
    private int pages;

    private String bookDescription;

    @Column(nullable = true)
    private Integer availableCopies;

    @ManyToOne
    @JoinColumn(name = "genreId")
    @NotNull
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "authorId")
    @NotNull
    private Author bookAuthor;

    public Book() {
    }

    public Book(String name, int yearOfPublishing, Author bookAuthor, int isbn, String publisher, int pages, Integer availableCopies, Genre genre) {
        this.name = name;
        this.yearOfPublishing = yearOfPublishing;
        this.bookAuthor = bookAuthor;
        this.isbn = isbn;
        this.publisher = publisher;
        this.pages = pages;
        this.availableCopies = availableCopies;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfPublishing() {
        return yearOfPublishing;
    }

    public void setYearOfPublishing(int yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
    }

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public Genre getGenre(){
        if(genre != null) {
            return genre;
        }
        else return null;
    }

    public void setGenre(Genre g) {
        this.genre = g;
    }

    public Author getBookAuthor(){
        if(bookAuthor != null) {
            return this.bookAuthor;
        } else return null;
    }

    public void setBookAuthor(Author author) {
        if(author != null){
            this.bookAuthor = author;
        }
        else this.setBookAuthor(null);
    }

    public String getAuthorName(){
        return bookAuthor.toString();
    }

    public void setAuthorName(String name){
        bookAuthor.setName(name);
    }
}
