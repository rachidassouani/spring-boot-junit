package io.rachidasouani.springbootjunit.service;

import io.rachidasouani.springbootjunit.dao.EmployeeRepository;
import io.rachidasouani.springbootjunit.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findEmployeeById(long employeeId) {
        return employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Oops, cannot find the employee with that ID :" + employeeId));
    }

    @Override
    public Employee findEmployeeByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email);
    }

    @Override
    public Employee updateEmployee(long oldEmployeeId, Employee newEmployee) {
        Employee oldEmployee = employeeRepository.findById(oldEmployeeId).get();
        oldEmployee.setFirstName(newEmployee.getFirstName());
        oldEmployee.setLastName(newEmployee.getLastName());
        oldEmployee.setEmail(newEmployee.getEmail());
        oldEmployee.setBirthDate(newEmployee.getBirthDate());
        Employee updatedEmployee = employeeRepository.save(oldEmployee);
        return updatedEmployee;
    }

    @Override
    public void deleteEmployee(long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

}
