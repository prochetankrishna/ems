package com.thechetankrishna.ems.controller;

import com.thechetankrishna.ems.model.EmployeeRequest;
import com.thechetankrishna.ems.model.EmployeeResponse;
import com.thechetankrishna.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable("employeeId") String employeeId) {
        return new ResponseEntity<>(this.employeeService.getEmployeeById(employeeId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployee(@RequestParam(value = "sortingOrder", defaultValue = "ASC") String sortingOrder,
                                                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                 @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNumber) {
        return new ResponseEntity<>(this.employeeService.getAllEmployee(sortingOrder, pageSize, pageNumber), HttpStatus.OK);
    }

    @GetMapping("/all/length")
    public ResponseEntity<Integer> getTotalEmployees() {
        return new ResponseEntity<>(this.employeeService.getTotalEmployees(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> addNewEmployee(@RequestBody EmployeeRequest employeeRequest) {
        return new ResponseEntity<>(this.employeeService.addNewEmployee(employeeRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable("employeeId") String employeeId,
                                                           @RequestBody EmployeeRequest employeeRequest) {
        return new ResponseEntity<>(this.employeeService.updateEmployee(employeeId, employeeRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> deleteEmployeeById(@PathVariable("employeeId") String employeeId) {
        this.employeeService.deleteEmployee(employeeId);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
