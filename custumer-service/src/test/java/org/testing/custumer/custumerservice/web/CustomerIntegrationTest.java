package org.testing.custumer.custumerservice.web;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testing.custumer.custumerservice.dto.CustomerDto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class CustomerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    List<CustomerDto> customers;

    @BeforeEach
    public void setUp() {
        this.customers = List.of(
            CustomerDto.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build(),
            CustomerDto.builder().id(2L).firstName("Mohamed").lastName("youssfi").email("mohamed@gmail.com").build(),
            CustomerDto.builder().id(3L).firstName("Yassin").lastName("ech").email("yassin@gmail.com").build()
        );
    }

    @Test
    public void shouldGetAllCustomers() {

        ResponseEntity<CustomerDto[]> response = testRestTemplate.exchange("/api/customers" , HttpMethod.GET , null , CustomerDto[].class);
        List<CustomerDto> list = Arrays.asList(Objects.requireNonNull(response.getBody()));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(list.size()).isEqualTo(3);
        assertThat(list).usingRecursiveComparison().isEqualTo(customers);

    }

    @Test
    public void shouldSearchCustomersByFirstName() {

        String keyword = "a";
        ResponseEntity<CustomerDto[]> response = testRestTemplate.exchange("/api/customers/search?keyword=" + keyword, HttpMethod.GET , null , CustomerDto[].class);
        List<CustomerDto> list = Arrays.asList(Objects.requireNonNull(response.getBody()));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(list.size()).isEqualTo(3);
        List<CustomerDto> expected = customers.stream().filter(c -> c.getFirstName().toLowerCase().contains(keyword.toLowerCase())).toList();
        assertThat(list).usingRecursiveComparison().isEqualTo(expected);

    }


    @Test
    public void shouldGetCustomerById(){

        long id = 1L;
        ResponseEntity<CustomerDto> response = testRestTemplate.exchange("/api/customers/" + id, HttpMethod.GET , null , CustomerDto.class);
        CustomerDto customerDto = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(customerDto).isNotNull();
        assertThat(customerDto).usingRecursiveComparison().isEqualTo(customers.getFirst());

    }

    @Test
    public void shouldNotFoundCustomerById(){

        long id = 8L;
        ResponseEntity<CustomerDto> response = testRestTemplate.exchange("/api/customers/" + id, HttpMethod.GET , null , CustomerDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }
}