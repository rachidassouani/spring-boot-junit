package io.rachidasouani.springbootjunit.service;

import io.rachidasouani.springbootjunit.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> findAllEmployees();
    Employee findEmployeeById(long employeeId);
    Employee findEmployeeByEmail(String email);
    Employee updateEmployee(long oldEmployeeId, Employee newEmployee);
    void deleteEmployee(long employeeId);
}
