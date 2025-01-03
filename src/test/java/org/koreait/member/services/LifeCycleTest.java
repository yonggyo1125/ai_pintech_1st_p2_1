package org.koreait.member.services;

import org.junit.jupiter.api.*;

public class LifeCycleTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("BEFORE ALL");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("AFTER ALL");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("BEFORE EACH");
    }

    @AfterEach
    void afterEach() {
        System.out.println("AFTER EACH");
    }

    @Test
    void test1() {
        System.out.println("TEST1");
    }

    @Test
    @Disabled
    void test2() {
        System.out.println("TEST2");
    }

    @Test
    @Timeout(3L)
    void test3() throws Exception {
        System.out.println("TEST3");
        Thread.sleep(5000);
    }
}
