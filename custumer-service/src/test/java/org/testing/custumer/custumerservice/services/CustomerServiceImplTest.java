package org.testing.custumer.custumerservice.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testing.custumer.custumerservice.dto.CustomerDto;
import org.testing.custumer.custumerservice.entities.Customer;
import org.testing.custumer.custumerservice.mapper.CustomerMapper;
import org.testing.custumer.custumerservice.repository.CustomerRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl underTest;

    @Test
    void shouldSaveNewCustomer(){

        Customer customer = Customer.builder().firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        CustomerDto customerDto = CustomerDto.builder().firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        Customer savedCustomer = Customer.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        CustomerDto expected = CustomerDto.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();

        Mockito.when(customerRepository.findByEmail(customerDto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(customerMapper.fromCustomerDto(customerDto)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(savedCustomer);
        Mockito.when(customerMapper.fromCustomer(savedCustomer)).thenReturn(expected);

        CustomerDto result = underTest.saveNewCustomer(customerDto);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }

}