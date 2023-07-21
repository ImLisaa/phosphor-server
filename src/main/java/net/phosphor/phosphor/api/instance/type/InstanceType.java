package net.phosphor.phosphor.api.instance.type;

import net.minestom.server.instance.generator.Generator;
import net.phosphor.phosphor.api.instance.generator.FlatGenerator;
import net.phosphor.phosphor.api.instance.generator.VoidGenerator;

public enum InstanceType {

    FLAT(new FlatGenerator()),
    VOID(new VoidGenerator());


    private final Generator generator;

    InstanceType(Generator generator) {
        this.generator = generator;
    }

    public Generator generator() {
        return this.generator;
    }
}
