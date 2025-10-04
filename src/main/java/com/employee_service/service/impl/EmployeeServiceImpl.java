package com.employee_service.service.impl;

import com.employee_service.dto.EmployeeRequest;
import com.employee_service.dto.EmployeeResponse;
import com.employee_service.entity.Employee;
import com.employee_service.exception.EmployeeNotFoundException;
import com.employee_service.exception.DuplicateEmployeeException;
import com.employee_service.repository.EmployeeRepository;
import com.employee_service.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        log.info("Fetching all active employees");
        return employeeRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        log.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return convertToResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        log.info("Creating new employee: {}", request.getFirstName());

        if (employeeRepository.existsByFirstNameAndPaternalLastNameAndMaternalLastName(
                request.getFirstName(),
                request.getPaternalLastName(),
                request.getMaternalLastName())) {
            throw new DuplicateEmployeeException("Employee already exists with same name");
        }

        Employee employee = modelMapper.map(request, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);

        log.info("Employee created successfully with id: {}", savedEmployee.getId());
        return convertToResponse(savedEmployee);
    }

    @Override
    @Transactional
    public List<EmployeeResponse> createMultipleEmployees(List<EmployeeRequest> requests) {
        log.info("Creating {} employees in batch", requests.size());
        return requests.stream()
                .map(this::createEmployee)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        log.info("Updating employee with id: {}", id);

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        if (request.getFirstName() != null) {
            existingEmployee.setFirstName(request.getFirstName());
        }
        if (request.getSecondName() != null) {
            existingEmployee.setSecondName(request.getSecondName());
        }
        if (request.getPaternalLastName() != null) {
            existingEmployee.setPaternalLastName(request.getPaternalLastName());
        }
        if (request.getMaternalLastName() != null) {
            existingEmployee.setMaternalLastName(request.getMaternalLastName());
        }
        if (request.getAge() != null) {
            existingEmployee.setAge(request.getAge());
        }
        if (request.getGender() != null) {
            existingEmployee.setGender(request.getGender());
        }
        if (request.getBirthDate() != null) {
            existingEmployee.setBirthDate(request.getBirthDate());
        }
        if (request.getPosition() != null) {
            existingEmployee.setPosition(request.getPosition());
        }
        if (request.getIsActive() != null) {
            existingEmployee.setIsActive(request.getIsActive());
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        log.info("Employee updated successfully with id: {}", id);
        return convertToResponse(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Soft deleting employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        employee.setIsActive(false);
        employeeRepository.save(employee);
        log.info("Employee soft deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> searchEmployeesByName(String name) {
        log.info("Searching employees with name containing: {}", name);
        return employeeRepository.searchActiveEmployeesByName(name)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private EmployeeResponse convertToResponse(Employee employee) {
        return modelMapper.map(employee, EmployeeResponse.class);
    }
}