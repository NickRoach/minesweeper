package org.example;

import java.util.Scanner;

public class App {

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int getn() {
        Scanner scannerObject = new Scanner(System.in);
        System.out.print("How large do you want the grid to be? Enter the number of squares on a side: ");
        return scannerObject.nextInt();
    }


    public static int getm() {
        Scanner scannerObject = new Scanner(System.in);
        System.out.print("How many mines do you want to put in the grid? ");
        return scannerObject.nextInt();
    }


    public static void gridRenderer(int[] grid, int n, boolean fin) {

        // print some new lines to clear the screen
//        int lines = 0;
//        while (lines++ < 5) {
//            System.out.println("\n");
//        }
        System.out.println("\n");
        System.out.println("y");

        for (int i = 0; i < n; i++) {
            System.out.print(n - i + "     "); //this prints the y axis indexes
            for (int j = 0; j < n; j++) {
                if (grid[i * n + j] == -3) {     //if a mine
                    if (fin) {
                        System.out.print(" M  ");
                    } else System.out.print(" /  ");
                } else if (grid[i * n + j] == -1) {    //if safe but not yet cleared
                    System.out.print(" /  ");
                } else if (grid[i * n + j] == -4) {    //if this was a mine just triggered
                    System.out.print(" X  ");
                } else
                    System.out.print(" " + grid[i * n + j] + "  ");    //if clear, print it's value (the number of mines around)
            }
            System.out.printf("\n");
        }
        //this prints the x axis indexes
        System.out.println(" ");
        System.out.print("      ");
        for (int j = 0; j < n; j++) {
            System.out.print(" " + (j + 1) + "  ");
        }
        System.out.printf(" x\n");
    }

    public static boolean onRightEdge(int index, int n) {
        return (index + 1) % n == 0;
    }

    public static boolean onLeftEdge(int index, int n) {
        return (index) % n == 0;
    }


    public static int minesAround(int[] grid, int index, int n) {
        int counter = 0;
        //this row
        if (!onLeftEdge(index, n)) {    //if it is not on the left edge, check the element to the left
            if (grid[index - 1] == -3) counter++;
        }

        if (!onRightEdge(index, n)) {    //if it is not on the right edge, check the element to the right
            if (grid[index + 1] == -3) counter++;
        }

        //row above
        if (index - n > 0) { //if it is not on the top row
            if (grid[index - n] == -3) counter++; //check the square above

            if (!onLeftEdge(index, n)) {    //if it is not on the left edge
                if (grid[index - n - 1] == -3) counter++; //check the square on the row above and one to the left
            }

            if (!onRightEdge(index, n)) {    //if it is not on the right edge
                if (grid[index - n + 1] == -3) counter++; //check the square on the row above and one to the right
            }
        }

        //row below
        if (index + n < grid.length) {  //if it is not on the bottom row
            if (grid[index + n] == -3) counter++; //check the square below

            if (!onRightEdge(index, n)) {    //if it is not on the right edge
                if (grid[index + n + 1] == -3) counter++; //check the square on the row below and one to the right
            }

            if (!onLeftEdge(index, n)) {    //if it is not on the left edge
                if (grid[index + n - 1] == -3) counter++; //check the square on the row below and one to the left
            }
        }

        return counter;
    }


    public static void main(String[] args) {
        int n;
        int m;
        int x;
        int y;
        int a;
        int b;
        int squaresCleared = 0;
        boolean fin = false;
        boolean endGame = false;

        while (!endGame) {
            n = 0;
            m = 0;
            x = 0;
            y = 0;
            fin =  false;

            while (n == 0) {
                n = getn();
                if (n < 3 || n > 9) {
                    System.out.println("Please enter a value between 3 and 9");
                    n = 0;
                }
            }

            System.out.printf("Your grid will have %d squares\n", n * n);


            while (m == 0) {
                m = getm();
                if (m <= 0 || m > (n * n)) {
                    System.out.println("Please enter a value greater than zero and less than the number of squares");
                    m = 0;
                }
            }

            System.out.printf("%d mines have been laid in the grid... ", m);
            System.out.println("Good luck");

            int[] grid = new int[n * n];

            //grid initializer
            for (int i = 0; i < grid.length; i++) {
                grid[i] = -1;
            }

            int k = 0;
            while (k < m) {
                int index = getRandomNumber(0, grid.length);
                if (grid[index] != -3) {
                    grid[index] = -3;
                    k++;
                }
            }

            //render the grid
            gridRenderer(grid, n, false);

            //start playing
            while (!fin) {

                x = 0;
                y = 0;

                Scanner scannerObject = new Scanner(System.in);

                while (x == 0) {
                    System.out.print("Enter the x co-ordinate of your guess: ");
                    x = scannerObject.nextInt();
                    if (x > n) {
                        System.out.println("You fell off the grid. Please enter a valid x index");
                        x = 0;
                    }
                }

                while (y == 0) {
                    System.out.print("Enter the y co-ordinate of your guess: ");
                    y = scannerObject.nextInt();
                    if (y > n) {
                        System.out.println("You fell off the grid. Please enter a valid y index");
                        y = 0;
                    }
                }

                    //this is to make the bottom left corner addressed as 1, 1 for a more intuitive input
                    a = x - 1;
                    b = n - y;


                    //guess checker
                    if (grid[b * n + a] == -3) {    //if the square has a mine
                        grid[b * n + a] = -4;
                        gridRenderer(grid, n, true);
                        System.out.printf("Your guess was %d, %d\n", x, y);
                        System.out.println("Boooom! You set off a mine! How unfortunate!");
                        fin = true;
                    } else if (grid[b * n + a] == -1) {     //if the square is safe, not yet cleared
                        int mineNumber = minesAround(grid, b * n + a, n);
                        grid[b * n + a] = mineNumber;
                        gridRenderer(grid, n, false);
                        squaresCleared++;
                        System.out.printf("Your guess was %d, %d\n", x, y);
                        if(mineNumber == 1) {
                            System.out.printf("Phew! That square is safe. There is %d mine around this square\n", mineNumber);
                        }
                        else System.out.printf("Phew! That square is safe. There are %d mines around this square\n", mineNumber);
                        System.out.printf("%d squares remain to be cleared\n", (n * n - squaresCleared - m));

                    } else if (grid[b * n + a] >= 0) {     //if the square is already cleared
                        gridRenderer(grid, n, false);
                        System.out.printf("Your guess was %d, %d\n", x, y);
                        if (grid[b * n + a] == 1) {
                            System.out.printf("You have already cleared this square. There is %d bomb around this square \n", grid[b * n + a]);
                        }
                        else System.out.printf("You have already cleared this square. There are %d bombs around this square \n", grid[b * n + a]);
                        System.out.printf("%d squares remain to be cleared\n", (n * n - squaresCleared - m));
                    }

                    if (squaresCleared == (n * n - m) && !fin) {   //if all the safe squares have been cleared the game is over
                        gridRenderer(grid, n, true);
                        System.out.printf("Your guess was %d, %d\n", x, y);
                        System.out.println("You cleared all the squares! Good job!");
                        fin = true;
                    }
                }
            //get input on whether to play again or not
            Scanner scannerObject = new Scanner(System.in);
            System.out.println("Enter 1 to exit or anything else to play again");
            int playAgain = scannerObject.nextInt();
            if (playAgain == 1) endGame = true;
            }
        }
    }
