package com.example.demo.controllers;

import com.example.demo.repositories.ConfigurationRepository;
import com.example.demo.repositories.PurchasedticketRepository;
import com.example.demo.repositories.TicketPoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final TicketPoolRepository ticketRepository;
    private final PurchasedticketRepository purchasedticketRepository;
    private final ConfigurationRepository configurationRepository;

    @GetMapping(path = "/totaltickets")
    public int getTotalTickets() {
        return ticketRepository.findAllTicketCount();
    }

    @GetMapping(path = "/soldtickets")
    public int getSoldTickets() {
        return purchasedticketRepository.countPurchasedticketTickets();
    }

    @GetMapping(path = "/remainingtickets")
    public int getRemainingTickets() {
        return configurationRepository.findAllRemainingTicketCount();
    }

}
