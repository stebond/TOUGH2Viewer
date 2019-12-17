/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tough2ViewerPRJ;

import java.util.*;
import java.util.Stack;

/**
 *
 * @author stebond
 */
public class QuickSortNonRec {

    static int Partition(String x[], int lb, int ub, int I[]) {

        int down, up, pj, temp_i;
        String a, temp;
        a = x[lb];

        up = ub;

        down = lb;

        while (down < up) {

            while (x[down].compareTo(a) < 0 && down < up) {
                down = down + 1;
            }

            while (x[up].compareTo(a) > 0) {
                up = up - 1;
            }

            if (down < up) {

                temp = x[down];

                x[down] = x[up];

                x[up] = temp;

                temp_i = I[down];
                I[down] = I[up];
                I[up] = temp_i;

            }

        }

        x[lb] = x[up];

        x[up] = a;

        pj = up;

        return (pj);

    }

    static void Quick(String[] a, int lb, int ub, int I[]) {

        Stack S = new Stack();

        S.push(lb);

        S.push(ub);

        while (!S.empty()) {

            ub = (Integer) S.pop();

            lb = (Integer) S.pop();

            if (ub <= lb) {
                continue;
            }

            int i = Partition(a, lb, ub, I);

            if (i - lb > ub - i) {

                S.push(lb);

                S.push(i - 1);

            }

            S.push(i + 1);

            S.push(ub);

            if (ub - i >= i - lb) {

                S.push(lb);

                S.push(i - 1);

            }

        }

    }
}
