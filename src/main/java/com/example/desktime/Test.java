package com.example.desktime;

public class Test
{
    public static int[][] giveArray(int[] array) {

        int[][] newarray = new int[array.length][array.length];

        int inc = 0;

        for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array.length; j++) {


//                System.out.println(array[i]);
                 newarray[i][j] =array[j] + inc;

//                System.out.println(array[i]);


            }
            inc = inc + 10;
        }

        return newarray;
    }

    public static void main(String[] args) {

        int [] array = {1,2,3,4};

        int[][] newarray = giveArray(array);

        for (int i = 0; i < newarray.length; i++) {

          int[] abc =newarray[i];

            for (int j = 0; j < newarray[i].length; j++) {

//                System.out.print(newarray[i][j] + " ");
            }
//            System.out.println();
        }
        int inc=0;

    }
}
