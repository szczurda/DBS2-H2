package com.projekt.dbs.tools;

import com.projekt.dbs.entities.Book;
import com.vaadin.flow.component.grid.dataview.GridListDataView;

public class BookFilter {

    GridListDataView<Book> dataView;
    private String name;
    private String author;
    private String publisher;
    private String yearOfPublishing;
    private String pages;
    private String bookDescription;

    private String availableCopies;

    public BookFilter(GridListDataView<Book> dataView) {
        this.dataView = dataView;
        dataView.addFilter(this::test);
    }

    public void setName(String name) {
        this.name = name;
        dataView.refreshAll();
    }

    public void setAuthor(String author) {
        this.author = author;
        dataView.refreshAll();
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
        dataView.refreshAll();
    }

    public void setYearOfPublishing(String yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
        dataView.refreshAll();
    }

    public void setPages(String pages) {
        this.pages = pages;
        dataView.refreshAll();
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
        dataView.refreshAll();
    }

    public void setAvailableCopies(String availableCopies){
        this.availableCopies = availableCopies;
        dataView.refreshAll();
    }

    public boolean test(Book book) {
        boolean nameFilter = true;
        boolean authorFilter = true;
        boolean publisherFilter = true;
        boolean yearFilter = true;
        boolean pagesFilter = true;
        boolean bookDescriptionFilter = true;
        boolean availableCopiesFilter = true;

        if (name != null && !name.isEmpty()) {
            String bookName = book.getName().toLowerCase();
            nameFilter = bookName.contains(name.toLowerCase());
        }

        if (author != null && !author.isEmpty()) {
            String bookAuthor = book.getAuthorName().toLowerCase();
            authorFilter = bookAuthor.toLowerCase().contains(author.toLowerCase());
        }

        if (publisher != null && !publisher.isEmpty()) {
            publisherFilter = book.getPublisher().toLowerCase().contains(publisher.toLowerCase());
        }

        if (yearOfPublishing != null && !yearOfPublishing.isEmpty()) {
            yearFilter = String.valueOf(book.getYearOfPublishing()).contains(yearOfPublishing);
        }

        if (pages != null && !pages.isEmpty()) {
            pagesFilter = String.valueOf(book.getPages()).contains(pages);
        }

        if (bookDescription != null && !bookDescription.isEmpty()) {
            bookDescriptionFilter = book.getBookDescription().toLowerCase().contains(bookDescription.toLowerCase());
        }

        if(availableCopies != null && !availableCopies.isEmpty()){
            availableCopiesFilter = String.valueOf(book.getAvailableCopies()).contains(availableCopies);
        }

        return nameFilter && authorFilter && publisherFilter && yearFilter && pagesFilter && bookDescriptionFilter && availableCopiesFilter;
    }

}