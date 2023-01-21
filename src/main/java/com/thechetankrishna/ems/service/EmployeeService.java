package com.thechetankrishna.ems.service;

import com.thechetankrishna.ems.model.EmployeeRequest;
import com.thechetankrishna.ems.model.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    public EmployeeResponse addNewEmployee(EmployeeRequest employeeRequest);

    public List<EmployeeResponse> getAllEmployee(String sortingOrder, int pageSize, int pageNumber);

    public EmployeeResponse getEmployeeById(String employeeId);

    public EmployeeResponse updateEmployee(String employeeId, EmployeeRequest employeeRequest);

    public void deleteEmployee(String employeeId);

    public int getTotalEmployees();
}
