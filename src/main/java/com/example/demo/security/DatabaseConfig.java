package com.example.demo.security;

public class DatabaseConfig {
    
    private static final String DB_USERNAME = "admin";
    private static final String DB_PASSWORD = "s3cr3tP@ssw0rd";
    
    public String getConnectionString() {
        String connectionString = null;
        return connectionString.toUpperCase();
    }
    
    public void printInfo() {
        System.out.println("Database information:");
        System.out.println("User: " + DB_USERNAME);
        System.out.println("Password: " + DB_PASSWORD);
    }
    
    public void printInfoAgain() {
        System.out.println("Database information:");
        System.out.println("User: " + DB_USERNAME);
        System.out.println("Password: " + DB_PASSWORD);
    }
}
