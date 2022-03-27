package ru.geekbrains;

public class HomeWorkApplication {
    private final static Object lock = new Object();
    private static volatile char currentLetter = 'A';

    public static void main(String[] args) {
        Thread threadOne = new ThreadOne();
        Thread threadTow = new ThreadTow();
        Thread threadThree = new ThreadThree();

        threadOne.start();
        threadTow.start();
        threadThree.start();
    }

    public static class ThreadOne extends Thread {
        @Override
        public void run() {
            try {
                synchronized (lock) {
                    for (int i = 0; i < 5; i++) {
                        while (currentLetter != 'A') {
                            lock.wait();
                        }
                        System.out.print("A");
                        currentLetter = 'B';
                        lock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ThreadTow extends Thread {
        @Override
        public void run() {
            try {
                synchronized (lock) {
                    for (int i = 0; i < 5; i++) {
                        while (currentLetter != 'B') {
                            lock.wait();
                        }
                        System.out.print("B");
                        currentLetter = 'C';
                        lock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ThreadThree extends Thread {
        @Override
        public void run() {
            try {
                synchronized (lock) {
                    for (int i = 0; i < 5; i++) {
                        while (currentLetter != 'C') {
                            lock.wait();
                        }
                        System.out.print("C");
                        currentLetter = 'A';
                        lock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
