package com.footmanff.jdktest.nio;

public class ChannelClosedException extends RuntimeException {

    public ChannelClosedException() {
    }

    public ChannelClosedException(String message) {
        super(message);
    }
}
