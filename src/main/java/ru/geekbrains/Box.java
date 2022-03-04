package ru.geekbrains;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {
    private final ArrayList<T> fruitList;

    public Box() {
        this.fruitList = new ArrayList<>();
    }

    public List<T> getFruitList() {
        return List.copyOf(fruitList);
    }

    public void addFruit(T fruit) {
        fruitList.add(fruit);
    }

    public double getWeight() {
        double weight = 0;
        for (T item : fruitList) {
            weight += item.getWeight();
        }

        return weight;
    }

    public boolean compare(Box<? extends Fruit> other) {
        return this.getWeight() == other.getWeight();
    }

    public void deposit(Box<T> from) {
        this.fruitList.addAll(from.fruitList);
        from.fruitList.clear();
    }

    @Override
    public String toString() {
        return "Box {" +
                "fruitList=" + fruitList +
                '}';
    }
}
