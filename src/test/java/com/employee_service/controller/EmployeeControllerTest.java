package com.employee_service.controller;

import com.employee_service.dto.EmployeeRequest;
import com.employee_service.dto.EmployeeResponse;
import com.employee_service.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
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
    @WithMockUser
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
    @WithMockUser
    void createEmployee_ShouldReturnCreated() throws Exception {
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Juan");
        request.setSecondName("Carlos");
        request.setPaternalLastName("Perez");
        request.setMaternalLastName("Gomez");
        request.setAge(30);
        request.setGender("MALE");

        Calendar calendar = Calendar.getInstance();
        calendar.set(1993, Calendar.MAY, 15);
        request.setBirthDate(calendar.getTime());

        request.setPosition("Developer");
        request.setIsActive(true);

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName("Juan");
        response.setPaternalLastName("Perez");

        when(employeeService.createEmployee(any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan"));
    }

    @Test
    @WithMockUser
    void updateEmployee_ShouldReturnUpdatedEmployee() throws Exception {
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Juan Carlos");
        request.setSecondName("Alberto");
        request.setPaternalLastName("Perez");
        request.setMaternalLastName("Gomez");
        request.setAge(31);
        request.setGender("MALE");

        Calendar calendar = Calendar.getInstance();
        calendar.set(1993, Calendar.MAY, 15);
        request.setBirthDate(calendar.getTime());

        request.setPosition("Senior Developer");
        request.setIsActive(true);

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName("Juan Carlos");
        response.setPaternalLastName("Perez");

        when(employeeService.updateEmployee(eq(1L), any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/employees/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan Carlos"));
    }

    @Test
    @WithMockUser
    void deleteEmployee_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
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