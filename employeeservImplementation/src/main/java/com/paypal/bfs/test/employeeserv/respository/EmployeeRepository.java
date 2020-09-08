package com.paypal.bfs.test.employeeserv.respository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paypal.bfs.test.employeeserv.domains.EmployeeDTO;


@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeDTO, Long> {

}
