package io.rachidasouani.springbootjunit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.rachidasouani.springbootjunit.dao.EmployeeRepository;
import io.rachidasouani.springbootjunit.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee firstEmployee;
    private Employee secondEmployee;

    @BeforeEach
    void init() {
        firstEmployee = new Employee();
        firstEmployee.setId(1L);
        firstEmployee.setFirstName("Rachid");
        firstEmployee.setLastName("Assouani");
        firstEmployee.setEmail("rachid@assouani.com");
        firstEmployee.setBirthDate(LocalDate.of(2020, Month.JANUARY, 1));

        secondEmployee = new Employee();
        secondEmployee.setFirstName("Rachida");
        secondEmployee.setLastName("Assouani");
        secondEmployee.setEmail("rachida@assouani.com");
        secondEmployee.setBirthDate(LocalDate.of(2022, Month.JANUARY, 2));
    }

    @Test
    @DisplayName("Should save th employee to the database")
    void save() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(firstEmployee);

        Employee savedEmployee = employeeService.saveEmployee(firstEmployee);

        assertNotNull(savedEmployee);
        assertThat(savedEmployee.getFirstName()).isEqualTo("Rachid");
    }

    @Test
    @DisplayName("Should find All Employees")
    void findAllEmployees() {
        List<Employee> employees = Arrays.asList(firstEmployee, secondEmployee);

        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> allEmployees = employeeService.findAllEmployees();

        assertNotNull(allEmployees);
        assertEquals(2, allEmployees.size());
    }

    @Test
    @DisplayName("Should return Employee By Its Id")
    void findEmployeeById() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(firstEmployee));

        Employee foundedEmployee = employeeService.findEmployeeById(1);

        assertNotNull(foundedEmployee);
        assertThat(foundedEmployee.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should throw Exception")
    void findEmployeeByIdThrowException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(firstEmployee));

        assertThrows(RuntimeException.class, () -> employeeService.findEmployeeById(2L));
    }

    @Test
    @DisplayName("Should return Employee By Its Email")
    void findEmployeeByEmail() {
        when(employeeRepository.findEmployeeByEmail(anyString())).thenReturn(firstEmployee);

        Employee foundedEmployee = employeeService.findEmployeeByEmail("rachid@assouani.com");

        assertNotNull(foundedEmployee);
        assertThat(foundedEmployee.getEmail()).isEqualTo("rachid@assouani.com");
    }

    @Test
    @DisplayName("Should update the Employee")
    void updateEmployee() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(firstEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(firstEmployee);

        firstEmployee.setFirstName("xx");
        firstEmployee.setLastName("yy");
        firstEmployee.setEmail("xx@yy.com");

        Employee updatedEmployee = employeeService.updateEmployee(1, firstEmployee);

        assertNotNull(updatedEmployee);
        assertEquals("xx", updatedEmployee.getFirstName());
        assertEquals("yy", updatedEmployee.getLastName());
        assertEquals("xx@yy.com", updatedEmployee.getEmail());
    }

    @Test
    @DisplayName("Should delete an Employee")
    void deleteEmployee() {
        doNothing().when(employeeRepository).deleteById(anyLong());

        employeeService.deleteEmployee(1);

        verify(employeeRepository, times(1)).deleteById(1l);
    }
}
