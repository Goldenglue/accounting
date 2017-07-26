package core;

import java.util.Scanner;

public class Test {
    interface Box<T> {
        void put(T item);

        T get();
    }

    public static void main(String[] args) {
        int s = (new Scanner(System.in)).nextInt();
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                if (i == j || i == s - 1 - j || i + 1 == (s + 1) / 2 || j + 1 == (s + 1) / 2) {
                    System.out.print("* ");
                } else {
                    System.out.print(". ");
                }
                System.out.println();
            }

        }
    }


}
