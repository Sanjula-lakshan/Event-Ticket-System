package com.example.demo.controllers;

import com.example.demo.models.Purchasedticket;
import com.example.demo.models.TicketPool;
import com.example.demo.repositories.ConfigurationRepository;
import com.example.demo.repositories.PurchasedticketRepository;
import com.example.demo.repositories.TicketPoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/ticketpurchase")
@RequiredArgsConstructor
public class TicketPurchaseController {

    private final PurchasedticketRepository purchasedticketRepository;
    private final TicketPoolRepository ticketRepository;
    private final ConfigurationRepository configurationRepository;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Purchasedticket>> getAllTickets() {
        List<Purchasedticket> purchasedticket = purchasedticketRepository.findAll();
        return ResponseEntity.ok(purchasedticket);
    }

    @PostMapping
    public ResponseEntity<String> saveTicket(@RequestBody Purchasedticket purchasedticket) {
        if (purchasedticket.getId() != null) {
            purchasedticket.setId(null);
        }
        purchasedticketRepository.save(purchasedticket);

        Optional<TicketPool> purchaseTicket = ticketRepository.findById(purchasedticket.getTicketpool().getId());
        if (purchaseTicket.isPresent()) {
            purchaseTicket.get().setCount(purchaseTicket.get().getCount() - 1);
            ticketRepository.save(purchaseTicket.get());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Ticket Purchased Successfully");
    }

    @PutMapping
    public ResponseEntity<String> updateTicket(@RequestBody Purchasedticket purchasedticket) {

        Optional<Purchasedticket> extPurchaseTicket = purchasedticketRepository.findById(purchasedticket.getId());
        if (extPurchaseTicket.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket with id " + purchasedticket.getId() + " not found");
        }

        purchasedticketRepository.save(purchasedticket);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ticket Updated Successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable Integer id) {
        Optional<Purchasedticket> extPurchaseTicket = purchasedticketRepository.findById(id);
        if (extPurchaseTicket.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket with id " + id + " not found");
        }

        Optional<TicketPool> deletePurchase = ticketRepository.findById(extPurchaseTicket.get().getTicketpool().getId());
        if (deletePurchase.isPresent()) {
            TicketPool ticket = deletePurchase.get();

            // Increment the ticket count
            ticket.setCount(ticket.getCount() + 1);
            ticketRepository.save(ticket);

            // Update remaining tickets for each configuration of the vendor
            ticket.getVendor().getConfigurations().forEach(configuration -> {
                configuration.setRemainingtic(configuration.getRemainingtic() + 1);
            });

            // Save updated configurations directly through the repository
            configurationRepository.saveAll(ticket.getVendor().getConfigurations());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket in ticketRepository with id " + id + " not found");
        }

        // Delete the purchased ticket
        purchasedticketRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Ticket Deleted Successfully");
    }


}
