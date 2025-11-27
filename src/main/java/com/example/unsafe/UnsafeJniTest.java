package com.example.unsafe;

import java.util.concurrent.*;

public class UnsafeJniTest {

    public static void main(String[] args) throws Exception {

        Utils.printTestStart("unsafe Jni C Interop test");

        long startTime = System.nanoTime();

        UnsafeJniArray.init(Long.BYTES * Byte.SIZE * Constants.NUMBER_OF_LONGS);
        UnsafeJniArray.putLong(0, 0L);

        try (ExecutorService pool = Executors.newFixedThreadPool((int) Constants.NUMBER_OF_THREADS)) {
            for (int threadIndex = 0; threadIndex < Constants.NUMBER_OF_THREADS; threadIndex++) {
                pool.submit(() -> {
                    for (long operation_index = 0; operation_index < Constants.NUMBER_OF_OPERATIONS; operation_index++) {
                        long content = UnsafeJniArray.getLong(0);
                        UnsafeJniArray.putLong(0, content + 1);
                    }
                });
            }
        } finally {
            long arrayContent = UnsafeJniArray.getLong(0);
            UnsafeJniArray.free();

            long endTime = System.nanoTime();
            double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;

            Utils.printResults(arrayContent, elapsedSeconds);
        }
    }
}
