# Accessing Data with JPA
本节指引你使用`Spring Data JPA`来从关系型数据库中保存和获取数据。

通过构建一个应用将`Customer` POJOs保存到基于内存的数据库中。

## 指引

首先在本地数据库创建spring-guides数据库以及customer表。
```sql
create table `customer`(
    `id`varchar(32) not null, 
    `first_name` varchar(128) not null,
    `last_name` varchar(128) not null, 
    primary key (`id`)
) comment 'customer';
```

### 创建一个Customer类
类中有三个属性：`id`, `firstName`, `lastName`；两个构造器。
```java
package com.weilian.adwj.service;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


// @Entity将其注册为数据库表
@Entity
@Data
public class Customer {

    // @Id表示数据库主键
    @Id
    private String id;

    private String firstName;

    private String lastName;

    public Customer(String id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    protected Customer(){

    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
```
说明：

Here you have a `Customer` class with three attributes: `id`, `firstName`, and `lastName`. 
You also have two constructors. The default constructor exists only for the sake of JPA. You do not use it directly, 
so it is designated as `protected`. The other constructor is the one you use to create instances of Customer to be saved to the database.

The `Customer` class is annotated with `@Entity`, indicating that it is a JPA entity. (Because no `@Table` annotation exists, it is assumed that this entity is mapped to a table named `Customer`.)

The `Customer` object’s `id` property is annotated with `@Id` so that JPA recognizes it as the object’s ID. The id property is also annotated with `@GeneratedValue` to indicate that the ID should be generated automatically.

The other two properties, `firstName` and `lastName`, are left unannotated. It is assumed that they are mapped to columns that share the same names as the properties themselves.

The convenient `toString()` method print outs the customer’s properties.

### 创建简单查询接口
CustomerRepository查询接口继承CrudRepository类。

Spring Data JPA focuses on using JPA to store data in a relational database. Its most compelling feature is the ability to create repository implementations automatically, at runtime, from a repository interface.

CustomerRepository extends the CrudRepository interface. The type of entity and ID that it works with, Customer and Long, are specified in the generic parameters on CrudRepository. By extending CrudRepository, CustomerRepository inherits several methods for working with Customer persistence, including methods for saving, deleting, and finding Customer entities.

Spring Data JPA also lets you define other query methods by declaring their method signature. For example, CustomerRepository includes the findByLastName() method.

In a typical Java application, you might expect to write a class that implements CustomerRepository. However, that is what makes Spring Data JPA so powerful: You need not write an implementation of the repository interface. Spring Data JPA creates an implementation when you run the application.

Now you can wire up this example and see what it looks like!
```java
package com.weilian.adwj.jparepository;

import com.weilian.adwj.service.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    Customer findById(long id);

}

```

### 创建主函数
The main() method uses Spring Boot’s SpringApplication.run() method to launch an application. Did you notice that there was not a single line of XML? There is no web.xml file, either. This web application is 100% pure Java and you did not have to deal with configuring any plumbing or infrastructure.

Now you need to modify the simple class that the Initializr created for you. To get output (to the console, in this example), you need to set up a logger. Then you need to set up some data and use it to generate output. The following listing shows the finished

```java
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
            repository.findByLastName("xiao5").forEach(bauer -> {
                log.info(bauer.toString());
            });
            // for (Customer bauer : repository.findByLastName("Bauer")) {
            //  log.info(bauer.toString());
            // }
            log.info("");
        };
    }

}
```

### 执行main函数