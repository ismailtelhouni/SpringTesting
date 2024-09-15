package org.testing.custumer.custumerservice.services;

import org.testing.custumer.custumerservice.dto.CustomerDto;
import org.testing.custumer.custumerservice.exception.CustomerNotFoundException;
import org.testing.custumer.custumerservice.exception.EmailAlreadyExistException;

import java.util.List;

public interface CustomerService {

    CustomerDto saveNewCustomer(CustomerDto customerDTO) throws EmailAlreadyExistException;
    List<CustomerDto> getAllCustomers();
    CustomerDto findCustomerById(Long id) throws CustomerNotFoundException;
    List<CustomerDto> searchCustomers(String keyword);
    CustomerDto updateCustomer(Long id, CustomerDto customerDTO)throws CustomerNotFoundException;
    void deleteCustomer(Long id)throws CustomerNotFoundException;

}
