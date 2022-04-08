package ru.geekbrains;

import ru.geekbrains.annotations.AfterSuite;
import ru.geekbrains.annotations.BeforeSuite;
import ru.geekbrains.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {
    private final Class<?> clazz;
    private Method beforeSuite;
    private Method afterSuite;

    public TestRunner(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void run() {
        List<Item> items = getMethodsList(clazz.getMethods());
        try {
            Object obj = clazz.getConstructor().newInstance();
            beforeSuite.invoke(obj);
            for (Item item : items) {
                item.method.invoke(obj);
            }
            afterSuite.invoke(obj);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private List<Item> getMethodsList(Method[] methods) {
        List<Item> methodList = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeSuite != null) {
                    throw new RuntimeException("Method with annotation BeforeSuite exists");
                }
                beforeSuite = method;
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuite != null) {
                    throw new RuntimeException("Method with annotation AfterSuite exists");
                }
                afterSuite = method;
            } else if (method.isAnnotationPresent(Test.class)) {
                int order = method.getAnnotation(Test.class).order();
                if (order > 0 && order < 10){
                    methodList.add(new Item(order, method));
                } else {
                    throw new RuntimeException("Order value must be between 1 and 10");
                }
            }
        }

        Collections.sort(methodList);
        return methodList;
    }

    private static class Item implements Comparable<Item> {
        int order;
        Method method;

        public Item(int order, Method method) {
            this.order = order;
            this.method = method;
        }

        @Override
        public int compareTo(Item o) {
            return this.order - o.order;
        }
    }

    public static void start(Class<?> clazz) {
        TestRunner testRunner = new TestRunner(clazz);
        testRunner.run();
    }

    public static void start(String clazz) {
        try {
            Class<?> fClass = Class.forName(clazz);
            start(fClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
