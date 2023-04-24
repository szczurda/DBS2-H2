package com.projekt.dbs.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class BookReturn {
    @Id
    @NotNull
    @GeneratedValue
    Long bookReturnId;

    @NotNull
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumns({@JoinColumn(name = "bookBorrowId", referencedColumnName = "bookBorrowId"), @JoinColumn(name = "userID", referencedColumnName = "userId")})
    BookBorrow bookBorrow;

    @NotNull
    LocalDate date;

    @NotNull
    Long employeeId;


    public BookReturn(BookBorrow bookBorrow, Long employeeId) {
        this.employeeId = employeeId;
        this.bookBorrow = bookBorrow;
        this.date = LocalDate.now();
    }

    public BookReturn() {
    }

    public Long getBookReturnId() {
        return bookReturnId;
    }

    public void setBookReturnId(Long bookReturnId) {
        this.bookReturnId = bookReturnId;
    }

    public BookBorrow getBookBorrow() {
        return bookBorrow;
    }

    public void setBookBorrow(BookBorrow bookBorrow) {
        this.bookBorrow = bookBorrow;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
