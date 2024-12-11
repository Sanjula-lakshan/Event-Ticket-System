package models;

public class TicketPool {
    private int currentTickets;
    private final int maxTicketCapacity;

    public TicketPool(int initialTickets, int maxTicketCapacity) {
        this.currentTickets = initialTickets;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public synchronized void addTickets(int tickets) {
        if (currentTickets + tickets <= maxTicketCapacity) {
            currentTickets += tickets;
            System.out.println("Vendor added " + tickets + " tickets. Current tickets: " + currentTickets);
        } else {
            System.out.println("Cannot add tickets. Maximum capacity reached.");

        }
    }

    public synchronized void removeTicket() {
        if (currentTickets > 0) {
            currentTickets--;
            System.out.println("Customer purchased a ticket. Remaining tickets: " + currentTickets);
        } else {
            System.out.println("No tickets available for purchase.");
        }
    }

    public synchronized int getCurrentTickets() {
        return currentTickets;
    }
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
}
