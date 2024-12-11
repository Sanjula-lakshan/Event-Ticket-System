package utils;

import config.Configuration;
import models.TicketPool;
import models.Customer;
import models.Vendor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemManager {
    private Configuration config;
    private TicketPool ticketPool;
    private ExecutorService executor;
    private volatile boolean isRunning;

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public void startSystem() {
        if (config == null) {
            System.out.println("System is not configured yet. Please configure the system first.");
            return;
        }

        if (isRunning) {
            System.out.println("System is already running.");
            return;
        }

        ticketPool = new TicketPool(config.getTotalTickets(), config.getMaxTicketCapacity());
        isRunning = true;

        executor = Executors.newFixedThreadPool(config.getNumVendors() + config.getNumCustomers());

        for (int i = 0; i < config.getNumVendors(); i++) {
            executor.execute(new Vendor(ticketPool, config.getTicketReleaseRate(), config.getTotalTickets()));
        }

        for (int i = 0; i < config.getNumCustomers(); i++) {
            executor.execute(new Customer(ticketPool, config.getCustomerRetrievalRate(), config.getTotalTickets()));
        }

        System.out.println("System started.");
    }

    public void stopSystem() {
        if (!isRunning) {
            System.out.println("System is not running.");
            return;
        }

        isRunning = false;
        executor.shutdownNow();

        try {
            if (!executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("System stopped.");
    }

    public void displayTicketStatus() {
        if (ticketPool == null) {
            System.out.println("System not started yet.");
        } else {
            System.out.println("Current tickets: " + ticketPool.getCurrentTickets());
        }
    }
}
