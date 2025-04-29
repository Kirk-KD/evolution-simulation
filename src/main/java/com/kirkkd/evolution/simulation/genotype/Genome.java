package com.kirkkd.evolution.simulation.genotype;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public record Genome(List<Chromosome> chromosomes) {
    public Genome(List<Chromosome> chromosomes) {
        this.chromosomes = new ArrayList<>(chromosomes);
    }

    @Override
    public List<Chromosome> chromosomes() {
        return Collections.unmodifiableList(chromosomes);
    }

    public Genome copy() {
        List<Chromosome> copiedChromosomes = chromosomes.stream()
                .map(Chromosome::copy)
                .collect(Collectors.toList());
        return new Genome(copiedChromosomes);
    }


    @Override
    public String toString() {
        return "[Genome with " + chromosomes.size() + " chromosomes]\n" + toIndentedString();
    }

    public String toIndentedString() {
        StringBuilder sb = new StringBuilder();
        for (Chromosome chr : chromosomes) {
            sb.append(chr.toIndentedString("  "))
                    .append("\n");
        }
        return sb.toString();
    }
}