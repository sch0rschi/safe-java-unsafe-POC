package com.example.unsafe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.unsafe.Constants.*;

public class PanamaArrayUnsynchronizedTest {

    public static void main(String[] args) throws Exception {

        Utils.printTestStart("Panama unsynchronized add test");

        long startTime = System.nanoTime();

        PanamaArray array = new PanamaArray(NUMBER_OF_LONGS);
        array.putLong(0, 0L);

        try (ExecutorService pool = Executors.newFixedThreadPool((int) NUMBER_OF_THREADS)) {
            for (int threadIndex = 0; threadIndex < NUMBER_OF_THREADS; threadIndex++) {
                pool.submit(() -> {
                    for (long operation_index = 0; operation_index < NUMBER_OF_OPERATIONS; operation_index++) {
                        long content = array.getLong(0);
                        array.putLong(0, content + 1);
                    }
                });
            }
        } finally {
            long arrayContent = array.getLong(0);
            array.close();

            long endTime = System.nanoTime();
            double elapsedSeconds = (endTime - startTime) / 1_000_000_000.0;

            Utils.printResults(arrayContent, elapsedSeconds);
        }
    }
}
