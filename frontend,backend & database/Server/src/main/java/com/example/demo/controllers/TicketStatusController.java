package com.example.demo.controllers;

import com.example.demo.models.Status;
import com.example.demo.repositories.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/ticketstatus")
@RequiredArgsConstructor
public class TicketStatusController {

    private final StatusRepository statusRepository;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Status>> getAllTickets() {
        List<Status> statuses = statusRepository.findAll();
        return ResponseEntity.ok(statuses);
    }
}
