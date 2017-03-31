package com.example.trdcmacpro.dvr_hammer.util;

public class RecordingItem {
    public final String url;
    public final String name;
    public final String time;
    public final String size;

    public RecordingItem(String url, String name, String time, String size) {
        this.url = url;
        this.name = name;
        this.time = time;
        this.size = size;
    }

    @Override
    public String toString() {
        return url;
    }
}
