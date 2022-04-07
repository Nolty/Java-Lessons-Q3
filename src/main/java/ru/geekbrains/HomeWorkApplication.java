package ru.geekbrains;

import java.util.Arrays;

public class HomeWorkApplication {

    public static int[] getArrayAfterFour(int[] list) {
        for (int i = list.length - 1; i > 0; i--) {
            if (list[i] == 4) {
                return Arrays.copyOfRange(list, i + 1, list.length);
            }
        }

        throw new RuntimeException("Not found");
    }

    public static boolean isArrayContainedOneOrFour(int[] list) {
        for (int value : list) {
            if (value == 1 || value == 4) {
                return true;
            }
        }

        return false;
    }
}
