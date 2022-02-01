package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements EmployeeServiceInterface{

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        super();
        this.employeeRepository = employeeRepository;
    }
    
    @Override
    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(long id) {
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if(existingEmployee == null){
            return null;
        } else{
        return existingEmployee;
        }
    }

    @Override
    public Employee deleteEmployee(long id) {
        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
        if(existingEmployee == null){
            return null;
        } else{
        employeeRepository.deleteById(id);
        return existingEmployee;
        }
    }
}

