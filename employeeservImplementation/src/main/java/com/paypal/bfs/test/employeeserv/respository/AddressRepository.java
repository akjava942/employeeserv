package com.paypal.bfs.test.employeeserv.respository;
import org.springframework.data.repository.CrudRepository;

import com.paypal.bfs.test.employeeserv.domains.AddressDTO;

public interface AddressRepository extends CrudRepository<AddressDTO, Long>  {

}
