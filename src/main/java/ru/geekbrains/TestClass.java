package ru.geekbrains;

import ru.geekbrains.annotations.AfterSuite;
import ru.geekbrains.annotations.BeforeSuite;
import ru.geekbrains.annotations.Test;

public class TestClass {

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("beforeSuite called");
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("afterSuite called");
    }

    @Test(order = 1)
    public void test1() {
        System.out.println("test1 is running");
    }

    @Test(order = 2)
    public void test3() {
        System.out.println("test3 is running");
    }

    @Test(order = 3)
    public void test4() {
        System.out.println("test4 is running");
    }

    @Test(order = 2)
    public void test2() {
        System.out.println("test2 is running");
    }
}
