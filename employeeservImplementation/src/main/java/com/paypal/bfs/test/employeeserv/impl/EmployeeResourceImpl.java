package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Address;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.domains.AddressDTO;
import com.paypal.bfs.test.employeeserv.domains.EmployeeDTO;
import com.paypal.bfs.test.employeeserv.exceptions.EmployeeNotFoundException;
import com.paypal.bfs.test.employeeserv.exceptions.EmployeeServiceException;
import com.paypal.bfs.test.employeeserv.respository.EmployeeRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.ws.rs.BadRequestException;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {

	@Autowired
	EmployeeRepository employeeRepository;

	public static final String date_format = "yyyy/MM/dd";

	private static final Logger log = LoggerFactory.getLogger(EmployeeResourceImpl.class);

	@Override
	public ResponseEntity<Employee> employeeGetById(String id) {

		if (StringUtils.isEmpty(id)) {
			throw new BadRequestException("employee id can not be null or empty");
		}

		Optional<EmployeeDTO> employeeDto = null;

		try {
			employeeDto = employeeRepository.findById(Long.parseLong(id));
		} catch (Exception e) {
			log.error("error while fetching employee details from data base :" + e.getMessage());
			throw new EmployeeServiceException("error while creating employee");

		}

		EmployeeDTO emp = null;

		try {
			emp = employeeDto.get();
		} catch (Exception e) {
			log.error("finding employee id resulted in error :" + e.getMessage());
			throw new EmployeeNotFoundException(Long.parseLong(id));
		}

		Employee employee = getEmployeeInfo(emp);

		Address address = getAddressInfo(emp);

		employee.setAddress(address);

		return new ResponseEntity<>(employee, HttpStatus.OK);
	}

	private Address getAddressInfo(EmployeeDTO emp) {
		AddressDTO addressDTO = emp.getAddress();

		Address address = new Address();
		address.setAddress1(addressDTO.getAddress1());
		address.setAddress2(addressDTO.getAddress2());
		address.setCity(addressDTO.getCity());
		address.setCountry(addressDTO.getCountry());
		address.setState(addressDTO.getState());
		address.setZipcode(addressDTO.getZipCode());
		address.setAddressId(addressDTO.getAddressId().intValue());

		return address;

	}

	private Employee getEmployeeInfo(EmployeeDTO emp) {
		Employee employee = new Employee();
		employee.setId(emp.getEmployeeId().intValue());
		employee.setFirstName(emp.getFirstName());
		employee.setLastName(emp.getLastName());
		employee.setDateBirth(String.valueOf(emp.getDateOfBirth()));
		return employee;
	}

	@Override
	public ResponseEntity<String> createEmployee(@RequestBody Employee emp) {

		validateEmployeeRequest(emp);

		EmployeeDTO empDTO = null;
		try {
			empDTO = createEmployeeDTO(emp);
		} catch (Exception e) {
			log.error("error while preparing employee dto :" + e.getMessage());
			throw new EmployeeServiceException("error while creating employee dto");
		}

		AddressDTO addressDTO = createAddressDTO(emp);

		addressDTO.setEmployee(empDTO);

		empDTO.setAddress(addressDTO);

		try {
			employeeRepository.save(empDTO);
		} catch (Exception e) {
			log.error("error while creating employee :" + e.getMessage());
			throw new EmployeeServiceException("error while creating employee");
		}

		return new ResponseEntity<>("employee record created", HttpStatus.CREATED);

	}

	private void validateEmployeeRequest(Employee emp) {

		if (StringUtils.isEmpty(emp.getFirstName())) {
			throw new BadRequestException("first name can not be null or empty");
		}

		if (StringUtils.isEmpty(emp.getLastName())) {
			throw new BadRequestException("last name can not be null or empty");
		}

		if (StringUtils.isEmpty(emp.getDateBirth())) {
			throw new BadRequestException("date of birth  can not be null or empty");
		}

		validateAddress(emp);

	}

	private void validateAddress(Employee emp) {
		Address address = emp.getAddress();

		if (address == null) {
			throw new BadRequestException("address can not be null");
		}

		if (StringUtils.isEmpty(address.getAddress1())) {
			throw new BadRequestException("address line1 can not be null or empty");
		}

		if (StringUtils.isEmpty(address.getCity())) {
			throw new BadRequestException("city can not be null or empty");
		}

		if (StringUtils.isEmpty(address.getState())) {
			throw new BadRequestException("state can not be null or empty");
		}

		if (StringUtils.isEmpty(address.getCountry())) {
			throw new BadRequestException("country can not be null or empty");
		}
		if (StringUtils.isEmpty(address.getZipcode())) {
			throw new BadRequestException("zipcode can not be null or empty");
		}
	}

	private AddressDTO createAddressDTO(Employee emp) {
		Address address = emp.getAddress();

		String add1 = address.getAddress1();
		String add2 = address.getAddress2();
		String city = address.getCity();
		String state = address.getState();
		String country = address.getCountry();
		String zipcode = address.getZipcode();

		AddressDTO addressDTO = new AddressDTO(add1, add2, city, state, country, zipcode);

		if (emp.getAddress().getAddressId() != null) {
			addressDTO.setAddressId(Long.valueOf(emp.getAddress().getAddressId()));
		}
		return addressDTO;
	}

	private EmployeeDTO createEmployeeDTO(Employee emp) throws ParseException {

		String fristName = emp.getFirstName();
		String lastName = emp.getLastName();

		String dob = emp.getDateBirth();
		Date date = new SimpleDateFormat(date_format).parse(dob);

		EmployeeDTO empDTO = new EmployeeDTO(fristName, lastName, date);

		if (emp.getId() != null) {
			empDTO.setEmployeeId(Long.valueOf(emp.getId()));
		}

		return empDTO;
	}
}
