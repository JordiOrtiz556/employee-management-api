package com.employee_service.repository;

import com.employee_service.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void findByIsActiveTrue_ShouldReturnOnlyActiveEmployees() {
        Employee activeEmployee = new Employee();
        activeEmployee.setFirstName("Active");
        activeEmployee.setPaternalLastName("Employee");
        activeEmployee.setAge(30);
        activeEmployee.setGender("MALE");
        activeEmployee.setBirthDate(new Date());
        activeEmployee.setPosition("Developer");
        activeEmployee.setIsActive(true);

        Employee inactiveEmployee = new Employee();
        inactiveEmployee.setFirstName("Inactive");
        inactiveEmployee.setPaternalLastName("Employee");
        inactiveEmployee.setAge(25);
        inactiveEmployee.setGender("FEMALE");
        inactiveEmployee.setBirthDate(new Date());
        inactiveEmployee.setPosition("Tester");
        inactiveEmployee.setIsActive(false);

        entityManager.persist(activeEmployee);
        entityManager.persist(inactiveEmployee);
        entityManager.flush();

        List<Employee> result = employeeRepository.findByIsActiveTrue();

        assertEquals(1, result.size());
        assertEquals("Active", result.get(0).getFirstName());
    }

    @Test
    void findByIdAndIsActiveTrue_WhenEmployeeActive_ShouldReturnEmployee() {
        Employee employee = new Employee();
        employee.setFirstName("Juan");
        employee.setPaternalLastName("Perez");
        employee.setAge(30);
        employee.setGender("MALE");
        employee.setBirthDate(new Date());
        employee.setPosition("Developer");
        employee.setIsActive(true);

        Employee saved = entityManager.persist(employee);
        entityManager.flush();

        Optional<Employee> result = employeeRepository.findByIdAndIsActiveTrue(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getFirstName());
    }

    @Test
    void searchActiveEmployeesByName_ShouldReturnMatchingEmployees() {
        Employee employee = new Employee();
        employee.setFirstName("Juan");
        employee.setPaternalLastName("Perez");
        employee.setAge(30);
        employee.setGender("MALE");
        employee.setBirthDate(new Date());
        employee.setPosition("Developer");
        employee.setIsActive(true);

        entityManager.persist(employee);
        entityManager.flush();

        List<Employee> result = employeeRepository.searchActiveEmployeesByName("Juan");

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getFirstName());
    }
}