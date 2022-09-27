package io.rachidasouani.springbootjunit.integration;

import io.rachidasouani.springbootjunit.dao.EmployeeRepository;
import io.rachidasouani.springbootjunit.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {

    @LocalServerPort
    private int portNumber;
    private String baseUrl = "http://localhost";
    
    private Employee firstEmployee;
    private Employee secondEmployee;

    @Autowired
    private EmployeeRepository employeeRepository;

    private static RestTemplate restTemplate;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void beforeSetup() {
        baseUrl = baseUrl + ":" + portNumber + "/api/v1/employees";

        firstEmployee = new Employee();
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

    @AfterEach
    public void afterSetup() {
        employeeRepository.deleteAll();
    }

    @Test
    void shouldSaveEmployeeTest() {
        // saving new employee
        Employee savedEmployee = restTemplate.postForObject(baseUrl, firstEmployee, Employee.class);

        assertNotNull(savedEmployee);
        assertThat(savedEmployee.getId()).isNotNull();
    }

    @Test
    void shouldFindAllEmployeeTest() {
        // saving new employee
        restTemplate.postForObject(baseUrl, firstEmployee, Employee.class);

        // saving another employee
        restTemplate.postForObject(baseUrl, secondEmployee, Employee.class);

        List<Employee> allEmployees = restTemplate.getForObject(baseUrl, List.class);

        assertNotNull(allEmployees);
        assertThat(allEmployees.size()).isEqualTo(2);
    }

    @Test
    void shouldFindOneEmployeeTest() {
        // saving new employee
        Employee savedFirstEmployee = restTemplate.postForObject(baseUrl, firstEmployee, Employee.class);

        // saving saved employee by its ID
        Employee existingEmployee = restTemplate.getForObject(baseUrl + "/searchById/" + savedFirstEmployee.getId(), Employee.class);

        assertNotNull(existingEmployee);
        assertEquals("Rachid", existingEmployee.getFirstName());
    }


    @Test
    void shouldDeleteMovieTest() {
        // saving new employee
        Employee savedFirstEmployee = restTemplate.postForObject(baseUrl, firstEmployee, Employee.class);

        // saving another employee
        restTemplate.postForObject(baseUrl, secondEmployee, Employee.class);

        // deleting an employee
        restTemplate.delete(baseUrl + "/" + savedFirstEmployee.getId());

        // fetching list of employees
        List<Employee> allEmployees = restTemplate.getForObject(baseUrl, List.class);

        assertThat(allEmployees).isNotNull();
        assertEquals(1, allEmployees.size());
    }

    @Test
    void shouldUpdateEmployeeTest() {
        // saving new employee
        Employee savedFirstEmployee = restTemplate.postForObject(baseUrl, firstEmployee, Employee.class);

        // setting new values for the existing employee
        savedFirstEmployee.setFirstName("xx");
        savedFirstEmployee.setLastName("yy");
        savedFirstEmployee.setEmail("xx@yy.com");

        // update employee
        restTemplate.put(baseUrl + "/{employeeId}" , savedFirstEmployee, savedFirstEmployee.getId());

        Employee updatedEmployee = restTemplate
                .getForObject(baseUrl+ "/searchById/" + savedFirstEmployee.getId(), Employee.class);

        assertNotNull(updatedEmployee);
        assertEquals("xx", updatedEmployee.getFirstName());
        assertEquals("yy", updatedEmployee.getLastName());
        assertEquals("xx@yy.com", updatedEmployee.getEmail());
    }
}
