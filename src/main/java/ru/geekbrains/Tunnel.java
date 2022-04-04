package ru.geekbrains;

public class Tunnel extends Stage {
    private final int maxCount;
    private static volatile int currentCount = 0;
    private static final Object monitor = new Object();

    public Tunnel(int maxCount) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
        this.maxCount = maxCount;
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);

                synchronized (monitor) {
                    currentCount++;
                }

                while (true) {
                    if (currentCount <= maxCount) {
                        break;
                    }
                }

                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                synchronized (monitor) {
                    currentCount--;
                }
                System.out.println(c.getName() + " закончил этап: " + description);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
