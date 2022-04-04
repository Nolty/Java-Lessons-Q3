package ru.geekbrains;

import java.util.concurrent.CyclicBarrier;

public class HomeWorkApp {
    public static final int CARS_COUNT = 4;

    public static void main(String[] args) {
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(CARS_COUNT / 2), new Road(40));
        Car[] cars = new Car[CARS_COUNT];

        CyclicBarrier cb = new CyclicBarrier(CARS_COUNT);
        Car.setCyclicBarrier(cb);

        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }

        for (Car car : cars) {
            Thread thread = new Thread(car);
            thread.start();
        }

        while (true) {
            if (Car.isReadyStart()) {
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
                break;
            }
        }

        while (true) {
         if (Car.isEndRace()) {
             System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
             break;
         }
        }
    }
}
