package org.testing.custumer.custumerservice.web;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testing.custumer.custumerservice.dto.CustomerDto;
import org.testing.custumer.custumerservice.exception.ErrorMessage;
import org.testing.custumer.custumerservice.repository.CustomerRepository;

import java.util.*;

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
    @Autowired
    private CustomerRepository customerRepository;

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

    @Test
    @Rollback
    public void shouldSaveValidCustomer(){

        CustomerDto customerDto = CustomerDto.builder().firstName("Amal").lastName("Salane").email("amal@gmail.com").build();
        ResponseEntity<CustomerDto> response = testRestTemplate.exchange("/api/customers", HttpMethod.POST , new HttpEntity<>(customerDto) , CustomerDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(customerDto);

    }

    @Test
    public void shouldNotSaveInvalidCustomer() throws JsonProcessingException {

        CustomerDto customerDto = CustomerDto.builder().firstName("").lastName("").email("").build();
        ResponseEntity<CustomerDto> response = testRestTemplate.exchange("/api/customers", HttpMethod.POST , new HttpEntity<>(customerDto) , CustomerDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        System.out.println("response.getBody() ------------- ; "+response.getBody());
        System.out.println("response.getBody() ------------- ; "+response.getStatusCode());

        CustomerDto cust = response.getBody();
        assertThat(cust).isNotNull();
        assert cust != null;
        boolean firstName = false;
        if(cust.getFirstName().equals("size must be between 2 and 2147483647")) { firstName = true; }
        else if (cust.getFirstName().equals("must not be empty")) { firstName = true; }
        assertThat(firstName).isEqualTo(true);

        boolean lastName = false;
        if(cust.getLastName().equals("size must be between 2 and 2147483647")) { lastName = true; }
        else if (cust.getLastName().equals("must not be empty")) { lastName = true; }
        assertThat(lastName).isEqualTo(true);

        boolean email = false;
        if(cust.getEmail().equals("size must be between 8 and 2147483647")) { email = true; }
        else if (cust.getEmail().equals("must not be empty")) { email = true; }
        assertThat(email).isEqualTo(true);

    }

    @Test
    @Rollback
    public void shouldUpdateValidCustomer() {

        long id = 2L;
        CustomerDto customerDto = CustomerDto.builder().firstName("Amal").lastName("Salane").email("amal@gmail.com").build();

        ResponseEntity<CustomerDto> response = testRestTemplate.exchange("/api/customers/"+id , HttpMethod.PUT , new HttpEntity<>(customerDto) , CustomerDto.class);
        CustomerDto cust = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(cust).isNotNull();
        assertThat(cust).usingRecursiveComparison().ignoringFields("id").isEqualTo(customerDto);

    }

    @Test
    public void shouldNotUpdateInvalidCustomer() {

        long id = 3L;
        CustomerDto customerDto = CustomerDto.builder().firstName("").lastName("").email("").build();
        ResponseEntity<CustomerDto> response = testRestTemplate.exchange("/api/customers/"+id , HttpMethod.PUT , new HttpEntity<>(customerDto) , CustomerDto.class);
        CustomerDto cust = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(cust).isNotNull();
        assert cust != null;

        boolean firstName = false;
        if(cust.getFirstName().equals("size must be between 2 and 2147483647")) { firstName = true; }
        else if (cust.getFirstName().equals("must not be empty")) { firstName = true; }
        assertThat(firstName).isEqualTo(true);

        boolean lastName = false;
        if(cust.getLastName().equals("size must be between 2 and 2147483647")) { lastName = true; }
        else if (cust.getLastName().equals("must not be empty")) { lastName = true; }
        assertThat(lastName).isEqualTo(true);

        boolean email = false;
        if(cust.getEmail().equals("size must be between 8 and 2147483647")) { email = true; }
        else if (cust.getEmail().equals("must not be empty")) { email = true; }
        assertThat(email).isEqualTo(true);

    }

    @Test
    public void shouldNotUpdateCustomerNotFound(){
        long id = -1L;
        CustomerDto customerDto = CustomerDto.builder().firstName("Amal").lastName("Salane").email("amal@gmail.com").build();
        ResponseEntity<ErrorMessage> response = testRestTemplate.exchange("/api/customers/"+id , HttpMethod.PUT , new HttpEntity<>(customerDto) , ErrorMessage.class);
        ErrorMessage cust = response.getBody();
        System.out.println("-------------------------------"+response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    @Rollback
    public void shouldDeleteCustomer(){

        long id = 1L;
        ResponseEntity<String> response = testRestTemplate.exchange("/api/customers/"+id , HttpMethod.DELETE , null , String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    public void shouldNotDeleteInvalidCustomer(){
        long id = -1L;
        ResponseEntity<ErrorMessage> response = testRestTemplate.exchange("/api/customers/"+id , HttpMethod.DELETE , null , ErrorMessage.class);
        ErrorMessage cust = response.getBody();

        System.out.println("-------------------------------"+response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);


    }

}