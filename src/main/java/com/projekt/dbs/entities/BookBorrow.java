package com.projekt.dbs.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class BookBorrow {
    @Id
    @NotNull
    @GeneratedValue
    private Long bookBorrowId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User borrower;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "bookISBN", referencedColumnName = "isbn")
    private Book book;

    private LocalDate loanDate;

    @ManyToOne
    @JoinColumn(name = "employeeId", referencedColumnName = "employeeId")
    private Employee lender;

    private LocalDate dueDate;

    private boolean active;

    private int daysOverDueDate;

    private boolean returned;

    private LocalDate returnDate;

    public BookBorrow(Book book, User borrower) {
        this.returned = false;
        this.borrower = borrower;
        this.active = false;
        this.book = book;
        this.returnDate = null;
        this.returned = false;
    }

    public BookBorrow(){}

    public Long getBookBorrowId() {
        return bookBorrowId;
    }

    public User getBorrower() {
        return borrower;
    }

    public String getBorrowerName(){
        return borrower.getFirstName() + " " + borrower.getLastName();
    }

    public Book getBook() {
        return book;
    }

    public String getBookAuthor(){
        return book.getAuthorName();
    }

    public Integer getBorrowerId(){
        return borrower.getId();
    }

    public String getBookName(){
        return book.getName();
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public Employee getLender() {
        return lender;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getDaysOverDueDate() {
        return daysOverDueDate;
    }

    public boolean isOverDueDate(){
        if(this.daysOverDueDate > 1){
            return true;
        } else {
            return false;
        }
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

}
