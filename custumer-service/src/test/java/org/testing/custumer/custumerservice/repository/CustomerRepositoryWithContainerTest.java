package org.testing.custumer.custumerservice.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testing.custumer.custumerservice.entities.Customer;

import java.util.List;
import java.util.Optional;

@Testcontainers
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryWithContainerTest {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("customer-db")
            .withUsername("ismail")
            .withPassword("1234");

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void connectionEstablishedTest() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

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
    void shouldFindCustomerByEmail(){

        String email = "ismail@gmail.com";
        Optional<Customer> result = customerRepository.findByEmail(email);
        assertThat(result).isPresent();

    }

    @Test
    void shouldNotFindCustomerByEmail(){

        String email = "test@gmail.com";
        Optional<Customer> result = customerRepository.findByEmail(email);
        assertThat(result).isEmpty();

    }

    @Test
    void shouldFindCustomerByFirstName(){
        String keyword = "a";
        List<Customer> expected = List.of(
                Customer.builder().firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build(),
                Customer.builder().firstName("Mohamed").lastName("youssfi").email("mohamed@gmail.com").build(),
                Customer.builder().firstName("Yassin").lastName("ech").email("yassin@gmail.com").build()
        );
        List<Customer> customers =  customerRepository.findByFirstNameContainsIgnoreCase(keyword);
        System.out.println("customers :"+customers);
        assertThat(customers).isNotNull();
        Assertions.assertThat(customers).hasSameSizeAs(expected);
        assertThat(customers).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);

    }

}