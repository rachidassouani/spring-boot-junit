package io.rachidasouani.springbootjunit.controller;

import io.rachidasouani.springbootjunit.model.Employee;
import io.rachidasouani.springbootjunit.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return new ResponseEntity<Employee>(savedEmployee, HttpStatus.OK);
    }

    @GetMapping
    public List<Employee> findAllEmployees() {
        return employeeService.findAllEmployees();
    }

    @GetMapping("searchById/{employeeId}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable("employeeId") long employeeId) {
        Employee foundedEmployee = employeeService.findEmployeeById(employeeId);
        return new ResponseEntity<>(foundedEmployee, HttpStatus.OK);
    }

    @GetMapping("searchByEmail/{employeeEmail}")
    public ResponseEntity<Employee> findEmployeeByEmail(@PathVariable("employeeEmail") String employeeEmail) {
        Employee foundedEmployee = employeeService.findEmployeeByEmail(employeeEmail);
        return new ResponseEntity<>(foundedEmployee, HttpStatus.OK);
    }

    @PutMapping("{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("employeeId") long employeeId,
                                           @RequestBody Employee employee) {
        Employee updateEmployee = employeeService.updateEmployee(employeeId, employee);
        return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
    }

    @DeleteMapping("{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("employeeId") long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }
}
