package edu.neu.coe.info6205.union_find;

import java.util.Random;
import java.util.Scanner;

/**
 * Union if the sites are not already connected,
 * Loops until all sites are connected
 *
 */

public class UF_Client {
    public static int count(int n){
        int connect=0;
        int totalComponents=n;
        int pairs =0;
        UF_HWQUPC unionFind = new UF_HWQUPC(n);
        Random rand = new Random();


        while(totalComponents!=1){

                int p = rand.nextInt(n);
                int q = rand.nextInt(n);

                pairs+=1;

                if (unionFind.connected(p, q)) {
                   // System.out.println("already connected!");
                }

                else {
                    unionFind.union(p, q);
                   // System.out.println("just connected");
                    connect++;
                    totalComponents--;
                }
        }
        System.out.println("Number of pairs generated in order to connect all sites: " +pairs);

        return connect;

    }

    /**
     * Main function to take user input for the number of sites that need to be connected
     *
     * n is the number of sites
     */
    public static void main(String[] args){
        System.out.println("Enter the number of sites n: \n");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        count(n);





    }
}
