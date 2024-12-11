package com.example.demo.controllers;

import com.example.demo.models.TicketPool;
import com.example.demo.repositories.TicketPoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketPoolRepository ticketRepository;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<TicketPool>> getAllTickets() {
        List<TicketPool> tickets = ticketRepository.findAllTickets();

        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    public ResponseEntity<String> saveTicket(@Valid @RequestBody TicketPool ticket) {

        if (ticket.getId() != null) {
            ticket.setId(null);
        }

        ticketRepository.save(ticket);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Ticket saved successfully");
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<String> updateTicket(@Valid @RequestBody TicketPool ticket) {

        System.out.println(ticket);
        // Log the request body for debugging
        System.out.println("Received Ticket: " + ticket);

        Optional<TicketPool> existingTicket = ticketRepository.findById(ticket.getId());
        if (existingTicket.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ticket with id " + ticket.getId() + " not found");
        }

//        ticket.setVendor(existingTicket.get().getVendor());
        ticketRepository.save(ticket);
        return ResponseEntity.ok("Ticket updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Integer id) {
        if (!ticketRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ticket with id " + id + " not found");
        }

        ticketRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
