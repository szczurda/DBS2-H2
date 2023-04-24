package com.projekt.dbs.repository;

import com.projekt.dbs.entities.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRating, Integer> {

    @Query("SELECT CASE WHEN COUNT(br) > 0 THEN true ELSE false END FROM BookRating br WHERE br.reviewer.id = :userId AND br.ratedBook.isbn = :bookIsbn")
    boolean alreadyExists(@Param("userId")Integer userId, @Param("bookIsbn") Integer bookIsbn);

    @Query("SELECT br.rating FROM BookRating br WHERE br.reviewer.id = :userId AND br.ratedBook.isbn = :bookIsbn")
    Integer getUsersBookRating(@Param("userId") Integer userId, @Param("bookIsbn") Integer bookIsbn);

    @Modifying
    @Transactional
    @Query("UPDATE BookRating br SET br.rating = :rating WHERE br.reviewer.id = :userId AND br.ratedBook.isbn = :bookIsbn")
    void updateUsersBookRating(@Param("rating") Integer rating, @Param("userId") Integer userId, @Param("bookIsbn") Integer bookIsbn);
}
