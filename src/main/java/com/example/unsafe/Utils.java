package com.example.unsafe;

import static com.example.unsafe.Constants.NUMBER_OF_OPERATIONS;
import static com.example.unsafe.Constants.NUMBER_OF_THREADS;

public final class Utils {

    private Utils() {
        // finalizing Constructor
    }

    static void printTestStart(String description) {
        System.out.printf("=== Running %s with %d threads and %d operations each. ===\n",
                description,
                NUMBER_OF_THREADS,
                NUMBER_OF_OPERATIONS);
    }

    static void printResults(long arrayContent, double elapsedSeconds) {
        long delta = NUMBER_OF_THREADS * NUMBER_OF_OPERATIONS - arrayContent;
        System.out.printf("Race conditions: %d = %.3f%%.\n", delta, 100.0 * delta / NUMBER_OF_THREADS / NUMBER_OF_OPERATIONS);
        System.out.printf("Elapsed time: %.3f seconds.\n", elapsedSeconds);
    }
}
