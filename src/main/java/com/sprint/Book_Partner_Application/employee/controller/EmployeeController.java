package com.sprint.Book_Partner_Application.employee.controller;

import com.sprint.Book_Partner_Application.employee.entity.Employee;
import com.sprint.Book_Partner_Application.employee.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeRepository repo;

    public EmployeeController(EmployeeRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/add")
    public Employee addEmployee(@RequestBody Employee emp) {
        return repo.save(emp);
    }

    @GetMapping("/get")
    public List<Employee> getAll() {
        return repo.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        repo.deleteById(id);
        return "Employee Deleted Successfully";
    }
}
