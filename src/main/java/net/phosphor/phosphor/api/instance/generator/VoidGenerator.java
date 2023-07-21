package net.phosphor.phosphor.api.instance.generator;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;

public class VoidGenerator implements Generator {

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        unit.modifier().fill(new Pos(0, 0, 0), new Pos(0, 0, 0), Block.GOLD_BLOCK);
    }
}
