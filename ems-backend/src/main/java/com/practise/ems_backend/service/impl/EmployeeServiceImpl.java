package com.practise.ems_backend.service.impl;

import com.practise.ems_backend.dto.EmployeeDto;
import com.practise.ems_backend.entity.Employee;
import com.practise.ems_backend.exception.ResourceNotFoundException;
import com.practise.ems_backend.mapper.EmployeeMapper;
import com.practise.ems_backend.repository.EmployeeRepository;
import com.practise.ems_backend.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        // first convert employeedto to employeeJPA entity since we need to store in db
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    // if no employee - raise no ResourceNotFoundException
    @Override
    public EmployeeDto getEmployee(Long employeeId) {
        Employee receivedEmployee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee does not exists with given id" + employeeId));
        return EmployeeMapper.mapToEmployeeDto(receivedEmployee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
       List<Employee> employees = employeeRepository.findAll();
       return employees.stream().map((employee) -> EmployeeMapper.mapToEmployeeDto(employee)).collect(Collectors.toList());

    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {
       // first check whether employee with id exists or not
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(()->  new ResourceNotFoundException("Employee doesnot exists with the given id"));
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setEmail(updatedEmployee.getEmail());
        // save returns updated employee
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("No employee with given id to delete"));
        employeeRepository.deleteById(employeeId);
    }
}
