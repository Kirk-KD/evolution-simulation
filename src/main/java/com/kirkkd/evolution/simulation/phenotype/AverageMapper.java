package com.kirkkd.evolution.simulation.phenotype;

import com.kirkkd.evolution.simulation.genotype.Chromosome;
import com.kirkkd.evolution.simulation.genotype.Gene;
import com.kirkkd.evolution.simulation.genotype.Genome;
import com.kirkkd.evolution.simulation.genotype.Locus;

import java.util.*;

public class AverageMapper implements IPhenotypeMapper {
    @Override
    public Map<GeneKey<Object>, Object> map(Genome genome) {
        Map<GeneKey<Object>, List<Float>> collected = new HashMap<>();

        for (Chromosome chr : genome.chromosomes()) {
            for (Locus<?> locus : chr.loci()) {
                Gene<?> gene = locus.maternal().name();

                if (gene.getMapperMode() == Gene.MapperMode.AVERAGE) {
                    GeneKey<Object> key = new GeneKey<>(gene);

                    float v1 = ((Number) locus.maternal().value()).floatValue();
                    float v2 = ((Number) locus.paternal().value()).floatValue();

                    collected.computeIfAbsent(key, k -> new ArrayList<>())
                            .add((v1 + v2) / 2f);
                }
            }
        }

        Map<GeneKey<Object>, Object> averaged = new HashMap<>();

        for (Map.Entry<GeneKey<Object>, List<Float>> entry : collected.entrySet()) {
            List<Float> values = entry.getValue();
            float sum = 0f;
            for (float v : values) {
                sum += v;
            }
            float avg = sum / values.size();
            averaged.put(entry.getKey(), avg);
        }

        return averaged;
    }
}
