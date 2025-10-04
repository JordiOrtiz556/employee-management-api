package com.employee_service.repository;

import com.employee_service.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByIsActiveTrue();

    Optional<Employee> findByIdAndIsActiveTrue(Long id);

    @Query("SELECT e FROM Employee e WHERE " +
            "(LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(e.paternalLastName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "e.isActive = true")
    List<Employee> searchActiveEmployeesByName(@Param("name") String name);

    boolean existsByFirstNameAndPaternalLastNameAndMaternalLastName(
            String firstName, String paternalLastName, String maternalLastName);
}