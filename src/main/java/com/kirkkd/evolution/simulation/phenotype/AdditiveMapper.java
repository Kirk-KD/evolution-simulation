package com.kirkkd.evolution.simulation.phenotype;

import com.kirkkd.evolution.simulation.genotype.Chromosome;
import com.kirkkd.evolution.simulation.genotype.Gene;
import com.kirkkd.evolution.simulation.genotype.Genome;
import com.kirkkd.evolution.simulation.genotype.Locus;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AdditiveMapper implements IPhenotypeMapper {
    @Override
    public Map<GeneKey<Object>, Object> map(Genome genome) {
        Map<GeneKey<Object>, Float> traits = new HashMap<>();
        Random random = new Random();

        for (Chromosome chr : genome.chromosomes()) {
            for (Locus<?> locus : chr.loci()) {
                Gene<?> gene = locus.maternal().name();
                if (gene.getMapperMode() == Gene.MapperMode.ADDITIVE) {
                    float v1 = ((Number) locus.maternal().value()).floatValue();
                    float v2 = ((Number) locus.paternal().value()).floatValue();
                    traits.merge(new GeneKey<>(gene), v1 < v2 ? random.nextFloat(v1, v2) : random.nextFloat(v2, v1), Float::sum);
                }
            }
        }

        return new HashMap<>(traits);
    }
}
