package com.example.unsafe;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;

public class PanamaArray implements AutoCloseable {

    private final Arena arena;
    private final MemorySegment segment;
    private static final VarHandle LONG_HANDLE = ValueLayout.JAVA_LONG.varHandle();

    public PanamaArray(long size) {
        this.arena = Arena.ofShared();
        this.segment = arena.allocate(ValueLayout.JAVA_LONG, size);
    }

    public void putLong(long index, long value) {
        long byteOffset = index * Long.BYTES;
        LONG_HANDLE.setVolatile(segment, byteOffset, value);
    }

    public long getLong(long index) {
        long byteOffset = index * Long.BYTES;
        return (long) LONG_HANDLE.getVolatile(segment, byteOffset);
    }

    public void addLongSafe(long index, long delta) {
        long byteOffset = index * Long.BYTES;
        LONG_HANDLE.getAndAdd(segment, byteOffset, delta);
    }

    @Override
    public void close() {
        arena.close();
    }
}