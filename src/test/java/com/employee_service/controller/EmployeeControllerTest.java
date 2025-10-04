package com.employee_service.controller;

import com.employee_service.dto.EmployeeRequest;
import com.employee_service.dto.EmployeeResponse;
import com.employee_service.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllEmployees_ShouldReturnEmployees() throws Exception {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName("Juan");

        List<EmployeeResponse> employees = Arrays.asList(response);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Juan"));
    }

    @Test
    void getEmployeeById_WhenExists_ShouldReturnEmployee() throws Exception {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName("Juan");

        when(employeeService.getEmployeeById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan"));
    }

    @Test
    void createEmployee_ShouldReturnCreated() throws Exception {
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Juan");
        request.setPaternalLastName("Perez");
        request.setAge(30);
        request.setGender("MALE");
        request.setPosition("Developer");

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName("Juan");

        when(employeeService.createEmployee(any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan"));
    }

    @Test
    void updateEmployee_ShouldReturnUpdatedEmployee() throws Exception {
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Juan Carlos");

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName("Juan Carlos");

        when(employeeService.updateEmployee(eq(1L), any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan Carlos"));
    }

    @Test
    void deleteEmployee_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchEmployeesByName_ShouldReturnMatchingEmployees() throws Exception {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName("Juan");

        List<EmployeeResponse> employees = Arrays.asList(response);
        when(employeeService.searchEmployeesByName("Juan")).thenReturn(employees);

        mockMvc.perform(get("/api/v1/employees/search")
                        .param("name", "Juan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Juan"));
    }
}