package com.nhat.biotech.data;

import com.nhat.biotech.Biotech;

public record Creature(String id) {

    @Override
    public String id() {
        return Biotech.MOD_ID + ":" + id;
    }

    public String babyId() {
        return Biotech.MOD_ID + ":baby_" + id;
    }
}