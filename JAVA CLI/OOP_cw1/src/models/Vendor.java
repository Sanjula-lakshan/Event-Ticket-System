package models;

import utils.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int releaseRate; // Tickets per interval in milliseconds
    private final int totalTickets;
    private static final AtomicInteger vendorCounter = new AtomicInteger(0);
    private final int vendorId;

    public Vendor(TicketPool ticketPool, int releaseRate, int totalTickets) {
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate * 1000; // Convert seconds to milliseconds
        this.totalTickets = totalTickets;
        this.vendorId = vendorCounter.incrementAndGet();
    }

    @Override
    public void run() {
        Logger.log("Vendor " + vendorId + " started.");

        while (ticketPool.getCurrentTickets() < totalTickets) {
            String ticketId = "Vendor " + vendorId + "-ID" + System.nanoTime();
            ticketPool.addTickets(1); // Adds 1 ticket at a time
            Logger.log("Vendor " + vendorId + ": Ticket added; Ticket ID: " + ticketId);

            try {
                Thread.sleep(releaseRate);
            } catch (InterruptedException e) {
                Logger.log("Vendor " + vendorId + " interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }

        Logger.log("Vendor " + vendorId + " finished adding tickets.");
    }
}


