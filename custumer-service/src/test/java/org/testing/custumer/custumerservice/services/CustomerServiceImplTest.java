package org.testing.custumer.custumerservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testing.custumer.custumerservice.mapper.CustomerMapper;
import org.testing.custumer.custumerservice.repository.CustomerRepository;

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



    }

}