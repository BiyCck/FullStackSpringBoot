package com.cicek;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
@RestController
public class Main {

    // fake db
    private static List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(
                1,
                "Alex",
                "alex@gmail.com",
                21
        );
        customers.add(alex);

        Customer jamila = new Customer(
                2,
                "Jamila",
                "jamila@gmail.com",
                21
        );
        customers.add(jamila);
    }

    public static void main(String[] args) {
        System.out.println(customers);
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("api/v1/customers")
    public List<Customer> getCustomers(){
        return customers;
    }

    @GetMapping("api/v1/customers/{id}")
    public Customer getCustomer(@PathVariable("id") Integer customerId) {
        return customers.stream().filter(customer -> customer.id.equals(customerId))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("customer with id [%s] not found".formatted(customerId))
                );
    }

    static class Customer {
        private Integer id;
        private String name;
        private String email;
        private Integer age;

        public Customer() {
        }

        public Customer(Integer id, String name, String email, Integer age) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.age = age;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Customer customer = (Customer) o;
            return Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(email, customer.email) && Objects.equals(age, customer.age);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, email, age);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Customer{");
            sb.append("id=").append(id);
            sb.append(", name='").append(name).append('\'');
            sb.append(", email='").append(email).append('\'');
            sb.append(", age=").append(age);
            sb.append('}');
            return sb.toString();
        }
    }
}
