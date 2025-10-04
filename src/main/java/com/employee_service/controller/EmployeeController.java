package com.employee_service.controller;

import com.employee_service.dto.EmployeeRequest;
import com.employee_service.dto.EmployeeResponse;
import com.employee_service.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        log.info("GET /api/v1/employees - Fetching all employees");
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        log.info("GET /api/v1/employees/{} - Fetching employee by id", id);
        EmployeeResponse employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        log.info("POST /api/v1/employees - Creating new employee");
        EmployeeResponse createdEmployee = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<EmployeeResponse>> createMultipleEmployees(
            @Valid @RequestBody List<EmployeeRequest> requests) {
        log.info("POST /api/v1/employees/batch - Creating {} employees", requests.size());
        List<EmployeeResponse> createdEmployees = employeeService.createMultipleEmployees(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {
        log.info("PUT /api/v1/employees/{} - Updating employee", id);
        EmployeeResponse updatedEmployee = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("DELETE /api/v1/employees/{} - Deleting employee", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchEmployeesByName(
            @RequestParam String name) {
        log.info("GET /api/v1/employees/search?name={} - Searching employees", name);
        List<EmployeeResponse> employees = employeeService.searchEmployeesByName(name);
        return ResponseEntity.ok(employees);
    }
}