UNAME_S := $(shell uname -s)

ifeq ($(UNAME_S), Darwin)
    JNI_OS_DIR = darwin
    LIB_EXT = dylib
else
    JNI_OS_DIR = linux
    LIB_EXT = so
endif

JAVA_HOME ?= $(shell dirname $(shell dirname $(shell readlink -f $$(which javac)))))
JAVAC      = $(JAVA_HOME)/bin/javac
JAVA       = $(JAVA_HOME)/bin/java

CC         = clang

SRC_JAVA   = src/main/java
SRC_C      = src/main/c
CLASS_DIR  = target/classes
TARGET_DIR = target

JAVA_SRCS := $(shell find $(SRC_JAVA) -name "*.java")

JNI_HEADERS := $(patsubst $(SRC_JAVA)/%.java,$(TARGET_DIR)/%.h,$(JAVA_SRCS))

NATIVE_LIB = $(TARGET_DIR)/libunsafejni.$(LIB_EXT)

MAIN_CLASS = com.example.unsafe.UnsafeJniTest

JNI_INCLUDES = -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/$(JNI_OS_DIR)

all: build

build: $(NATIVE_LIB)

$(JNI_HEADERS): $(JAVA_SRCS)
	mkdir -p $(CLASS_DIR) $(TARGET_DIR)
	$(JAVAC) -h $(TARGET_DIR) -d $(CLASS_DIR) $(JAVA_SRCS)

$(NATIVE_LIB): $(JNI_HEADERS)
	$(CC) -fPIC -shared \
	    -I$(TARGET_DIR) \
	    $(JNI_INCLUDES) \
	    -o $(NATIVE_LIB) $(SRC_C)/*.c

run: build
	$(JAVA) --enable-native-access=ALL-UNNAMED \
	        -Djava.library.path=$(TARGET_DIR) \
	        -cp $(CLASS_DIR) $(MAIN_CLASS)

clean:
	rm -rf $(CLASS_DIR) $(TARGET_DIR)/*.h $(TARGET_DIR)/*.so $(TARGET_DIR)/*.dylib

.PHONY: all build run clean
