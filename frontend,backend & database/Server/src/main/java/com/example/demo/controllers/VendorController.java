package com.example.demo.controllers;

import com.example.demo.models.Vendor;
import com.example.demo.repositories.TicketPoolRepository;
import com.example.demo.repositories.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorRepository vendorRepository;
    private final TicketPoolRepository ticketRepository;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Vendor>> getAllVendors() {
        return ResponseEntity.ok(vendorRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<String> saveVendor(@Valid @RequestBody Vendor vendor) {

        if (vendor.getId() != null) {
            vendor.setId(null);
        }

        Vendor existingVendor = vendorRepository.findByEmail(vendor.getEmail());
        if (existingVendor != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Vendor with email " + vendor.getEmail() + " already exists");
        }

//        if (vendor.getTickets() == null || vendor.getTickets().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("Vendor must have at least one ticket");
//        }

        vendorRepository.save(vendor);
//        vendor.getTickets().forEach(ticket -> ticket.setVendor(vendor));
//        ticketRepository.saveAll(vendor.getTickets());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Vendor saved successfully");
    }

    @PutMapping
    public ResponseEntity<String> updateVendor(@Valid @RequestBody Vendor vendor) {
        Optional<Vendor> existingVendor = vendorRepository.findById(vendor.getId());
        if (existingVendor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Vendor with id " + vendor.getId() + " not found");
        }

//        if (vendor.getTickets() == null || vendor.getTickets().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("Vendor must have at least one ticket");
//        }

        Vendor updatedVendor = existingVendor.get();
        updatedVendor.setName(vendor.getName());
        updatedVendor.setEmail(vendor.getEmail());

        vendorRepository.save(vendor);
//        vendor.getTickets().forEach(ticket -> ticket.setVendor(vendor));
//        ticketRepository.saveAll(vendor.getTickets());

        return ResponseEntity.status(HttpStatus.OK)
                .body("Vendor updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Integer id) {
        Optional<Vendor> existingVendor = vendorRepository.findById(id);
        if (existingVendor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Vendor with id " + id + " not found");
        }

        vendorRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
