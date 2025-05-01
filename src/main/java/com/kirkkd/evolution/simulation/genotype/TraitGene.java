package com.kirkkd.evolution.simulation.genotype;

import com.kirkkd.evolution.util.Color;

import java.util.concurrent.ThreadLocalRandom;

public class TraitGene extends Gene {
    private final TraitType type;
    private final double value;

    public TraitGene(TraitType type, double value, boolean enabled) {
        this.type = type;
        this.value = value;
        this.enabled = enabled;
    }

    public TraitType getType() {
        return type;
    }

    public double getValue() {
        return enabled ? value : type.fallback;
    }

    public enum TraitType {
        METABOLIC_COST_PER_SEC(0.05, 0.005, 1),
        MOVEMENT_COST_PER_POWER_PER_SEC(0.05, 0.005, 1),
        MOVEMENT_POWER_MULTIPLIER(1, 0.1, 5),
        COLOR_RGBA(Color.packRGBA(240, 240, 240, 130), Color.packRGBA(0, 0, 0, 0), Color.packRGBA(255, 255, 255, 255));

        final double fallback;
        final double min;
        final double max;

        TraitType(double fallback, double min, double max) {
            this.fallback = fallback;
            this.min = min;
            this.max = max;
        }

        public double randomValue() {
            return ThreadLocalRandom.current().nextDouble(min, max);
        }
    }
}
