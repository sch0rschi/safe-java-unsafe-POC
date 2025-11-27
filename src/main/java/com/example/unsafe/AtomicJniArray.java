package com.example.unsafe;

public class AtomicJniArray {
    static {
        System.loadLibrary("atomicjni");
    }

    public static native void init(long size);

    public static native void putLong(long index, long value);

    public static native long getLong(long index);

    public static native void addOne(long index);

    public static native void free();
}
