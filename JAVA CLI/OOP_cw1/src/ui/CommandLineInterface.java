package ui;

import config.Configuration;
import utils.Logger;

import java.util.Optional;
import java.util.Scanner;

public class CommandLineInterface {
    public enum UserChoice {
        CONFIGURE, START, STOP, VIEW_STATUS, EXIT
    }

    private final Scanner scanner;

    public CommandLineInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void start(UserActionHandler actionHandler) {
        Logger.log("Command-line interface started.");

        while (true) {
            displayMenu();
            Optional<UserChoice> choice = getUserChoice();

            if (choice.isEmpty()) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            if (choice.get() == UserChoice.EXIT) {
                actionHandler.handle(choice.get());
                break;
            }

            actionHandler.handle(choice.get());
        }
    }

    public Configuration configureSystem() {
        System.out.println("\nSystem Configuration:");
        Logger.log("System Configuration started.");

        Configuration config = new Configuration();
        config.setTotalTickets(readPositiveInteger("Enter total number of tickets: "));
        config.setTicketReleaseRate(readPositiveInteger("Enter ticket release rate (tickets/sec): "));
        config.setCustomerRetrievalRate(readPositiveInteger("Enter customer retrieval rate (tickets/sec): "));
        config.setMaxTicketCapacity(readPositiveInteger("Enter maximum ticket capacity: "));
        config.setNumVendors(readPositiveInteger("Enter number of vendors: "));
        config.setNumCustomers(readPositiveInteger("Enter number of customers: "));

        saveConfiguration(config);

        Logger.log("Configuration has ended.");
        return config;
    }

    private void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Configure the system");
        System.out.println("2. Start the system");
        System.out.println("3. Stop the system");
        System.out.println("4. View ticket status");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private Optional<UserChoice> getUserChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            return switch (choice) {
                case 1 -> Optional.of(UserChoice.CONFIGURE);
                case 2 -> Optional.of(UserChoice.START);
                case 3 -> Optional.of(UserChoice.STOP);
                case 4 -> Optional.of(UserChoice.VIEW_STATUS);
                case 5 -> Optional.of(UserChoice.EXIT);
                default -> Optional.empty();
            };
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private int readPositiveInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Please enter a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
            }
        }
    }

    private void saveConfiguration(Configuration config) {
        try {
            config.saveToFile("configuration.json");
            System.out.println("Configuration saved to JSON successfully.");
            config.saveToTextFile("configuration.txt");
            System.out.println("Configuration saved to text file.");
        } catch (Exception e) {
            System.out.println("Error saving configuration: " + e.getMessage());
        }
    }

    public interface UserActionHandler {
        void handle(UserChoice choice);
    }
}
