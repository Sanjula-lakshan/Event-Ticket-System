package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Scanner;

public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int numVendors;
    private int numCustomers;

    // Getters and Setters
    public int getTotalTickets() { return totalTickets; }
    public void setTotalTickets(int totalTickets) { this.totalTickets = totalTickets; }

    public int getTicketReleaseRate() { return ticketReleaseRate; }
    public void setTicketReleaseRate(int ticketReleaseRate) { this.ticketReleaseRate = ticketReleaseRate; }

    public int getCustomerRetrievalRate() { return customerRetrievalRate; }
    public void setCustomerRetrievalRate(int customerRetrievalRate) { this.customerRetrievalRate = customerRetrievalRate; }

    public int getMaxTicketCapacity() { return maxTicketCapacity; }
    public void setMaxTicketCapacity(int maxTicketCapacity) { this.maxTicketCapacity = maxTicketCapacity; }

    public int getNumVendors() { return numVendors; }
    public void setNumVendors(int numVendors) { this.numVendors = numVendors; }

    public int getNumCustomers() { return numCustomers; }
    public void setNumCustomers(int numCustomers) { this.numCustomers = numCustomers; }

    // Save and Load Methods
    public void saveToTextFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Total Tickets: " + totalTickets + "\n");
            writer.write("Ticket Release Rate: " + ticketReleaseRate + "\n");
            writer.write("Customer Retrieval Rate: " + customerRetrievalRate + "\n");
            writer.write("Max Ticket Capacity: " + maxTicketCapacity + "\n");
            writer.write("Number of Vendors: " + numVendors + "\n");
            writer.write("Number of Customers: " + numCustomers + "\n");
        } catch (IOException e) {
            System.out.println("Error saving to text file: " + e.getMessage());
        }
    }

    public static Configuration loadFromTextFile(String fileName) {
        Configuration config = new Configuration();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("Total Tickets: ")) {
                    config.setTotalTickets(Integer.parseInt(line.split(": ")[1]));
                } else if (line.startsWith("Ticket Release Rate: ")) {
                    config.setTicketReleaseRate(Integer.parseInt(line.split(": ")[1]));
                } else if (line.startsWith("Customer Retrieval Rate: ")) {
                    config.setCustomerRetrievalRate(Integer.parseInt(line.split(": ")[1]));
                } else if (line.startsWith("Max Ticket Capacity: ")) {
                    config.setMaxTicketCapacity(Integer.parseInt(line.split(": ")[1]));
                } else if (line.startsWith("Number of Vendors: ")) {
                    config.setNumVendors(Integer.parseInt(line.split(": ")[1]));
                } else if (line.startsWith("Number of Customers: ")) {
                    config.setNumCustomers(Integer.parseInt(line.split(": ")[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading from text file: " + e.getMessage());
        }
        return config;
    }

    public void saveToFile(String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            System.out.println("Error saving configuration: " + e.getMessage());
        }
    }

    public static Configuration loadFromFile(String fileName) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, Configuration.class);
        } catch (IOException e) {
            System.out.println("Error loading configuration: " + e.getMessage());
            return null;
        }
    }
}
