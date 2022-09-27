package io.rachidasouani.springbootjunit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rachidasouani.springbootjunit.model.Employee;
import io.rachidasouani.springbootjunit.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Employee firstEmployee;
    private Employee secondEmployee;
    private static final String URL_TEMPLATE = "/api/v1/employees";

    @BeforeEach
    public void init() {
        // Arrange: Setting up the data that required for the test case
        firstEmployee = new Employee();
        firstEmployee.setId(1L);
        firstEmployee.setFirstName("Rachid");
        firstEmployee.setLastName("Assouani");
        firstEmployee.setEmail("rachid@assouani.com");
        firstEmployee.setBirthDate(LocalDate.of(2020, Month.JANUARY, 1));

        secondEmployee = new Employee();
        secondEmployee.setId(2L);
        secondEmployee.setFirstName("Rachida");
        secondEmployee.setLastName("Assouani");
        secondEmployee.setEmail("rachida@assouani.com");
        secondEmployee.setBirthDate(LocalDate.of(2022, Month.JANUARY, 2));
    }



    @Test
    void shouldSavedNewEmployee() throws Exception {
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(firstEmployee);

        this.mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstEmployee)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(firstEmployee.getFirstName())))
                .andExpect(jsonPath("$.email", is(firstEmployee.getEmail())));
    }

    @Test
    void shouldReturnAllEmployees() throws Exception {
        List<Employee> allEmployees = Arrays.asList(firstEmployee, secondEmployee);

        when(employeeService.findAllEmployees()).thenReturn(allEmployees);

        this.mockMvc.perform(get(URL_TEMPLATE)
                .content(objectMapper.writeValueAsString(allEmployees)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(allEmployees.size())));
    }


    @Test
    void shouldReturnEmployeeByItsId() throws Exception {
        when(employeeService.findEmployeeById(anyLong())).thenReturn(firstEmployee);

        this.mockMvc.perform(get(URL_TEMPLATE + "/searchById/{employeeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstEmployee)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void shouldDeleteEmployeeByItsId() throws Exception {
        doNothing().when(employeeService).deleteEmployee(anyLong());

        this.mockMvc.perform(delete(URL_TEMPLATE + "/{employeeId}", 1L))

                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateTheEmployee() throws Exception{
        when(employeeService.updateEmployee(anyLong(), any(Employee.class))).thenReturn(firstEmployee);

        this.mockMvc.perform(put(URL_TEMPLATE + "/{employeeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(firstEmployee)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(firstEmployee.getFirstName())));
    }
}