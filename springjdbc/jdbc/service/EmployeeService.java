package com.springjdbc.jdbc.service;

import com.springjdbc.jdbc.entity.Employee;
import com.springjdbc.jdbc.payload.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    private final EmployeeDao employeeDao;
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public EmployeeService(EmployeeDao employeeDao, SimpleJdbcInsert jdbcInsert, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.employeeDao = employeeDao;
        this.jdbcInsert = jdbcInsert;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public int createEmployee(Employee employee) {
     return    employeeDao.save(employee);
    }

    public int updateEmployee(Employee employee, int id) {
      if( employeeDao.existsById(id)) {
          return employeeDao.update(employee,id);
      }
      return 0;
    }

    public int deleteByEmployeeId(int id) {
        if( employeeDao.existsById(id)) {
            return employeeDao.deleteById(id);
        }
        return 0;
    }

    public Employee getEmployeeById(int id) {
        if( employeeDao.existsById(id)) {
            return employeeDao.findById(id);
        }
        return null;
    }

    public List<Employee> getAllEmployee() {
        return employeeDao.findAll();
    }
    public int save(Employee emp) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("name", emp.getName());
//        params.put("department", emp.getDepartment());
//        params.put("salary", emp.getSalary());
//
//        // Auto generates SQL: INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)
//        // Returns generated primary key
//        return jdbcInsert.executeAndReturnKey(params).intValue();
        SqlParameterSource params = new BeanPropertySqlParameterSource(emp);
        return jdbcInsert.executeAndReturnKey(params).intValue();
    }

    public Employee updateEmployeenameParameter(Employee emp, int id) {
        String sql = "UPDATE employees SET name=:name, department=:department, salary=:salary WHERE id=:id";
//        emp.setId(id);
//        SqlParameterSource params = new BeanPropertySqlParameterSource(emp);

//
//        Map<String, Object> params = new HashMap<>();
//        params.put("name", emp.getName());
//        params.put("department", emp.getDepartment());
//        params.put("salary", emp.getSalary());
//        params.put("id", id);
        // ensure id is included
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", emp.getName())
                .addValue("department", emp.getDepartment())
                .addValue("salary", emp.getSalary())
                .addValue("id", id);

        int rowsUpdated = namedParameterJdbcTemplate.update(sql, params);

        if (rowsUpdated > 0) {
            emp.setId(id);
            return emp;
        } else {
            System.out.println("sssssss");
            return null;
        }
    }
}
