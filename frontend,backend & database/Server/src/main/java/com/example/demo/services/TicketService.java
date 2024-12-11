//package com.example.demo.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TicketService {
//
//    @Autowired
//    private TicketRepository ticketRepository;
//
//    public Ticket addTicket(String vendorId) {
//        Ticket ticket = new Ticket(vendorId, false);
//        return ticketRepository.save(ticket);
//    }
//
//    public List<Ticket> getAllTickets() {
//        return ticketRepository.findAll();
//    }
//
//    public boolean buyTicket() {
//        List<Ticket> availableTickets = ticketRepository.findAll().stream()
//                .filter(ticket -> !ticket.isSold())
//                .toList();
//        if (!availableTickets.isEmpty()) {
//            Ticket ticket = availableTickets.get(0);
//            ticket.setSold(true);
//            ticketRepository.save(ticket);
//            return true;
//        }
//        return false;
//    }
//
//    public long countAvailableTickets() {
//        return ticketRepository.countByIsSold(false);
//    }
//}
//
