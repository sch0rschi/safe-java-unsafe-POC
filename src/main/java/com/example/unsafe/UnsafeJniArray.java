package com.example.unsafe;

public class UnsafeJniArray {
    static {
        System.loadLibrary("unsafejni");
    }

    public static native void init(long size);

    public static native void putLong(long index, long value);

    public static native long getLong(long index);

    public static native void free();
}
