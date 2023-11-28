package org.NixDB.Datastructures;
import java.util.Random;

public class DumbSort {
    public static <T extends Comparable<T>> T[] performDumbSort(T[] arr) {
        Random rand = new Random(25);
        for (int i = 0; i < arr.length;i++) {
            int swapIndex = rand.nextInt(i, arr.length);
            swap(arr, i, swapIndex);
            if (i>=1 && !isGreater(arr[i], arr[i-1])) {
                i = -1;
            }
        }
        return arr;
    }

    public static <T extends Comparable<T>> boolean isGreater(T a, T b) {
        return a.compareTo(b) >= 0;
    }

    public static <T> void swap(T[] arr, int x, int y) {
        T temp = arr[x];
        arr[x] = arr[y];
        arr[y] = temp;
    }

    public static void main(String[] args) {
        Random rand = new Random();

            for (int i = 1; i < 13; i++) {
                Integer[] arr = new Integer[i];
                for (int j = 0; j < arr.length; j++) {
                    arr[j] = rand.nextInt();
                }
                long startTime = System.nanoTime();
                performDumbSort(arr);
                long endTime = System.nanoTime();
                long timeTaken = endTime - startTime;
                System.out.printf("n=%-5d t=%12dns %12f second\n", i, timeTaken, timeTaken/1000000000d);
            }
    }
}
