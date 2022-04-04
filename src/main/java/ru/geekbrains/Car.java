package ru.geekbrains;

import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;

    private static CyclicBarrier cyclicBarrier;

    private static volatile boolean readyStart = false;
    private static volatile boolean endRace = false;
    private static final Object monitor = new Object();
    private static volatile boolean isWin = false;

    static {
        CARS_COUNT = 0;
    }

    private final Race race;
    private final int speed;
    private final String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    public static void setCyclicBarrier(CyclicBarrier cyclicBarrier) {
        Car.cyclicBarrier = cyclicBarrier;
    }

    public static boolean isReadyStart() {
        return readyStart;
    }

    public static boolean isEndRace() {
        return endRace;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            cyclicBarrier.await();
            System.out.println(this.name + " готов");
            cyclicBarrier.await();
            readyStart = true;

            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);

                if (i == race.getStages().size() - 1) {
                    synchronized (monitor) {
                        if (!isWin) {
                            System.out.println(this.name + " WIN");
                            isWin = true;
                        }
                    }
                }
            }

            cyclicBarrier.await();
            endRace = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
