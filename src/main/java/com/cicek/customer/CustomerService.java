package com.cicek.customer;

import com.cicek.exception.DuplicateResourceException;
import com.cicek.exception.RequestValidationException;
import com.cicek.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDao.selectCustomerById(id).orElseThrow(
                () -> new ResourceNotFoundException("customer with id [%s] not found".formatted(id))
        );
    }

    public void addCustomer(
        CustomerRegistrationRequest customerRegistrationRequest
    ) {
        if (customerDao.existsCustomerWithEmail(customerRegistrationRequest.email())){
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }

        customerDao.insertCustomer(
                new Customer(
                        customerRegistrationRequest.name(),
                        customerRegistrationRequest.email(),
                        customerRegistrationRequest.age()
                )
        );
    }

    public void deleteCustomerById(Integer id){
        if (!customerDao.existsCustomerWithId(id)) {
            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Integer customerId, CustomerUpdateRequest updateRequest){

        Customer customer = getCustomer(customerId);
        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().isBlank() && !customer.getName().equals(updateRequest.name())){
            customer.setName(updateRequest.name());
            changes = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().isBlank() && !customer.getEmail().equals(updateRequest.email())){
            customer.setEmail(updateRequest.email());
            changes = true;
        }

        if (updateRequest.age() != null && !customer.getAge().equals(updateRequest.age())){
            customer.setAge(updateRequest.age());
            changes = true;
        }

        if (!changes){
            throw new RequestValidationException("No changed data available for update");
        } else if (customerDao.existsCustomerWithEmail(updateRequest.email())){
            throw new DuplicateResourceException(
                    "email already taken"
            );
        } else{
            customerDao.updateCustomer(customer);
        }
    }
}
