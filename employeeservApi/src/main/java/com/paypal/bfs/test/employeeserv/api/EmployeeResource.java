package com.paypal.bfs.test.employeeserv.api;

import com.paypal.bfs.test.employeeserv.api.model.Employee;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Interface for employee resource operations.
 */
public interface EmployeeResource {

	/**
	 * Retrieves the {@link Employee} resource by id.
	 *
	 * @param id employee id.
	 * @return {@link Employee} resource.
	 */
	@GetMapping("/v1/bfs/employees/{id}")
	ResponseEntity<Employee> employeeGetById(@PathVariable("id") String id);

	@PostMapping("/v1/bfs/employees")
	ResponseEntity<String> createEmployee(Employee emp) throws ParseException;
}
