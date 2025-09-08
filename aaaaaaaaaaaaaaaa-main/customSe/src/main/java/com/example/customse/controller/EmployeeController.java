package com.example.customse.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/web/getEmp")
    public String get(){
        return "ABhi";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/web/getEmp")
    public String set(){
        return "ABhi";
    }

}
