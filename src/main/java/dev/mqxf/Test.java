package dev.mqxf;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

import java.util.Arrays;

import static java.lang.Math.*;

public class Test {

    int len = 8;

    public void run() {
        float[] array = new float[len * 2];
        int a = 3;
        for (int i = 0; i < len * 2; i += 2) {
            array[i] = (float) cos(2 * PI * a * (i / 2) / len);
        }
        FloatFFT_1D fft = new FloatFFT_1D(len);
        System.out.println(Arrays.toString(array));
        fft.complexForward(array);
        System.out.println(Arrays.toString(array));
    }

}
