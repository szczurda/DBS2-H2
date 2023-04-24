package com.projekt.dbs.repository;

import com.projekt.dbs.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e WHERE e.firstName LIKE %:searchTerm%")
    List<Employee> searchByFirstName(@Param("searchTerm")String searchTerm);

    @Query("SELECT e FROM Employee e WHERE e.lastName LIKE %:searchTerm%")
    List<Employee> searchByLastName(@Param("searchTerm")String searchTerm);

    @Query("SELECT e FROM Employee e WHERE e.email LIKE %:searchTerm%")
    List<Employee> searchByEmail(@Param("searchTerm")String searchTerm);

    @Query("SELECT e FROM Employee e WHERE e.phoneNumber = searchTerm")
    Employee searchByPhoneNumber(@Param("searchTerm")Integer searchTerm);
    @Query("SELECT e FROM Employee e WHERE e.employeeId = :searchTerm")
    Employee searchById(@Param("searchTerm")Long searchTerm);
}
