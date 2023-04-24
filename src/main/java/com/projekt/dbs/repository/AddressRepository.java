package com.projekt.dbs.repository;

import com.projekt.dbs.entities.Address;
import com.projekt.dbs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE a.street LIKE %:searchTerm%")
    List<User> searchByStreet(@Param("searchTerm")String searchTerm);

    @Query("SELECT a FROM Address a WHERE a.zip = searchTerm")
    List<User> searchByZip(@Param("searchTerm")String searchTerm);

    @Query("SELECT a FROM Address a WHERE a.addressId = searchTerm")
    Address searchByAddressID(@Param("searchTerm")Long searchTerm);

}
