package com.github.naxos84.spaceshooter.helper;

public class MathHelper {

    private MathHelper() {
    }

    public static float clamp(final float value, final float min, final float max) {
        return Math.max(min, Math.min(max, value));
    }

}
