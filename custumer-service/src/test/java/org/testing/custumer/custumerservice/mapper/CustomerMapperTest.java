package org.testing.custumer.custumerservice.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.testing.custumer.custumerservice.dto.CustomerDto;
import org.testing.custumer.custumerservice.entities.Customer;

import java.util.List;

class CustomerMapperTest {

    CustomerMapper underTest = new CustomerMapper();

    @Test
    public void shouldMapCustomerToCustomerDto() {

        Customer givenCustomer = Customer.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        CustomerDto expected = CustomerDto.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        CustomerDto result = underTest.fromCustomer(givenCustomer);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    public void shouldMapCustomerDtoToCustomer() {

        Customer expected = Customer.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        CustomerDto givenCustomerDto = CustomerDto.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build();
        Customer result = underTest.fromCustomerDto(givenCustomerDto);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }

    @Test
    public void shouldMapListOfCustomersToListOfCustomersDto() {

        List<Customer> givenCustomers = List.of(
                Customer.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build(),
                Customer.builder().id(2L).firstName("Mohamed").lastName("youssfi").email("mohamed@gmail.com").build(),
                Customer.builder().id(3L).firstName("Yassin").lastName("ech").email("yassin@gmail.com").build()
        );
        List<CustomerDto> expectedList = List.of(
                CustomerDto.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build(),
                CustomerDto.builder().id(2L).firstName("Mohamed").lastName("youssfi").email("mohamed@gmail.com").build(),
                CustomerDto.builder().id(3L).firstName("Yassin").lastName("ech").email("yassin@gmail.com").build()
        );
        List<CustomerDto> result = underTest.fromCustomers(givenCustomers);

        assertThat(result).isNotNull();
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedList);

    }

    @Test
    public void shouldNotMapNullCustomerToCustomerDto() {
        assertThatThrownBy(()->underTest.fromCustomer(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldNotMapNullCustomerDtoToCustomer() {
        assertThatThrownBy(()->underTest.fromCustomerDto(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldNotMapNullListOfCustomersToListOfCustomersDto() {
        assertThatThrownBy(()->underTest.fromCustomers(null)).isInstanceOf(NullPointerException.class);
    }



}