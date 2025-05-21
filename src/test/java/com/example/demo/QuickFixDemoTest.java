package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuickFixDemoTest {
    @Test
    void testAdd() {
        QuickFixDemo demo = new QuickFixDemo();
        assertEquals(5, demo.add(2, 3));
    }

    @Test
    void testSum() {
        QuickFixDemo demo = new QuickFixDemo();
        assertEquals(7, demo.sum(3, 4));
    }

    @Test
    void testHandleException() {
        QuickFixDemo demo = new QuickFixDemo();
        // Should not throw
        demo.handleException();
    }
}
