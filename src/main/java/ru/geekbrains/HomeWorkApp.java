package ru.geekbrains;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeWorkApp {

    public static void main(String[] args) {
        Integer[] arr = {1, 2, 3, 4, 5, 6, 7, 8};

        swap(arr, 1, 6);
        System.out.println(Arrays.toString(arr));

        ArrayList<Integer> integerArrayList = arrayToList(arr);
        System.out.println(integerArrayList);

        Box<Apple> appleBox1 = new Box<>();
        for (int i = 0; i < 10; i++) {
            appleBox1.addFruit(new Apple());
        }

        Box<Apple> appleBox2 = new Box<>();
        for (int i = 0; i < 5; i++) {
            appleBox2.addFruit(new Apple());
        }

        Box<Orange> orangeBox = new Box<>();
        for (int i = 0; i < 10; i++) {
            orangeBox.addFruit(new Orange());
        }

        System.out.println(appleBox1);
        System.out.println(appleBox1);
        System.out.println(orangeBox);

        System.out.println(orangeBox.compare(appleBox1));
        System.out.println(appleBox1.compare(appleBox2));

        System.out.println(appleBox1.getWeight());
        System.out.println(appleBox2.getWeight());
        System.out.println(orangeBox.getWeight());

        appleBox1.deposit(appleBox2);

        System.out.println(appleBox1.getWeight());
        System.out.println(appleBox2.getWeight());
        System.out.println(orangeBox.getWeight());
        System.out.println(orangeBox.compare(appleBox1));
    }

    public static <T> void swap(T[] arr, int from, int to) {
        if (from > 0 && from < arr.length && to > 0 && to < arr.length) {
            T temp = arr[from];
            arr[from] = arr[to];
            arr[to] = temp;
        }
    }

    public static <T> ArrayList<T> arrayToList(T[] arr) {
        return new ArrayList<>(Arrays.stream(arr).toList());
    }
}
