package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuickFixDemoTest {
    @Test
    void testAdd() {
        QuickFixDemo demo = new QuickFixDemo();
        assertEquals(5, demo.add(2, 3));
    }
}
