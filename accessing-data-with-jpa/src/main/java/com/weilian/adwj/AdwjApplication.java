package com.weilian.adwj;

import com.weilian.adwj.jparepository.CustomerRepository;
import com.weilian.adwj.service.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AdwjApplication {

    private static final Logger log = LoggerFactory.getLogger(AdwjApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AdwjApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(CustomerRepository repository) {
        return (args) -> {
            // save a few customers
            repository.save(new Customer("1", "Bauer", "xiao1"));
            repository.save(new Customer("2", "O'Brian", "xiao2"));
            repository.save(new Customer("3", "Bauer", "xiao3"));
            repository.save(new Customer("4", "Palmer", "xiao4"));
            repository.save(new Customer("5", "Dessler", "xiao5"));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Customer customer : repository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            Customer customer = repository.findById("1");
            log.info("Customer found with findById(1L):");
            log.info("--------------------------------");
            log.info(customer.toString());
            log.info("");

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repository.findByLastName("Bauer").forEach(bauer -> {
                log.info(bauer.toString());
            });
            // for (Customer bauer : repository.findByLastName("Bauer")) {
            //  log.info(bauer.toString());
            // }
            log.info("");
        };
    }

}
