package com.projekt.dbs.repository;

import com.projekt.dbs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
        @Query("SELECT u FROM User u WHERE u.firstName LIKE %:searchTerm%")
        List<User> searchByFirstName(@Param("searchTerm")String searchTerm);

        @Query("SELECT u FROM User u WHERE u.lastName LIKE %:searchTerm%")
        List<User> searchByLastName(@Param("searchTerm")String searchTerm);

        @Query("SELECT u FROM User u WHERE u.email LIKE %:searchTerm%")
        User searchByEmail(@Param("searchTerm")String searchTerm);

        @Query("SELECT u FROM User u WHERE u.id = :searchTerm")
        User searchById(@Param("searchTerm")Integer searchTerm);

        @Query("SELECT u.id FROM User u WHERE u.email = :searchTerm")
        Integer getIdByEmail(@Param("searchTerm") String searchTerm);
}
