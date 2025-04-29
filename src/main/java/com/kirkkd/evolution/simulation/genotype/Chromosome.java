package com.kirkkd.evolution.simulation.genotype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record Chromosome(String id, List<Locus<?>> loci) {
    public Chromosome(String id, List<Locus<?>> loci) {
        this.id = id;
        this.loci = new ArrayList<>(loci);
    }

    @Override
    public List<Locus<?>> loci() {
        return Collections.unmodifiableList(loci);
    }

    public Chromosome copy() {
        List<Locus<?>> copiedLoci = loci.stream()
                .map(Locus::copy)
                .collect(Collectors.toList());
        return new Chromosome(id, copiedLoci);
    }


    @Override
    public String toString() {
        return id + ": " + loci.size() + " loci";
    }

    public String toIndentedString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append(id)
                .append(" (").append(loci.size()).append(" loci):\n");
        for (Locus<?> locus : loci) {
            sb.append(locus.toIndentedString(indent))
                    .append("\n");
        }
        return sb.toString();
    }
}
