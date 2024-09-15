package org.testing.custumer.custumerservice.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testing.custumer.custumerservice.entities.Customer;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        System.out.println("--------------------------------------");
        customerRepository.save(Customer.builder().firstName("ismail")
                .lastName("telhouni").email("ismail@gmail.com").build());
        customerRepository.save(Customer.builder()
                .firstName("Mohamed").lastName("youssfi")
                .email("mohamed@gmail.com").build());
        customerRepository.save(Customer.builder()
                .firstName("Yassin").lastName("ech")
                .email("yassin@gmail.com").build());
        System.out.println("--------------------------------------");

    }

    @Test
    public void shouldFindCustomerByEmail(){

        String email = "ismail@gmail.com";
        Optional<Customer> result = customerRepository.findByEmail(email);
        assertThat(result).isPresent();

    }

    @Test
    public void shouldNotFindCustomerByEmail(){

        String email = "test@gmail.com";
        Optional<Customer> result = customerRepository.findByEmail(email);
        assertThat(result).isEmpty();

    }

    @Test
    public void shouldFindCustomerByFirstName(){
        String keyword = "a";
        List<Customer> expected = List.of(
            Customer.builder().firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build(),
            Customer.builder().firstName("Mohamed").lastName("youssfi").email("mohamed@gmail.com").build(),
            Customer.builder().firstName("Yassin").lastName("ech").email("yassin@gmail.com").build()
        );
        List<Customer> customers =  customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        System.out.println("customers :"+customers);
        assertThat(customers).isNotNull();
        assertThat(customers.size()).isEqualTo(expected.size());
        assertThat(customers).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);

    }

}