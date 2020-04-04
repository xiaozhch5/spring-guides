# Accessing relational data using JDBC with Spring
本节指引你通过使用Spring来操作关系型数据。

通过本节学习，你可以使用`Spring JdbcTemplate`来操作存储在关系型数据库中的数据。

# 指引
首先创建一个Customer.java文件，映射数据库中的表
```java
package com.weilian.ardujws.dao;

import lombok.Data;

@Data
public class Customer {

    private String id;

    private String firstName, secondName;

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}
```
修改ArdujwsApplication.java文件，用于操作数据库。

Spring provides a template class called JdbcTemplate that makes it easy to work with SQL relational databases and JDBC. Most JDBC code is mired in resource acquisition, connection management, exception handling, and general error checking that is wholly unrelated to what the code is meant to achieve. The JdbcTemplate takes care of all of that for you. All you have to do is focus on the task at hand. 
```java
package com.weilian.ardujws;

import com.weilian.ardujws.dao.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class ArdujwsApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ArdujwsApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(ArdujwsApplication.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE IF EXISTS customers");
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

        // Split up the array of whole names into an array of first/last names
        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        // Use a Java 8 stream to print out each tuple of the list
        splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

        log.info("Querying for customer records where first_name = 'Josh':");
        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> log.info(customer.toString()));
    }
}
```

@SpringBootApplication is a convenience annotation that adds all of the following:

@Configuration: Tags the class as a source of bean definitions for the application context.

@EnableAutoConfiguration: Tells Spring Boot to start adding beans, based on classpath settings, other beans, and various property settings.

@ComponentScan: Tells Spring to look for other components, configurations, and services in the com.example.relationaldataaccess package. In this case, there are none.

The main() method uses Spring Boot’s SpringApplication.run() method to launch an application.

Spring Boot supports H2 (an in-memory relational database engine) and automatically creates a connection. Because we use spring-jdbc, Spring Boot automatically creates a JdbcTemplate. The @Autowired JdbcTemplate field automatically loads it and makes it available.

This Application class implements Spring Boot’s CommandLineRunner, which means it will execute the run() method after the application context is loaded.

First, install some DDL by using the execute method of JdbcTemplate.

Second, take a list of strings and, by using Java 8 streams, split them into firstname/lastname pairs in a Java array.

Then install some records in your newly created table by using the batchUpdate method of JdbcTemplate. The first argument to the method call is the query string. The last argument (the array of Object instances) holds the variables to be substituted into the query where the ? characters are.


For single insert statements, the insert method of JdbcTemplate is good. However, for multiple inserts, it is better to use batchUpdate.

Use ? for arguments to avoid SQL injection attacks by instructing JDBC to bind variables.

Finally, use the query method to search your table for records that match the criteria. You again use the ? arguments to create parameters for the query, passing in the actual values when you make the call. The last argument is a Java 8 lambda that is used to convert each result row into a new Customer object.


Java 8 lambdas map nicely onto single method interfaces, such as Spring’s RowMapper. If you use Java 7 or earlier, you can plug in an anonymous interface implementation and have the method body be the same as the lambda expression’s body.

# 运行
直接执行ArdujwsApplication.java