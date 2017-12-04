package utils;

import java.util.Arrays;

public class Array {

    public static byte[] copyAllInto(byte[] in, byte[] from) {

        byte[] res = Arrays.copyOf(in, in.length+from.length);

        int j = 0;

        for (int i = in.length; i < res.length ; i++) {

            res[i] = from[j];

            j++;

        }

        return res;
    }
}
