package com.kirkkd.evolution.simulation.phenotype;

import com.kirkkd.evolution.simulation.genotype.Gene;
import com.kirkkd.evolution.simulation.genotype.Chromosome;
import com.kirkkd.evolution.simulation.genotype.Genome;
import com.kirkkd.evolution.simulation.genotype.Locus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DominanceMapper implements IPhenotypeMapper {
    private final Set<String> dominantAlleles;

    public DominanceMapper(Set<String> dom) {
        this.dominantAlleles = dom;
    }

    @Override
    public Map<GeneKey<Object>, Object> map(Genome genome) {
        Map<GeneKey<Object>, Object> traits = new HashMap<>();
        for (Chromosome chr : genome.chromosomes()) {
            for (Locus<?> locus : chr.loci()) {
                Gene<?> gene = locus.maternal().name();
                if (gene.getMapperMode() == Gene.MapperMode.MENDELIAN) {
                    String a1 = locus.maternal().value().toString();
                    String a2 = locus.paternal().value().toString();
                    System.out.println(a1 + " " + a2);
                    boolean expressed = dominantAlleles.contains(a1)
                            || dominantAlleles.contains(a2);
                    traits.put(new GeneKey<>(gene), expressed);
                }
            }
        }
        return traits;
    }
}
