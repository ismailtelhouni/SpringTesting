package org.testing.custumer.custumerservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testing.custumer.custumerservice.entities.Customer;
import org.testing.custumer.custumerservice.repository.CustomerRepository;

@SpringBootApplication
public class CustumerServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(CustumerServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CustumerServiceApplication.class, args);
    }

    @Bean
    @Profile("!test")
    CommandLineRunner init(CustomerRepository customerRepository) {
        return args -> {
            logger.info("Starting CustomerServiceApplication");
            logger.info("--------------------init-----------------------");
            customerRepository.save(Customer.builder()
                .firstName("ismail")
                .lastName("telhouni")
                .email("ismail@gmail.com")
                .build());
            customerRepository.save(Customer.builder()
                .firstName("Mohamed")
                .lastName("youssfi")
                .email("mohamed@gmail.com")
                .build());
            customerRepository.save(Customer.builder()
                .firstName("Yassin")
                .lastName("ech")
                .email("yassin@gmail.com")
                .build());
        };
    }

}
