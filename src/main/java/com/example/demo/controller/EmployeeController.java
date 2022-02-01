package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import com.example.demo.model.Employee;
import com.example.demo.exception.EmptyInputException;
import com.example.demo.exception.ErrorMessage;
import com.example.demo.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@ControllerAdvice
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        if (employee.getName().isEmpty() || employee.getName().length() == 0) {
            throw new EmptyInputException("Failed while saving the employee, Name cannot be empty!");
        } else {
            Employee employee2 = employeeService.saveEmployee(employee);
            return new ResponseEntity<Employee>(
                    employee2, HttpStatus.CREATED);
        }

    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();

    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") long id,
            @RequestBody Employee employee) {
        Employee existingEmployee = employeeService.getEmployeeById(id);
        if (existingEmployee == null) {
            throw new NoSuchElementException("Employee with id " + id + " not found");
        } else {
            Employee employeeSaved = employeeService.saveEmployee(employee);
            return new ResponseEntity<Employee>(
                    employeeSaved, HttpStatus.ACCEPTED);
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") long id) {
        Employee existingEmployee = employeeService.deleteEmployee(id);
        if (existingEmployee == null) {
            throw new NoSuchElementException("Failed to delete the employee, Employee with id " + id + " not found");
        } else {
            return new ResponseEntity<String>("Employee with id " + id + " deleted successfully!", HttpStatus.ACCEPTED);
        }
    }

    @ExceptionHandler(value = { NoSuchElementException.class })
    public ResponseEntity<Object> NoSuchElementException(Exception exception, WebRequest request) {
        String errorMessageDescription = exception.getLocalizedMessage();
        if (errorMessageDescription == null)
            errorMessageDescription = exception.toString();
        int errorCode = 404;
        ErrorMessage errorMessage = new ErrorMessage(errorCode, errorMessageDescription);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { EmptyInputException.class })
    public ResponseEntity<Object> handleEmptyInputException(Exception exception, WebRequest request) {

        String errorMessageDescription = exception.getLocalizedMessage();

        if (errorMessageDescription == null)
            errorMessageDescription = exception.toString();

        int errorCode = 406;
        ErrorMessage errorMessage = new ErrorMessage(errorCode, errorMessageDescription);

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception exception, WebRequest request) {

        String errorMessageDescription = exception.getLocalizedMessage();

        if (errorMessageDescription == null)
            errorMessageDescription = exception.toString();

        int errorCode = 500;
        ErrorMessage errorMessage = new ErrorMessage(errorCode, errorMessageDescription);

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
