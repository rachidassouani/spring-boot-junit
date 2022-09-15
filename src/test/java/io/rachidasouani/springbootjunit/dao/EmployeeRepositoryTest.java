package io.rachidasouani.springbootjunit.dao;

import io.rachidasouani.springbootjunit.model.Employee;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class EmployeeRepositoryTest {

    private final EmployeeRepository employeeRepository;

    private Employee firstEmployee;
    private Employee secondEmployee;

    public EmployeeRepositoryTest(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @BeforeEach
    public void init() {

        // Arrange: Setting up the data that required for the test case
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
    public void afterEach() {
        employeeRepository.deleteAll();
    }

    @Test
    @DisplayName("It should save the employee to the database")
    public void save() {

        // Act: calling a method/Unit that is going to be tested
        Employee savedEmployee = employeeRepository.save(firstEmployee);

        // Assert: Verity that the expected result is correct or not
        assertNotNull(savedEmployee);
        assertThat(savedEmployee.getId()).isNotEqualTo(null);
    }

    @Test
    @DisplayName("It should find all employees 'they are 2'")
    public void findAllEmployees() {

        employeeRepository.save(firstEmployee);
        employeeRepository.save(secondEmployee);

        // Act: calling a method/Unit that is going to be tested
        List<Employee> allEmployees = employeeRepository.findAll();

        // Assert: Verity that the expected result is correct or not
        assertNotNull(allEmployees);
        assertEquals(2, allEmployees.size());
    }

    @Test
    @DisplayName("It should find employee by its ID")
    public void findEmployeeById() {

        Employee savedEmployee = employeeRepository.save(firstEmployee);

        // Act: calling a method/Unit that is going to be tested
        Employee employeeFromDB = employeeRepository.findById(savedEmployee.getId()).get();

        // Assert: Verity that the expected result is correct or not
        assertNotNull(employeeFromDB);
        assertThat(employeeFromDB.getFirstName()).isEqualTo("Rachid");
        assertThat(employeeFromDB.getLastName()).isEqualTo("Assouani");
        assertThat(employeeFromDB.getBirthDate()).isBefore(LocalDate.of(2020, Month.JANUARY, 2));
    }

    @Test
    @DisplayName("It should find employee by its email")
    public void findEmployeeByEmail() {

        employeeRepository.save(firstEmployee);

        // Act: calling a method/Unit that is going to be tested
        Employee employeeFromDB = employeeRepository.findEmployeeByEmail("rachid@assouani.com");

        // Assert: Verity that the expected result is correct or not
        assertNotNull(employeeFromDB);
        assertThat(employeeFromDB.getFirstName()).isEqualTo("Rachid");
        assertThat(employeeFromDB.getLastName()).isEqualTo("Assouani");
        assertThat(employeeFromDB.getBirthDate()).isEqualTo(LocalDate.of(2020, Month.JANUARY, 1));
    }

    @Test
    @DisplayName("It should update an existing employee")
    public void updateEmployee() {

        Employee savedEmployee = employeeRepository.save(firstEmployee);

        Employee employeeFromDB = employeeRepository.findById(savedEmployee.getId()).get();
        employeeFromDB.setFirstName("xx");
        employeeFromDB.setLastName("yy");
        employeeFromDB.setEmail("xx@yy.com");
        employeeFromDB.setBirthDate(LocalDate.of(2020, Month.JANUARY, 1));

        // Act: calling a method/Unit that is going to be tested
        Employee updatedEmployee = employeeRepository.save(employeeFromDB);

        // Assert: Verity that the expected result is correct or not
        assertNotNull(updatedEmployee);
        assertThat(updatedEmployee.getFirstName()).isEqualTo("xx");
        assertThat(updatedEmployee.getLastName()).isEqualTo("yy");
        assertThat(updatedEmployee.getEmail()).isEqualTo("xx@yy.com");
        assertThat(employeeFromDB.getBirthDate()).isEqualTo(LocalDate.of(2020, Month.JANUARY, 1));

    }

    @Test
    @DisplayName("It should delete an employee")
    public void deleteEmployeeById() {

        Employee savedEmployee = employeeRepository.save(firstEmployee);

        final long savedEmployeeId = savedEmployee.getId();

        // Act: calling a method/Unit that is going to be tested
        employeeRepository.deleteById(savedEmployeeId);

        Optional<Employee> employeeFromDB = employeeRepository.findById(savedEmployeeId);
        List<Employee> allEmployees = employeeRepository.findAll();

        // Assert: Verity that the expected result is correct or not
        assertThat(allEmployees.size()).isEqualTo(0);
        assertThat(employeeFromDB).isEqualTo(Optional.empty());
    }
}
