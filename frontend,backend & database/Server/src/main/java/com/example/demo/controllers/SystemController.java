package com.example.demo.controllers;

import com.example.demo.models.Configuration;
import com.example.demo.models.Customer;
import com.example.demo.models.Purchasedticket;
import com.example.demo.models.TicketPool;
import com.example.demo.repositories.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/systemctl")
@RequiredArgsConstructor
public class SystemController {

    private final ConfigurationRepository configurationRepository;
    private final CustomerRepository customerRepository;
    private final TicketPoolRepository ticketPoolRepository;
    private final PurchasedticketRepository purchasedTicketRepository;
    private final StatusRepository statusRepository;
    @Qualifier("customTaskExecutor")
    private final ThreadPoolTaskExecutor taskExecutor;

    private boolean isSystemRunning = false;
    private final List<String> logMessages = Collections.synchronizedList(new ArrayList<>());

    @GetMapping(path = "/start", produces = "application/json")
    public ResponseEntity<Object> startSystem() {
        if (isSystemRunning) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseMessage("System is already running."));
        }

        isSystemRunning = true;
        logMessages.clear(); // Clear logs for a new run
        taskExecutor.execute(this::populateTickets);
        return ResponseEntity.ok(new ResponseMessage("System Started"));
    }

    @GetMapping(path = "/stop", produces = "application/json")
    public ResponseEntity<Object> stopSystem() {
        if (!isSystemRunning) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseMessage("System is not running."));
        }

        isSystemRunning = false;
        return ResponseEntity.ok(new ResponseMessage("System Stopped"));
    }

    @GetMapping(path = "/logs", produces = "application/json")
    public ResponseEntity<List<String>> getLogs() {
        return ResponseEntity.ok(new ArrayList<>(logMessages));
    }

    private void populateTickets() {
        logMessages.add("System started processing tickets...");
        while (isSystemRunning) {
            try {
                List<Configuration> configurations = configurationRepository.findAll();
                logMessages.add("Configurations found: " + configurations.size());

                for (Configuration config : configurations) {
                    List<TicketPool> ticketPools = ticketPoolRepository.findAllByVendorId(config.getVendor().getId());
                    logMessages.add("Ticket pools found for vendor: " + ticketPools.size());

                    List<Customer> customers = customerRepository.findAll();

                    for (TicketPool ticketPool : ticketPools) { // Iterate through all ticket pools
                        for (Customer customer : customers) {
                            int ticketsForCustomer = config.getTicfrocustomer();

                            // Check if ticket pool is empty
                            if (ticketPool.getCount() == 0) {
                                logMessages.add("TicketPool is empty for vendor: " + ticketPool.getVendor().getName());
                                ticketPool.setStatus(statusRepository.findByName("Sold"));
                                ticketPoolRepository.save(ticketPool); // Ensure the status is saved immediately
                                break; // Break out of the customer loop as the pool is empty
                            }

                            // Allocate tickets for the current customer
                            int ticketsAllocated = 0;
                            while (ticketsAllocated < ticketsForCustomer && ticketPool.getCount() > 0) {
                                Purchasedticket ticket = new Purchasedticket();
                                ticket.setTicketpool(ticketPool);
                                ticket.setCustomer(customer);
                                ticket.setPurchasedate(LocalDate.now());

                                purchasedTicketRepository.save(ticket);

                                ticketsAllocated++;
                                config.setRemainingtic(config.getRemainingtic() - 1);
                                configurationRepository.save(config);

                                ticketPool.setCount(ticketPool.getCount() - 1);
                                ticketPoolRepository.save(ticketPool);
                            }

                            // Log customer ticket allocation
                            logMessages.add("Tickets allocated for customer: " + customer.getName() +
                                    " from vendor: " + ticketPool.getVendor().getName() +
                                    " (Allocated: " + ticketsAllocated + ")");

                            // Stop allocation for the current configuration if tickets are exhausted
                            if (config.getRemainingtic() <= 0) {
                                logMessages.add("Configuration exhausted for vendor: " + ticketPool.getVendor().getName());
                                break; // Break out of the customer loop
                            }
                        }

                        // If ticket pool is empty, update status (ensure this happens after all customers are processed)
                        if (ticketPool.getCount() == 0 && !"Sold".equals(ticketPool.getStatus().getName())) {
                            logMessages.add("Finalizing ticket pool status for vendor: " + ticketPool.getVendor().getName());
                            ticketPool.setStatus(statusRepository.findByName("Sold"));
                            ticketPoolRepository.save(ticketPool);
                        }
                    }
                }

                // Check if all configurations are exhausted
                boolean allConfigsExhausted = configurations.stream().allMatch(config -> config.getRemainingtic() <= 0);
                if (allConfigsExhausted) {
                    logMessages.add("All configurations are exhausted. Stopping the system...");
                    isSystemRunning = false;
                    break;
                }

                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logMessages.add("System interrupted.");
            } catch (Exception e) {
                logMessages.add("Error occurred: " + e.getMessage());
            }
        }
        logMessages.add("System stopped processing tickets.");
    }



    @Setter
    @Getter
    static class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }
    }
}
