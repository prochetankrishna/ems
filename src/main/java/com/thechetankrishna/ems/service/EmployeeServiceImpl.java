package com.thechetankrishna.ems.service;

import com.thechetankrishna.ems.dto.AddressDTO;
import com.thechetankrishna.ems.dto.EmployeeDTO;
import com.thechetankrishna.ems.exception.EmployeeNotFoundException;
import com.thechetankrishna.ems.exception.InvalidEmployeeRequestException;
import com.thechetankrishna.ems.exception.NoEmployeeExistsException;
import com.thechetankrishna.ems.model.AddressRequest;
import com.thechetankrishna.ems.model.AddressResponse;
import com.thechetankrishna.ems.model.EmployeeRequest;
import com.thechetankrishna.ems.model.EmployeeResponse;
import com.thechetankrishna.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeResponse getEmployeeById(String employeeId) {
        Boolean isEmployeeExists = employeeRepository.existsById(employeeId);
        if (isEmployeeExists) {
            EmployeeDTO employeeDTO = employeeRepository.getReferenceById(employeeId);
            EmployeeResponse employeeResponse = this.createResponseFromDTO(employeeDTO);
            return employeeResponse;
        } else {
            throw new EmployeeNotFoundException("Employee with employee ID : " + employeeId +
                    " doesn't exists");
        }
    }

    //11 Total Rows -> Page Starts from 0 -> Page Size -> 25
    //Page 0 -> 11 Rows
    //Page 1 -> 0 Row
    //Page 2 -> 0 Row
    //Page 3 -> 0 Row
    @Override
    public List<EmployeeResponse> getAllEmployee(String sortingOrder, int pageSize, int pageNumber) {

        Pageable pageableWithSortingOrder = null;
        if (sortingOrder.equals("ASC")) {
            pageableWithSortingOrder = PageRequest.of(pageNumber, pageSize, Sort.by("firstName"));
        } else {
            pageableWithSortingOrder = PageRequest.of(pageNumber, pageSize,
                    Sort.by("firstName").descending());
        }

        Page<EmployeeDTO> employeeDTOPageList = this.employeeRepository.findAll(pageableWithSortingOrder);
        List<EmployeeResponse> employeeResponses =
                employeeDTOPageList.get()
                        .map(employeeDTO -> createResponseFromDTO(employeeDTO))
                        .collect(Collectors.toList());
        if (employeeResponses.size() != 0) {
            return employeeResponses;
        } else {
            throw new NoEmployeeExistsException("No More Employees in the DB");
        }
    }

    @Override
    public EmployeeResponse addNewEmployee(EmployeeRequest employeeRequest) {
        validateEmployeeRequest(employeeRequest);
        EmployeeDTO employeeDTO = createDTOFromRequest(employeeRequest);
        EmployeeDTO savedEmployeeDTO = this.employeeRepository.save(employeeDTO);
        EmployeeResponse employeeResponse = createResponseFromDTO(savedEmployeeDTO);
        return employeeResponse;
    }

    @Override
    public EmployeeResponse updateEmployee(String employeeId, EmployeeRequest employeeRequest) {
        validateEmployeeRequest(employeeRequest);
        if (this.employeeRepository.existsById(employeeId)) {
            EmployeeDTO employeeDTO = createDTOFromRequest(employeeRequest);
            EmployeeDTO savedEmployeeDTO = this.employeeRepository.save(employeeDTO);
            EmployeeResponse employeeResponse = createResponseFromDTO(savedEmployeeDTO);
            return employeeResponse;
        } else {
            throw new EmployeeNotFoundException("Employee with employee ID : " + employeeId +
                    " doesn't exists");
        }
    }

    @Override
    public void deleteEmployee(String employeeId) {
        if (this.employeeRepository.existsById(employeeId)) {
            this.employeeRepository.deleteById(employeeId);
        } else {
            throw new EmployeeNotFoundException("Employee with employee ID : " + employeeId +
                    " doesn't exists");
        }
    }

    @Override
    public int getTotalEmployees() {

        List<EmployeeDTO> employeeDTOList = this.employeeRepository.findAll();
        return employeeDTOList.size();
    }

    private EmployeeDTO createDTOFromRequest (EmployeeRequest employeeRequest) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(employeeRequest.getFirstName());
        employeeDTO.setLastName(employeeRequest.getLastName());
        employeeDTO.setSalary(employeeRequest.getSalary());

        AddressRequest addressRequest = employeeRequest.getAddress();
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setHouseNumber(addressRequest.getHouseNumber());
        addressDTO.setAddressLineOne(addressRequest.getAddressLineOne());
        addressDTO.setAddressLineTwo(addressRequest.getAddressLineTwo());
        addressDTO.setCity(addressRequest.getCity());
        addressDTO.setState(addressRequest.getState());
        addressDTO.setCountry(addressRequest.getCountry());
        addressDTO.setPincode(addressRequest.getPincode());

        employeeDTO.setAddress(addressDTO);
        return employeeDTO;
    }

    private EmployeeResponse createResponseFromDTO (EmployeeDTO employeeDTO) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployeeId(employeeDTO.getEmployeeId());
        employeeResponse.setFirstName(employeeDTO.getFirstName());
        employeeResponse.setLastName(employeeDTO.getLastName());
        employeeResponse.setSalary(employeeDTO.getSalary());

        AddressDTO addressDTO = employeeDTO.getAddress();
        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setAddressId(addressDTO.getAddressId());
        addressResponse.setHouseNumber(addressDTO.getHouseNumber());
        addressResponse.setAddressLineOne(addressDTO.getAddressLineOne());
        addressResponse.setAddressLineTwo(addressDTO.getAddressLineTwo());
        addressResponse.setCity(addressDTO.getCity());
        addressResponse.setState(addressDTO.getState());
        addressResponse.setCountry(addressDTO.getCountry());
        addressResponse.setPincode(addressDTO.getPincode());

        employeeResponse.setAddress(addressResponse);
        return employeeResponse;
    }

    private boolean validateEmployeeRequest(EmployeeRequest employeeRequest) {

        //FirstName
        if (employeeRequest.getFirstName() != null && !employeeRequest.getFirstName().isEmpty()
            && validateStringFields(employeeRequest.getFirstName())) {
            //LastName
            if (employeeRequest.getLastName() != null && !employeeRequest.getLastName().isEmpty()
            && validateStringFields(employeeRequest.getLastName())) {
                //Salary
                if (employeeRequest.getSalary() != null && !employeeRequest.getSalary().isNaN()
                && validateSalary(employeeRequest.getSalary())) {
                    return validateAddress(employeeRequest.getAddress());
                } else {
                    throw new InvalidEmployeeRequestException("Salary Cannot be less than 0.0");
                }
            } else {
                throw new InvalidEmployeeRequestException("Last Name cannot be null, empty" +
                        " and cannot contains digits (0-9)");
            }
        } else {
            throw new InvalidEmployeeRequestException("First Name cannot be null, empty" +
                    " and cannot contains digits (0-9)");
        }
    }

    private boolean validateAddress(AddressRequest addressRequest) {
        if (addressRequest.getHouseNumber() != null &&
                !addressRequest.getHouseNumber().isEmpty()) {
            if (addressRequest.getAddressLineOne() != null &&
                    !addressRequest.getAddressLineOne().isEmpty()) {
                if (addressRequest.getAddressLineTwo() != null &&
                        !addressRequest.getAddressLineTwo().isEmpty()) {
                    if (addressRequest.getCity() != null &&
                            !addressRequest.getCity().isEmpty() &&
                            validateStringFields(addressRequest.getCity())) {
                        if (addressRequest.getState() != null &&
                                !addressRequest.getState().isEmpty() &&
                                validateStringFields(addressRequest.getState())) {
                            if (addressRequest.getCountry() != null &&
                                    !addressRequest.getCountry().isEmpty() &&
                                    validateStringFields(addressRequest.getCountry())) {
                                if (addressRequest.getPincode() != null &&
                                        !addressRequest.getPincode().isEmpty()) {
                                    return true;
                                } else {
                                    throw new InvalidEmployeeRequestException("Pincode cannot be null or empty");
                                }
                            } else {
                                throw new InvalidEmployeeRequestException("Country cannot be null or empty and cannot contain digits (0-9)");
                            }
                        } else {
                            throw new InvalidEmployeeRequestException("State cannot be null or empty and cannot contain digits (0-9)");
                        }
                    } else {
                        throw new InvalidEmployeeRequestException("City cannot be null or empty and cannot contain digits (0-9)");
                    }
                } else {
                    throw new InvalidEmployeeRequestException("Address Line 2 cannot be null or empty");
                }
            } else {
                throw new InvalidEmployeeRequestException("Address Line 1 cannot be null or empty");
            }
        } else {
            throw new InvalidEmployeeRequestException("House or Flat Number cannot be null or empty");
        }
    }

    private boolean validateStringFields(String stringProperties) {
        for (int i = 0; i < stringProperties.length(); i++) {
            Character eachCharacter = stringProperties.charAt(i);
            if (Character.isDigit(eachCharacter)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateSalary(Double salary) {
        return salary > 0.0;
    }
}
