package org.testing.custumer.custumerservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testing.custumer.custumerservice.dto.CustomerDto;
import org.testing.custumer.custumerservice.entities.Customer;
import org.testing.custumer.custumerservice.exception.CustomerNotFoundException;
import org.testing.custumer.custumerservice.exception.EmailAlreadyExistException;
import org.testing.custumer.custumerservice.mapper.CustomerMapper;
import org.testing.custumer.custumerservice.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customerDTO) throws EmailAlreadyExistException {
        log.info(String.format("Saving new Customer => %s ", customerDTO.toString()));
        Optional<Customer> byEmail = customerRepository.findByEmail(customerDTO.getEmail());
        if(byEmail.isPresent()) {
            log.error(String.format("This email %s already exist", customerDTO.getEmail()));
            throw new EmailAlreadyExistException();
        }
        Customer customerToSave = customerMapper.fromCustomerDto(customerDTO);
        Customer savedCustomer = customerRepository.save(customerToSave);
        return customerMapper.fromCustomer(savedCustomer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> allCustomers = customerRepository.findAll();
        return customerMapper.fromCustomers(allCustomers);
    }

    @Override
    public CustomerDto findCustomerById(Long id) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) throw new CustomerNotFoundException();
        return customerMapper.fromCustomer(customer.get());
    }

    @Override
    public List<CustomerDto> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        return customerMapper.fromCustomers(customers);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDTO) throws CustomerNotFoundException {
        Optional<Customer> customer=customerRepository.findById(id);
        if(customer.isEmpty()) throw new CustomerNotFoundException();
        customerDTO.setId(id);
        Customer customerToUpdate = customerMapper.fromCustomerDto(customerDTO);
        Customer updatedCustomer = customerRepository.save(customerToUpdate);
        return customerMapper.fromCustomer(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        Optional<Customer> customer=customerRepository.findById(id);
        if(customer.isEmpty()) throw new CustomerNotFoundException();
        customerRepository.deleteById(id);
    }
}
