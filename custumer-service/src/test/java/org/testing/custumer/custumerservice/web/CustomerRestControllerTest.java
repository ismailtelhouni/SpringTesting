package org.testing.custumer.custumerservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testing.custumer.custumerservice.dto.CustomerDto;
import org.testing.custumer.custumerservice.entities.Customer;
import org.testing.custumer.custumerservice.exception.CustomerNotFoundException;
import org.testing.custumer.custumerservice.exception.EmailAlreadyExistException;
import org.testing.custumer.custumerservice.services.CustomerService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@WebMvcTest(CustomerRestController.class)
class CustomerRestControllerTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    List<CustomerDto> customers;

    @BeforeEach
    void setUp() {
        this.customers = List.of(
            CustomerDto.builder().id(1L).firstName("ismail").lastName("telhouni").email("ismail@gmail.com").build(),
            CustomerDto.builder().id(2L).firstName("Mohamed").lastName("youssfi").email("mohamed@gmail.com").build(),
            CustomerDto.builder().id(3L).firstName("Yassin").lastName("ech").email("yassin@gmail.com").build()
        );
    }

    @Test
    public void shouldGetAllCustomers() throws Exception {

        Mockito.when(customerService.getAllCustomers()).thenReturn(customers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(this.customers)));

    }

    @Test
    public void shouldGetCustomerById() throws Exception {

        Long id = 1L;
        Mockito.when(customerService.findCustomerById(id)).thenReturn(customers.getFirst());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/" + id))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(this.customers.getFirst())));

    }

    @Test
    public void shouldNotGetCustomerByInvalidId() throws Exception {

        Long id = -1L;
        Mockito.when(customerService.findCustomerById(id)).thenThrow(CustomerNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/" + id))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().string(""));

    }

    @Test
    public void shouldSearchCustomers() throws Exception {

        String keyword = "a";
        Mockito.when(customerService.searchCustomers(keyword)).thenReturn(customers);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/search?keyword=" + keyword))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(3)))
            .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(this.customers)));

    }

    @Test
    public void shouldSaveCustomer() throws Exception {

        CustomerDto customerDto= customers.getFirst();
        String expected = """
            {
                "id":1, "firstName":"ismail", "lastName":"telhouni", "email":"ismail@gmail.com"
            }
            """;

        Mockito.when(customerService.saveNewCustomer(Mockito.any())).thenReturn(customerDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(customerDto))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().json(expected));

    }

    @Test
    public void shouldNotSaveCustomerWhenEmailExist() throws Exception {

        CustomerDto customerDto= customers.getFirst();
        Mockito.when(customerService.saveNewCustomer(Mockito.any())).thenThrow(EmailAlreadyExistException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(customerDto))
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(""));

    }


}