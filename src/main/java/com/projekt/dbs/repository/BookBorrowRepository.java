package com.projekt.dbs.repository;

import com.projekt.dbs.entities.BookBorrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BookBorrowRepository extends JpaRepository<BookBorrow, Long> {

    @Query("SELECT bb FROM BookBorrow bb WHERE bb.id = searchTerm")
    List<BookBorrow> searchUsersBookBorrows(@Param("searchTerm")Integer searchTerm);

    @Query("SELECT bb FROM BookBorrow bb WHERE bb.book.isbn = searchTerm")
    List<BookBorrow> searchBookBorrows(@Param("searchTerm")Integer searchTerm);

    @Query("SELECT CASE WHEN COUNT(bb) > 0 THEN true ELSE false END FROM BookBorrow bb WHERE bb.book.isbn = :isbn AND bb.borrower.id = :userId AND bb.returned = FALSE")
    boolean isDuplicate(@Param("isbn")Integer isbn, @Param("userId") Integer userId);

    @Query("SELECT CASE WHEN COUNT(bb) > 0 THEN true ELSE false END FROM BookBorrow bb WHERE bb.bookBorrowId = :bookBorrowId AND bb.active = true")
    boolean isActive(@Param("bookBorrowId")Long bookBorrowId);

    @Query("SELECT bb FROM BookBorrow bb WHERE bb.active = FALSE AND bb.returned = false")
    List<BookBorrow> findPendingBookBorrows();

    @Modifying
    @Transactional
    @Query("UPDATE BookBorrow bb SET bb.lender.employeeId = :lenderId, bb.active = true, bb.loanDate = CURRENT_DATE(), bb.dueDate = DATEADD('DAY', 180, CURRENT_DATE), bb.daysOverDueDate = 0 WHERE bb.bookBorrowId = :bookBorrowId")
    void activateBookBorrow(@Param("lenderId") Long lenderId, @Param("bookBorrowId") Long bookBorrowId);

    @Query("SELECT bb FROM BookBorrow bb WHERE bb.borrower.id = :userId AND bb.returned = false")
    List<BookBorrow> findUsersBookBorrows(@Param("userId") Integer userId);

    @Query("SELECT bb FROM BookBorrow bb WHERE bb.borrower.id = :userId AND bb.returned = true")
    List<BookBorrow> findUsersReturnedBookBorrows(@Param("userId") Integer userId);

    @Query("SELECT bb FROM BookBorrow bb WHERE bb.active = TRUE")
    List<BookBorrow> findActiveBookBorrows();

    @Transactional
    @Modifying
    @Query("UPDATE BookBorrow bb SET bb.active = false, bb.returned = true, bb.returnDate = CURRENT_DATE() WHERE bb.bookBorrowId = :bookBorrowId")
    void endBookBorrow(@Param("bookBorrowId")Long bookBorrowId);

    @Query("SELECT bb FROM BookBorrow bb WHERE bb.returned = TRUE")
    List<BookBorrow> findReturnedBookBorrows();

    @Query("SELECT bb FROM BookBorrow bb WHERE bb.bookBorrowId = :bookBorrowId")
    BookBorrow getBookBorrowById(@Param("bookBorrowId") Long bookBorrowId);


}
