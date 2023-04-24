package com.projekt.dbs.repository;

import com.projekt.dbs.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT b FROM Book b WHERE b.name LIKE %:searchTerm%")
    List<Book> searchByName(@Param("searchTerm")String searchTerm);

    @Query("SELECT b FROM Book b WHERE b.bookAuthor.name LIKE %:searchTerm%")
    List<Book> searchByAuthor(@Param("searchTerm")String searchTerm);

    @Query("SELECT b FROM Book b WHERE b.yearOfPublishing = searchTerm")
    List<Book> searchByYearOfPublishing(@Param("searchTerm")Integer searchTerm);

    @Query("SELECT b FROM Book b WHERE b.publisher LIKE %:searchTerm%")
    List<Book> searchByPublisher(@Param("searchTerm")String searchTerm);

    @Query("SELECT b FROM Book b WHERE b.pages LIKE %:searchTerm%")
    List<Book> searchByPages(@Param("searchTerm")Integer searchTerm);

    @Query("SELECT b FROM Book b WHERE b.bookDescription LIKE %:searchTerm%")
    List<Book> searchByDescription(@Param("searchTerm")String searchTerm);

    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.availableCopies = b.availableCopies - 1 WHERE b.isbn = :isbn")
    void reduceAvailableCopies(@Param("isbn") Integer isbn);


    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.availableCopies = b.availableCopies + 1 WHERE b.isbn = :isbn")
    void increaseAvailableCopies(@Param("isbn") Integer isbn);

    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.bookAuthor.authorId = :authorId WHERE b.isbn = :isbn")
    void setBookAuthor(@Param("authorId") Long authorId, @Param("isbn") Integer isbn);
}
