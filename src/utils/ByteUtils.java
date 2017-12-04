package utils;

import java.nio.ByteBuffer;

public class ByteUtils {
    private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    public static byte[] longToBytes(long x) {
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] array) {
        buffer.put(array,0, array.length);
        buffer.flip();
        return buffer.getLong();
    }
}
