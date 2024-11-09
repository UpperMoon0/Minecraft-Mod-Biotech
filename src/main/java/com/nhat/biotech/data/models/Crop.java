package com.nhat.biotech.data.models;

import java.util.List;
import java.util.Map;

public record Crop(String seedId, List<Drop> yields) {

    public Crop(String seedId, List<Drop> yields) {
        this.seedId = seedId;
        this.yields = yields;
    }

    public String seedId() {
        return seedId;
    }

    public List<Drop> yields() {
        return yields;
    }
}
