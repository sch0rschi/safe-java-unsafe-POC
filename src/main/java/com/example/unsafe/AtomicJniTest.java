package com.example.unsafe;

import java.util.concurrent.*;

public class AtomicJniTest {

    private static final long NUMBER_OF_THREADS = 64;
    private static final long NUMBER_OF_LONGS = 1L;
    private static final long NUMBER_OF_OPERATIONS = 1_000_000L;

    public static void main(String[] args) throws Exception {

        System.out.printf("=== Running atomic Jni C Interop test with %d threads and %d operations each. ===\n",
                NUMBER_OF_THREADS,
                NUMBER_OF_OPERATIONS);

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

            long delta = NUMBER_OF_THREADS * NUMBER_OF_OPERATIONS - arrayContent;
            System.out.printf("Race conditions: %d = %.3f%%.\n", delta, 100.0 * delta / NUMBER_OF_THREADS / NUMBER_OF_OPERATIONS);
            System.out.printf("Elapsed time: %.3f seconds.\n", elapsedSeconds);
        }
    }
}
