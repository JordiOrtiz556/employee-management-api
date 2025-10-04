package com.employee_service.service;

import com.employee_service.dto.EmployeeRequest;
import com.employee_service.dto.EmployeeResponse;
import com.employee_service.entity.Employee;
import com.employee_service.exception.EmployeeNotFoundException;
import com.employee_service.exception.DuplicateEmployeeException;
import com.employee_service.repository.EmployeeRepository;
import com.employee_service.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeRequest employeeRequest;
    private EmployeeResponse employeeResponse;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Juan");
        employee.setPaternalLastName("Perez");
        employee.setAge(30);
        employee.setGender("MALE");
        employee.setPosition("Developer");
        employee.setIsActive(true);

        employeeRequest = new EmployeeRequest();
        employeeRequest.setFirstName("Juan");
        employeeRequest.setPaternalLastName("Perez");
        employeeRequest.setAge(30);
        employeeRequest.setGender("MALE");
        employeeRequest.setPosition("Developer");

        employeeResponse = new EmployeeResponse();
        employeeResponse.setId(1L);
        employeeResponse.setFirstName("Juan");
        employeeResponse.setPaternalLastName("Perez");
    }

    @Test
    void getAllEmployees_ShouldReturnActiveEmployees() {
        when(employeeRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(employee));
        when(modelMapper.map(any(Employee.class), eq(EmployeeResponse.class))).thenReturn(employeeResponse);

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getFirstName());
        verify(employeeRepository, times(1)).findByIsActiveTrue();
    }

    @Test
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() {
        when(employeeRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(employee));
        when(modelMapper.map(any(Employee.class), eq(EmployeeResponse.class))).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals("Juan", result.getFirstName());
        verify(employeeRepository, times(1)).findByIdAndIsActiveTrue(1L);
    }

    @Test
    void getEmployeeById_WhenEmployeeNotFound_ShouldThrowException() {
        when(employeeRepository.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(1L));
        verify(employeeRepository, times(1)).findByIdAndIsActiveTrue(1L);
    }

    @Test
    void createEmployee_ShouldSaveAndReturnEmployee() {
        when(employeeRepository.existsByFirstNameAndPaternalLastNameAndMaternalLastName(any(), any(), any()))
                .thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(modelMapper.map(any(EmployeeRequest.class), eq(Employee.class))).thenReturn(employee);
        when(modelMapper.map(any(Employee.class), eq(EmployeeResponse.class))).thenReturn(employeeResponse);

        EmployeeResponse result = employeeService.createEmployee(employeeRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_WhenDuplicateExists_ShouldThrowException() {
        when(employeeRepository.existsByFirstNameAndPaternalLastNameAndMaternalLastName(any(), any(), any()))
                .thenReturn(true);

        assertThrows(DuplicateEmployeeException.class, () -> employeeService.createEmployee(employeeRequest));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_ShouldSoftDeleteEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.deleteEmployee(1L);

        assertFalse(employee.getIsActive());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void searchEmployeesByName_ShouldReturnMatchingEmployees() {
        when(employeeRepository.searchActiveEmployeesByName("Juan")).thenReturn(Arrays.asList(employee));
        when(modelMapper.map(any(Employee.class), eq(EmployeeResponse.class))).thenReturn(employeeResponse);

        List<EmployeeResponse> result = employeeService.searchEmployeesByName("Juan");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(employeeRepository, times(1)).searchActiveEmployeesByName("Juan");
    }
}