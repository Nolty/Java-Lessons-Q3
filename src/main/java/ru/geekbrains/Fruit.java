package ru.geekbrains;

public abstract class Fruit {
    private final String name;
    private final double weight;

    public Fruit(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Fruit {" +
                "name='" + name +
                '}';
    }
}
