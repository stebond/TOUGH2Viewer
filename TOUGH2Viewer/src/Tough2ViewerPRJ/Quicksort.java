package Tough2ViewerPRJ;

/* Copyright (c) 2012 the authors listed at the following URL, and/or
the authors of referenced articles or incorporated external code:
http://en.literateprograms.org/Quicksort_(Java)?action=history&offset=20090430033330

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Retrieved from: http://en.literateprograms.org/Quicksort_(Java)?oldid=16417
 */
public class Quicksort {

    static <T extends Comparable<? super T>> void quicksort(T[] array, int[] I) {

        quicksort(array, 0, array.length - 1, I);
    }

    static <T extends Comparable<? super T>> void myquicksort(T[] array, int[] I) {
        int left0 = 0;
        int right0 = array.length;

        int temp_i;
        T pivot, temp;
        int left;
        int right;
        do {
            left = left0;
            right = right0;
            pivot = array[left0];

            do {

                do {
                    left++;
                } while (left <= right && array[left].compareTo(pivot) < 0);

                do {
                    right--;
                } while (array[right].compareTo(pivot) > 0);

                if (left < right) {
                    temp = array[left];
                    array[left] = array[right];
                    array[right] = temp;

                    temp_i = I[left];
                    I[left] = I[right];
                    I[right] = temp_i;
                }
            } while (left <= right);

            temp = array[left0];
            array[left0] = array[right];
            array[right] = temp;

            temp_i = I[left0];
            I[left0] = I[right];
            I[right] = temp_i;

            if (left0 < right) {
                right0 = right;
            }
            if (left < right0) {
                left0 = left;
            }
        } while (left0 < right || left < right0);
    }

    static <T extends Comparable<? super T>> void quicksort(T[] array, int left0, int right0, int[] I) {

        int left = left0;
        int right = right0 + 1;
        T pivot, temp;
        int temp_i;
        pivot = array[left0];

        do {

            do {
                left++;
            } while (left <= right0 && array[left].compareTo(pivot) < 0);

            do {
                right--;
            } while (array[right].compareTo(pivot) > 0);

            if (left < right) {
                temp = array[left];
                array[left] = array[right];
                array[right] = temp;

                temp_i = I[left];
                I[left] = I[right];
                I[right] = temp_i;
            }
        } while (left <= right);

        temp = array[left0];
        array[left0] = array[right];
        array[right] = temp;

        temp_i = I[left0];
        I[left0] = I[right];
        I[right] = temp_i;

        if (left0 < right) {
            quicksort(array, left0, right, I);
        }
        if (left < right0) {
            quicksort(array, left, right0, I);
        }
    }
}
