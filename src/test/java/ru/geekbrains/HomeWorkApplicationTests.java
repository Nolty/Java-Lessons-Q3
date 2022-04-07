package ru.geekbrains;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HomeWorkApplicationTests {

    @Test
    void getArrayAfterFour_testAssertArrayEquals1() {
        int[] integerArray = {1, 2, 4, 4, 2, 3, 4, 1, 7};
        int[] result = HomeWorkApplication.getArrayAfterFour(integerArray);
        Assertions.assertArrayEquals(new int[] {1, 7}, result);
    }

    @Test
    void getArrayAfterFour_testAssertArrayEquals2() {
        int[] integerArray = {1, 2, 4, 4, 2, 3, 7, 1, 7};
        int[] result = HomeWorkApplication.getArrayAfterFour(integerArray);
        Assertions.assertArrayEquals(new int[] {2, 3, 7, 1, 7}, result);
    }

    @Test
    void getArrayAfterFour_testAssertArrayNotEquals() {
        int[] integerArray = {1, 2, 4, 4, 2, 3, 1, 1, 7};
        int[] result = HomeWorkApplication.getArrayAfterFour(integerArray);
        Assertions.assertNotEquals(new int[] {1, 7}, result);
    }

    @Test
    void getArrayAfterFour_testAssertArrayThrows() {
        int[] integerArray = {1, 2, 6, 3, 2, 3, 1, 1, 7};

        Exception exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> HomeWorkApplication.getArrayAfterFour(integerArray)
        );

        Assertions.assertEquals("Not found", exception.getMessage());
    }

    @Test
    void isArrayContainedOneOrFour_testAssertTrue() {
        int[] integerArray = {0, 2, 4, 3, 2, 3};
        boolean result = HomeWorkApplication.isArrayContainedOneOrFour(integerArray);
        Assertions.assertTrue(result);
    }

    @Test
    void isArrayContainedOneOrFour_testAssertFalse() {
        int[] integerArray = {0, 2, 3, 3, 2, 3};
        boolean result = HomeWorkApplication.isArrayContainedOneOrFour(integerArray);
        Assertions.assertFalse(result);
    }

    @Test
    void isArrayContainedOneOrFour_testAssertFalseIfEmpty() {
        int[] integerArray = {};
        boolean result = HomeWorkApplication.isArrayContainedOneOrFour(integerArray);
        Assertions.assertFalse(result);
    }
}
