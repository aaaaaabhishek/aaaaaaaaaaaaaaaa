package com.springjdbc.jdbc.controller;

import com.springjdbc.jdbc.entity.Employee;
import com.springjdbc.jdbc.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping("/getEmployeById/{id}")
    public Employee getEmployee(@PathVariable int id){
       return employeeService.getEmployeeById(id);
    }
    @GetMapping("/getAllEmployee")
    public List<Employee> getAllEmployee(){
        return employeeService.getAllEmployee();
    }
    @PostMapping
    public String createEmployee(@RequestBody  Employee employee){
       int rows=employeeService.createEmployee(employee);
         if(rows>0) return  "Employee Added";
       return "Internal Error";
    }
    @PutMapping("/update/{id}")
    public String updateEmployee(@RequestBody Employee employee, @PathVariable int id){
        int rows=employeeService.updateEmployee(employee,id);
        if(rows>0) return  "Employee Updated";
        return "Internal Error";
    }
    @DeleteMapping("/deleteById/{id}")
    public String updateEmployee(@PathVariable int id){
        int rows=employeeService.deleteByEmployeeId(id);
        if(rows>0) return  "Employee Deleted";
        return "Internal Error";
    }


    @PostMapping("/simpleInsert")
    public String addEmployee(@RequestBody Employee employee) {
        int generatedId = employeeService.save(employee);
        return "Employee saved successfully with ID: " + generatedId;
    }

    @PutMapping("/updatenameParameter/{id}")
    public Employee updateEmployeee(@RequestBody Employee employee,@PathVariable int id) {
        Employee employee1 = employeeService.updateEmployeenameParameter(employee,id);
        return  employee1;
    }

}
