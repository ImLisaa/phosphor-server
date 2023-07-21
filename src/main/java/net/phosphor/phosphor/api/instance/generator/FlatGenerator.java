package net.phosphor.phosphor.api.instance.generator;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;

public class FlatGenerator implements Generator {

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        unit.modifier().fillHeight(-1, 0, Block.GRASS_BLOCK);
        unit.modifier().fillHeight(-2, -1, Block.DIRT);
        unit.modifier().fillHeight(-3, -2, Block.DIRT);
        unit.modifier().fillHeight(-4, -3, Block.BEDROCK);
    }
}
