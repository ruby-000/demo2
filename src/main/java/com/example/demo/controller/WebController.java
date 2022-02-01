package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;

@Controller
@ControllerAdvice
public class WebController {
	
	@Autowired
	private EmployeeService employeeService;

	public WebController(EmployeeService employeeService){
		super();
		this.employeeService = employeeService;
	}

	@GetMapping("/")
	public String home(){
		return "Welcome";
	}

	@GetMapping("/user")
	public String user(Model model){
		model.addAttribute("employees", employeeService.getAllEmployees());
		return "home";
	}
	
	@GetMapping("/admin")
	public String indexpage(Model model) {
		model.addAttribute("employees", employeeService.getAllEmployees()); 
		return "index";
		  
	}

	@GetMapping("/admin/new")
	public String newFormForEmployee(Model model){
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "new_emp";
	}

	@PostMapping("/admin")
	public String addEmployee(Employee emp, RedirectAttributes redirectAttribute){
		employeeService.saveEmployee(emp);
		redirectAttribute.addFlashAttribute("message", "Employee added successfully");
		return "redirect:/admin";
	}

	@GetMapping("/admin/edit/{id}")
	public String editForm(@PathVariable("id")long id, Model model, RedirectAttributes redirectAttribute){
		model.addAttribute("employee", employeeService.getEmployeeById(id));
		if(employeeService.getEmployeeById(id) == null){
            redirectAttribute.addFlashAttribute("message","Employee with id " + id +" not found");
			return "redirect:/admin";
        } else{
			return "edit_emp";
		}
	}

	@PostMapping("/admin/{id}")
	public String updateEmployee(@PathVariable Long id, @ModelAttribute("employee") Employee employee,
									Model model, RedirectAttributes redirectAttribute) {
			employeeService.getEmployeeById(id);
				employeeService.saveEmployee(employee);
				redirectAttribute.addFlashAttribute("message", "Employee with Id " + id + " updated successfully");
				return "redirect:/admin";
	}

	@GetMapping("/admin/delete/{id}")
	public String deleteEmployee(@PathVariable("id")Long id, RedirectAttributes redirectAttribute){
		Employee existingEmployee = employeeService.deleteEmployee(id);
        if(existingEmployee == null){
            redirectAttribute.addFlashAttribute("message","Failed to delete the employee, Employee with id " + id +" not found");
			return "redirect:/admin";
        } else{
		redirectAttribute.addFlashAttribute("message", "Employee with Id " + id + " successfully deleted");
		return "redirect:/admin";
		}
	}


}
