package com.example.demo;

public class QuickFixDemo {
    private int unusedField = 42; // Unused field (code smell)

    public int add(int a, int b) {
        // Simple logic, duplicated below
        return a + b;
    }

    public int sum(int a, int b) {
        // Duplicate logic to trigger SonarQube duplication rule
        return a + b;
    }

    public void handleException() {
        try {
            int x = 1 / 0;
        } catch (Exception e) {
            // Empty catch block (code smell)
        }
    }
}
