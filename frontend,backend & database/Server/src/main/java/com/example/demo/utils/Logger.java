package com.example.demo.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {
    public static synchronized void log(String message) {
        String logMessage = LocalDateTime.now() + " - " + message;
        System.out.println(logMessage);
        try (FileWriter writer = new FileWriter("system.log", true)) {
            writer.write(logMessage + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }
}
