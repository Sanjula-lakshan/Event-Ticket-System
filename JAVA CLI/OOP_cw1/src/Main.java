import config.Configuration;
import ui.CommandLineInterface;
import utils.SystemManager;


public class Main {
    private static Configuration config;
    private static SystemManager systemManager;

    public static void main(String[] args) {
        System.out.println("Welcome to the Real-Time Ticketing System!");
        CommandLineInterface cli = new CommandLineInterface();
        systemManager = new SystemManager();

        cli.start((userChoice) -> {
            switch (userChoice) {
                case CONFIGURE:
                    config = cli.configureSystem();
                    systemManager.setConfig(config);
                    break;
                case START:
                    systemManager.startSystem();
                    break;
                case STOP:
                    systemManager.stopSystem();
                    break;
                case VIEW_STATUS:
                    systemManager.displayTicketStatus();
                    break;
                case EXIT:
                    systemManager.stopSystem();
                    System.out.println("Exiting. Goodbye!");
                    break;
            }
        });
    }
}
