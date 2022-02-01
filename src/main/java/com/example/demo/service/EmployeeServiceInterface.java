package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Employee;

public interface EmployeeServiceInterface {

    public Employee saveEmployee(Employee employee);

    public List<Employee> getAllEmployees();

    public Employee getEmployeeById(long id);

    public Employee deleteEmployee(long id);
    
    
}
