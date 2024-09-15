package org.testing.custumer.custumerservice;

import org.springframework.boot.SpringApplication;

public class TestCustumerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(CustumerServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
