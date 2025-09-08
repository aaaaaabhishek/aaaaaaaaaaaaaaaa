package com.springjdbc.jdbc.payload;

import com.springjdbc.jdbc.entity.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class EmployeeDao {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(Employee employee) {
        String sql = "INSERT INTO EMPLOYEES(name,department,salary) VALUES(?,?,?)";
        return    jdbcTemplate.update(sql,employee.getName(),employee.getDepartment(),employee.getSalary());
    }

    public boolean existsById(int id) {
        String sql="SELECT COUNT(*) FROM EMPLOYEES WHERE id=?";
        Integer result=jdbcTemplate.queryForObject(sql,Integer.class,id);
        return  result!=null && result>0;
    }

    public int update(Employee employee, int id) {
        String sql = "UPDATE employees SET name = ?, department = ?, salary = ? WHERE id = ?";
       return jdbcTemplate.update(sql,employee.getName(),employee.getDepartment(),employee.getSalary(),id);
    }

    public int deleteById(int id) {
        String sql="DELETE FROM employees WHERE id=?";
       return   jdbcTemplate.update(sql,id);
    }

//    public Employee findById(int id) {
//        String sql="SELECT * FROM employees WHERE id=?";
//       return jdbcTemplate.queryForObject(sql,new EmployeeRowMapper(),id);
//    }
    public Employee findById(int id){
        String sql="SELECT * FROM employees WHERE id=?";
        return jdbcTemplate.query(sql,new Object[]{id},rs-> {
            if(rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setDepartment(rs.getString("department"));
                employee.setSalary(rs.getDouble("salary"));
                return employee;
            }
            return null;
        });
    }
//
//    public List<Employee> findAll() {
//        String sql="SELECT * FROM employees";
//        return jdbcTemplate.query(sql,new EmployeeRowMapper());
//
//    }

    public List<Employee> findAll() {
        String sql="SELECT * FROM employees";
        return jdbcTemplate.query(sql,rs->{
            List<Employee> employeeslist=new ArrayList<>();
            while(rs.next()){
                Employee employee=new Employee();
                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setDepartment(rs.getString("department"));
                employee.setSalary(rs.getDouble("salary"));
                employeeslist.add(employee);
            }
            return employeeslist;
        });
    }


    public static class EmployeeRowMapper implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee emp = new Employee();
            emp.setId(rs.getInt("id"));
            emp.setName(rs.getString("name"));
            emp.setDepartment(rs.getString("department"));
            emp.setSalary(rs.getDouble("salary"));
            return emp;
        }
    }

}
