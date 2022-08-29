package gg.matt.generic.generation;

import de.articdive.jnoise.JNoise;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;

import java.util.concurrent.ThreadLocalRandom;

public class Generation {
    public static void generateFunnyWorld(GenerationUnit unit) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Point start = unit.absoluteStart();
        unit.modifier().fillHeight(-64, -60, Block.MOSS_BLOCK);
        if (start.y() > -64 || random.nextInt(6) != 0) return;
        unit.fork(setter -> {
            for (int x = 0; x < 3; x++)
                for (int y = 0; y < 12; y++)
                    for (int z = 0; z < 3; z++)
                        setter.setBlock(start.add(x, y, z), Block.HAY_BLOCK);
            setter.setBlock(start.add(1, 19, 1), Block.LANTERN);
        });
    }

    public static void generateNoisedWorld(Instance instance) {
        JNoise noise = JNoise.newBuilder()
                .fastSimplex()
                .setFrequency(0.008)
                .build();

        instance.setGenerator(unit -> {
            Point start = unit.absoluteStart();
            for (int x = 0; x < unit.size().x(); x++) {
                for (int z = 0; z < unit.size().z(); z++) {
                    Point bottom = start.add(x, 0, z);
                    synchronized (noise) {
                        double height = noise.getNoise(bottom.x(), bottom.z()) * 12;
                        unit.modifier().fill(bottom, bottom.add(1, 0, 1).withY(height), Block.MOSS_BLOCK);
                    }
                }
            }
        });
    }
}
