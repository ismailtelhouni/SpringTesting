package org.testing.custumer.custumerservice.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.testing.custumer.custumerservice.dto.CustomerDto;
import org.testing.custumer.custumerservice.entities.Customer;

import java.util.List;

@Service
public class CustomerMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public Customer fromCustomerDto(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }

    public CustomerDto fromCustomer(Customer customer) {
        return modelMapper.map(customer, CustomerDto.class);
    }

    public List<CustomerDto> fromCustomers(List<Customer> customers) {
        return customers.stream().map(c-> modelMapper.map(c,CustomerDto.class)).toList();
    }



}
