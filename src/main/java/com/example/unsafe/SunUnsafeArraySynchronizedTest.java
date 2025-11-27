package com.example.unsafe;

import java.util.concurrent.*;

import static com.example.unsafe.Constants.*;

public class SunUnsafeArraySynchronizedTest {

    public static void main(String[] args) throws Exception {

        Utils.printTestStart("sun misc unsafe compareAndSwapLong test");

        long startTime = System.nanoTime();

        SunUnsafeArray array = new SunUnsafeArray(Long.BYTES * Byte.SIZE * NUMBER_OF_LONGS);
        array.putLong(0, 0L);

        try (ExecutorService pool = Executors.newFixedThreadPool((int) NUMBER_OF_THREADS)) {
            for (int threadIndex = 0; threadIndex < NUMBER_OF_THREADS; threadIndex++) {
                pool.submit(() -> {
                    for (long operation_index = 0; operation_index < NUMBER_OF_OPERATIONS; operation_index++) {
                        array.addLongSafe(0, 1);
                    }
                });
            }
        } finally {
            long arrayContent = array.getLong(0);
            array.free();

            long endTime = System.nanoTime();
            double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;

            Utils.printResults(arrayContent, elapsedSeconds);
        }
    }
}
