package com.example.demo.controllers;

import com.example.demo.models.Customer;
import com.example.demo.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return ResponseEntity.ok(customers);
    }

    @PostMapping
    public ResponseEntity<String> saveCustomer(@Valid @RequestBody Customer customer) {

        if (customer.getId() != null) {
            customer.setId(null);
        }

        if (customerRepository.findByEmail(customer.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Customer with email " + customer.getEmail() + " already exists");
        }

        // Ticket Purchase Handle by another Controller
//        if (customer.getPurchasedtickets() != null) {
//            customer.getPurchasedtickets().forEach(ticket -> ticket.setCustomer(customer));
//        }

        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Customer saved successfully");
    }

    @PutMapping
    public ResponseEntity<String> updateCustomer(@Valid @RequestBody Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.findById(customer.getId());
        if (existingCustomer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer with id " + customer.getId() + " not found");
        }

        Customer updatedCustomer = existingCustomer.get();
        updatedCustomer.setName(customer.getName());
        updatedCustomer.setEmail(customer.getEmail());

        // Ticket Purchase Handle by another Controller
//        if (customer.getPurchasedtickets() != null) {
//            updatedCustomer.getPurchasedtickets().clear();
//            customer.getPurchasedtickets().forEach(ticket -> ticket.setCustomer(updatedCustomer));
//            updatedCustomer.getPurchasedtickets().addAll(customer.getPurchasedtickets());
//        }

        customerRepository.save(updatedCustomer);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Customer updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }

        customerRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
