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
import org.testing.custumer.custumerservice.exception.CustomerNotFoundException;
import org.testing.custumer.custumerservice.exception.EmailAlreadyExistException;
import org.testing.custumer.custumerservice.mapper.CustomerMapper;
import org.testing.custumer.custumerservice.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

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

    @Test
    void shouldNotSaveNewCustomerWhenEmailExist(){

        Customer customer = Customer.builder().id(5L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        CustomerDto customerDto = CustomerDto.builder().firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();

        Mockito.when(customerRepository.findByEmail(customerDto.getEmail())).thenReturn(Optional.of(customer));
        assertThatThrownBy(()->underTest.saveNewCustomer(customerDto)).isInstanceOf(EmailAlreadyExistException.class);
    }


    @Test
    void shouldSaveNewCustomer2(){

        Customer customer = Customer.builder().firstName("yassin").lastName("ech-chykry").email("ismail@gmail.com").build();
        CustomerDto customerDto = CustomerDto.builder().firstName("yassin").lastName("ech-chykry").email("ismail@gmail.com").build();
        Customer savedCustomer = Customer.builder().id(1L).firstName("yassin").lastName("ech-chykry").email("ismail@gmail.com").build();
        CustomerDto expected = CustomerDto.builder().id(1L).firstName("yassin").lastName("ech-chykry").email("ismail@gmail.com").build();

        Mockito.when(customerRepository.findByEmail(customerDto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(customerMapper.fromCustomerDto(customerDto)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(savedCustomer);
        Mockito.when(customerMapper.fromCustomer(savedCustomer)).thenReturn(expected);

        CustomerDto result = underTest.saveNewCustomer2(customerDto);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    void shouldNotSaveNewCustomerWhenEmailExist2(){

        Customer customer = Customer.builder().id(5L).firstName("yassin").lastName("ech-chykry").email("ismail@gmail.com").build();
        CustomerDto customerDto = CustomerDto.builder().firstName("yassin").lastName("ech-chykry").email("ismail@gmail.com").build();

        Mockito.when(customerRepository.findByEmail(customerDto.getEmail())).thenReturn(Optional.of(customer));
        assertThatThrownBy(()->underTest.saveNewCustomer2(customerDto)).isInstanceOf(EmailAlreadyExistException.class);
    }

    @Test
    void shouldGetAllCustomers(){

        List<Customer> customers = List.of(
                Customer.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build(),
                Customer.builder().id(2L).firstName("Mohamed").lastName("youssfi").email("mohamed@gmail.com").build(),
                Customer.builder().id(3L).firstName("Yassin").lastName("ech").email("yassin@gmail.com").build()
        );
        List<CustomerDto> expected = List.of(
                CustomerDto.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build(),
                CustomerDto.builder().id(2L).firstName("Mohamed").lastName("youssfi").email("mohamed@gmail.com").build(),
                CustomerDto.builder().id(3L).firstName("Yassin").lastName("ech").email("yassin@gmail.com").build()
        );

        Mockito.when(customerRepository.findAll()).thenReturn(customers);
        Mockito.when(customerMapper.fromCustomers(customers)).thenReturn(expected);

        List<CustomerDto> result = underTest.getAllCustomers();
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    void shouldFindCustomerById(){

        Long customerId = 1L;
        Customer customer = Customer.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        CustomerDto customerDto = CustomerDto.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customerMapper.fromCustomer(customer)).thenReturn(customerDto);

        CustomerDto result = underTest.findCustomerById(customerId);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(customerDto);

    }

    @Test
    void shouldNotFindCustomerById(){

        Long customerId = 8L;

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> underTest.findCustomerById(customerId)).isInstanceOf(CustomerNotFoundException.class).hasMessage("Customer not found");

    }

    @Test
    void shouldFindCustomerById2(){

        Long customerId = 1L;
        Customer customer = Customer.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        CustomerDto customerDto = CustomerDto.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        Mockito.when(customerMapper.fromCustomer(customer)).thenReturn(customerDto);

        CustomerDto result = underTest.findCustomerById2(customerId);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(customerDto);

    }

    @Test
    void shouldNotFindCustomerById2(){

        Long customerId = 8L;

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(()-> underTest.findCustomerById2(customerId)).isInstanceOf(CustomerNotFoundException.class).hasMessage(null);

    }

    @Test
    void shouldSearchCustomers(){

        String keyword = "a";
        List<Customer> customers = List.of(
                Customer.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build(),
                Customer.builder().id(2L).firstName("Mohamed").lastName("youssfi").email("mohamed@gmail.com").build(),
                Customer.builder().id(3L).firstName("Yassin").lastName("ech").email("yassin@gmail.com").build()
        );
        List<CustomerDto> expected = List.of(
                CustomerDto.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build(),
                CustomerDto.builder().id(2L).firstName("Mohamed").lastName("youssfi").email("mohamed@gmail.com").build(),
                CustomerDto.builder().id(3L).firstName("Yassin").lastName("ech").email("yassin@gmail.com").build()
        );

        Mockito.when(customerRepository.findByFirstNameContainsIgnoreCase(keyword)).thenReturn(customers);
        Mockito.when(customerMapper.fromCustomers(customers)).thenReturn(expected);

        List<CustomerDto> result = underTest.searchCustomers(keyword);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    void shouldUpdateCustomer(){
        Long customerId = 6L;

        Customer customerOld = Customer.builder().id(6L).firstName("isma").lastName("telhou").email("ismail@gmail.com").build();

        CustomerDto customerDto = CustomerDto.builder().id(6L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        Customer customer = Customer.builder().id(6L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();

        Customer updatedCustomer = Customer.builder().id(6L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        CustomerDto expected = CustomerDto.builder().id(6L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerOld));
        Mockito.when(customerMapper.fromCustomerDto(customerDto)).thenReturn(customer);
        Mockito.when(customerRepository.save(customer)).thenReturn(updatedCustomer);
        Mockito.when(customerMapper.fromCustomer(updatedCustomer)).thenReturn(expected);

        CustomerDto result = underTest.updateCustomer(customerId, customerDto);
        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    void shouldUpdateCustomerNotFoundCustomer(){

        Long customerId = 8L;
        CustomerDto customerDto = CustomerDto.builder().id(6L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(()->underTest.updateCustomer(customerId , customerDto)).isInstanceOf(CustomerNotFoundException.class).hasMessage("Customer not found");

    }

    @Test
    void shouldDeleteCustomer(){

        Long customerId = 1L;
        Customer customer = Customer.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        underTest.deleteCustomer(customerId);
        Mockito.verify(customerRepository).deleteById(customerId);

    }

    @Test
    void shouldDeleteCustomerNotFoundCustomer(){
        Long customerId = 8L;

        Mockito.when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(()->underTest.deleteCustomer(customerId )).isInstanceOf(CustomerNotFoundException.class).hasMessage("Customer not found");
    }



}