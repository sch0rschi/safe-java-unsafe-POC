#include "com_example_unsafe_UnsafeJniArray.h"
#include <jni.h>
#include <stdint.h>
#include <stdlib.h>

static uint64_t *buffer = NULL;
static jlong length = 0;

JNIEXPORT void JNICALL Java_com_example_unsafe_UnsafeJniArray_init(JNIEnv *env,
                                                                jclass cls,
                                                                jlong size) {
  if (buffer) {
    free(buffer);
    buffer = NULL;
  }
  length = size;
  buffer = aligned_alloc(64, sizeof(uint64_t) * size);
  for (jlong i = 0; i < size; i++)
    buffer[i] = 0;
}

JNIEXPORT void JNICALL Java_com_example_unsafe_UnsafeJniArray_putLong(
    JNIEnv *env, jclass cls, jlong index, jlong value) {
  buffer[index] = value;
}

JNIEXPORT jlong JNICALL Java_com_example_unsafe_UnsafeJniArray_getLong(
    JNIEnv *env, jclass cls, jlong index) {
  return buffer[index];
}

JNIEXPORT void JNICALL Java_com_example_unsafe_UnsafeJniArray_free(JNIEnv *env,
                                                                jclass cls) {
  if (buffer) {
    free(buffer);
    buffer = NULL;
  }
}
