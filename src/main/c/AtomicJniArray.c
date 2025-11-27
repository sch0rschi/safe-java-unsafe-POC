#include "com_example_unsafe_UnsafeJniArray.h"
#include <jni.h>
#include <stdint.h>
#include <stdlib.h>
#include <stdatomic.h>

static _Atomic uint64_t* buffer = NULL;
static jlong length = 0;

JNIEXPORT void JNICALL Java_com_example_unsafe_AtomicJniArray_init(JNIEnv *env,
                                                             jclass cls,
                                                             jlong size) {
    if (buffer) {
        free(buffer);
        buffer = NULL;
    }
    length = size;
    posix_memalign((void**)&buffer, 64, sizeof(_Atomic uint64_t) * size);
    for (jlong i = 0; i < size; i++)
        atomic_init(&buffer[i], 0);
}

JNIEXPORT void JNICALL Java_com_example_unsafe_AtomicJniArray_putLong(JNIEnv *env,
                                                                jclass cls,
                                                                jlong index,
                                                                jlong value) {
    atomic_store_explicit(&buffer[index], value, memory_order_relaxed);
}

JNIEXPORT jlong JNICALL Java_com_example_unsafe_AtomicJniArray_getLong(JNIEnv *env,
                                                                 jclass cls,
                                                                 jlong index) {
    return atomic_load_explicit(&buffer[index], memory_order_relaxed);
}

JNIEXPORT void JNICALL Java_com_example_unsafe_AtomicJniArray_addOne(JNIEnv *env,
                                                               jclass cls,
                                                               jlong index) {
    atomic_fetch_add_explicit(&buffer[index], 1, memory_order_relaxed);
}

JNIEXPORT void JNICALL Java_com_example_unsafe_AtomicJniArray_free(JNIEnv *env,
                                                             jclass cls) {
    if (buffer) {
        free(buffer);
        buffer = NULL;
        length = 0;
    }
}
