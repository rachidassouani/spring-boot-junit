package io.rachidasouani.springbootjunit.dao;

import io.rachidasouani.springbootjunit.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findEmployeeByEmail(String email);
}
