package com.example.unsafe;

import java.util.concurrent.*;

import static com.example.unsafe.Constants.*;

public class AtomicJniTest {

    public static void main(String[] args) throws Exception {

        Utils.printTestStart("atomic Jni C Interop test");

        long startTime = System.nanoTime();

        AtomicJniArray.init(Long.BYTES * Byte.SIZE * NUMBER_OF_LONGS);
        AtomicJniArray.putLong(0, 0L);

        try (ExecutorService pool = Executors.newFixedThreadPool((int) NUMBER_OF_THREADS)) {
            for (int threadIndex = 0; threadIndex < NUMBER_OF_THREADS; threadIndex++) {
                pool.submit(() -> {
                    for (long operation_index = 0; operation_index < NUMBER_OF_OPERATIONS; operation_index++) {
                        AtomicJniArray.addOne(0);
                    }
                });
            }
        } finally {
            long arrayContent = AtomicJniArray.getLong(0);
            AtomicJniArray.free();

            long endTime = System.nanoTime();
            double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;

            Utils.printResults(arrayContent, elapsedSeconds);
        }
    }
}
