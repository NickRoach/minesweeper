package org.example;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

enum square {
    SAFE,
    MINE,
    CLEAR,
    BOOM,
}

public class App 
{

    public static int getRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void gridRenderer(square[] grid, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i * n + j] == square.CLEAR) {
                    System.out.print("[C]");
                } else System.out.print("[ ]");
            }
            System.out.printf("\n");
        }
    }


    public static void debugRenderer(square[] grid, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(grid[i * n + j] == square.MINE) {
                    System.out.print("[M] ");
                }
                if(grid[i * n + j] == square.SAFE) {
                    System.out.print("[ ] ");
                }
                if(grid[i * n + j] == square.CLEAR) {
                    System.out.print("[C] ");
                }
                if(grid[i * n + j] == square.BOOM) {
                    System.out.print("[X] ");
                }

            }
            System.out.printf("\n");
        }
    }

    public static int minesAround(square[] grid, int index, int n){
        int counter = 0;
        if(index - 1 >= 0) {
            if (grid[index - 1] == square.MINE) counter++;
        }

        if(index + 1 < grid.length) {
            if (grid[index + 1] == square.MINE) counter++;
        }

        if(index - n - 1 >= 0) {

            if (grid[index - n - 1] == square.MINE) counter++;
            if (grid[index - n] == square.MINE) counter++;
            if (grid[index - n + 1] == square.MINE) counter++;
        }

        if(index + n + 1 < grid.length) {
            if (grid[index + n + 1] == square.MINE) counter++;
            if (grid[index + n] == square.MINE) counter++;
            if (grid[index + n - 1] == square.MINE) counter++;
        }

        return counter;
    }


    public static void main( String[] args )
    {
        int n;
        int m;
        int x;
        int y;
        int lines = 0;
        int squaresCleared = 0;
        boolean fin = false;
        Scanner scannerObject = new Scanner(System.in);


        System.out.print("How large do you want the grid to be? Enter the number of squares on a side: ");
        n = scannerObject.nextInt();
        System.out.printf("Your grid will have %d squares\n", n*n);
        System.out.print("How many mines do you want to put in the grid? ");
        m = scannerObject.nextInt();
        System.out.printf("%d mines have been laid in the grid\n", m);
        System.out.println("Good luck");

//        n = 3;
//        m = 3;

        square[] grid = new square[n * n];

        //grid initalizer
        for(int i = 0; i < grid.length; i++){
            grid[i] = square.SAFE;
        }

        int k = 0;
        while(k < m){
            int index = getRandomNumber(0,grid.length);
            if(grid[index] != square.MINE){
                grid[index] = square.MINE;
                k++;
            }
        }

        while(!fin) {

        gridRenderer(grid, n);

//            debugRenderer(grid, n);


            System.out.print("Enter the x co-ordinate of your guess: ");
            x = scannerObject.nextInt();
            System.out.print("Enter the y co-ordinate of your guess: ");
            y = scannerObject.nextInt();

            lines = 0;
            while(lines++ < 5) {
                System.out.println("\n");
            }


            System.out.printf("Your guess was %d, %d\n", x, y);

            //this is to make the bottom left corner addressed as 1, 1 for a more intuitive input
            x--;
            y = n - y;


            //guess checker
            if (grid[y * n + x] == square.MINE) {
                System.out.println("Boooom!");
                grid[y * n + x] = square.BOOM;
                fin = true;
            } else if (grid[y * n + x] == square.SAFE) {
                grid[y * n + x] = square.CLEAR;
                System.out.printf("Phew! There are %d bombs around this square\n", minesAround(grid, y * n + x, n));
                squaresCleared++;
            } else if (grid[y * n + x] == square.CLEAR) {
                grid[y * n + x] = square.CLEAR;
                System.out.printf("You have already cleared this square. There are %d bombs around this square \n", minesAround(grid, y * n + x, n));
            }
            if (squaresCleared == n * n - m){
                System.out.println("You cleared all the squares! Good job!");
                fin = true;
            }
        }
        debugRenderer(grid, n);
    }
}
