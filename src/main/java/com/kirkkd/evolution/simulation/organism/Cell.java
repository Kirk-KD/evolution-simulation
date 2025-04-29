package com.kirkkd.evolution.simulation.organism;

import com.kirkkd.evolution.rendering.IRenderAction;
import com.kirkkd.evolution.rendering.PrimitiveShapes;
import com.kirkkd.evolution.simulation.genotype.Gene;
import com.kirkkd.evolution.simulation.genotype.Genome;
import com.kirkkd.evolution.simulation.genotype.mutation.IMutationStrategy;
import com.kirkkd.evolution.simulation.phenotype.CompositeMapper;
import com.kirkkd.evolution.simulation.phenotype.GeneKey;
import com.kirkkd.evolution.util.Color;
import com.kirkkd.evolution.util.Vec2f;
import com.kirkkd.evolution.window.IUpdateAction;
import com.kirkkd.evolution.window.Window;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Cell extends Organism implements IRenderAction, IUpdateAction {
    private static final Random RANDOM = new Random();
    private static final Map<GeneKey<?>, IGeneExpression<?>> geneExpressionMap = new HashMap<>() {{
        put(Gene.BODY_COLOR_R.getGeneKey(), cell -> cell.getTrait(Gene.BODY_COLOR_R.getGeneKey()).intValue());
        put(Gene.BODY_COLOR_G.getGeneKey(), cell -> cell.getTrait(Gene.BODY_COLOR_G.getGeneKey()).intValue());
        put(Gene.BODY_COLOR_B.getGeneKey(), cell -> cell.getTrait(Gene.BODY_COLOR_B.getGeneKey()).intValue());
    }};

    private final Vec2f position = new Vec2f(RANDOM.nextFloat(0, 500), RANDOM.nextFloat(0, 500));
    private final Color bodyColor = new Color(
            (int) geneExpressionMap.get(Gene.BODY_COLOR_R.getGeneKey()).express(this),
            (int) geneExpressionMap.get(Gene.BODY_COLOR_G.getGeneKey()).express(this),
            (int) geneExpressionMap.get(Gene.BODY_COLOR_B.getGeneKey()).express(this)
    );

    public Cell(Genome genome) {
        super(genome, new CompositeMapper());
        Window.getInstance().WORLD_SPACE_RENDER_ACTIONS.add(this);
    }

    @Override
    public void render() {
        PrimitiveShapes.circle(new PrimitiveShapes.Context(position, bodyColor), 20);
    }

    @Override
    public void update() {
    }

    public Cell mitosis(IMutationStrategy ms) {
        Genome clonedGenome = this.getGenome().copy();
        return new Cell(ms.mutate(clonedGenome));
    }
}
