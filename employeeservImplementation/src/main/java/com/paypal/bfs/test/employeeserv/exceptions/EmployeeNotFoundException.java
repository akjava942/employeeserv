package com.paypal.bfs.test.employeeserv.exceptions;

public class EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException(Long id) {
		super("Employee id not found : " + id);
	}

}
