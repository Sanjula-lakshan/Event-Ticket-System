package models;

import utils.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate; // Retrieval interval in milliseconds
    private final int totalTickets;
    private static final AtomicInteger customerCounter = new AtomicInteger(0);
    private final int customerId;

    public Customer(TicketPool ticketPool, int retrievalRate, int totalTickets) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate * 1000; // Convert seconds to milliseconds
        this.totalTickets = totalTickets;
        this.customerId = customerCounter.incrementAndGet();
    }

    @Override
    public void run() {
        Logger.log("Customer " + customerId + " started.");

        while (ticketPool.getCurrentTickets() > 0 && ticketPool.getCurrentTickets() <= totalTickets) {
            ticketPool.removeTicket();
            Logger.log("Customer " + customerId + " purchased a ticket.");

            try {
                Thread.sleep(retrievalRate);
            } catch (InterruptedException e) {
                Logger.log("Customer " + customerId + " interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }

        Logger.log("Customer " + customerId + " finished purchasing tickets.");
    }
}

