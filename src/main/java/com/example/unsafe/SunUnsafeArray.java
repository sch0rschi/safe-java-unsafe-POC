package com.example.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class SunUnsafeArray {

    private static final Unsafe UNSAFE = getUnsafeInstance();

    private final long baseAddress;

    private static Unsafe getUnsafeInstance() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Unable to access Unsafe", e);
        }
    }

    public SunUnsafeArray(long size) {
        this.baseAddress = UNSAFE.allocateMemory(size * Long.BYTES);
        UNSAFE.setMemory(baseAddress, size * Long.BYTES, (byte) 0);
    }

    public void putLong(long index, long value) {
        UNSAFE.putLong(baseAddress + index * Long.BYTES, value);
    }

    public long getLong(long index) {
        return UNSAFE.getLong(baseAddress + index * Long.BYTES);
    }

    public void addLongSafe(long index, long delta) {
        long offset = baseAddress + index * Long.BYTES;
        UNSAFE.getAndAddLong(null, offset, delta);
    }

    public void addLongUnsafe(long index, long delta) {
        long offset = baseAddress + index * Long.BYTES;
        long old = UNSAFE.getLong(offset);
        UNSAFE.putLong(offset, old + delta);
    }

    public void free() {
        UNSAFE.freeMemory(baseAddress);
    }
}
