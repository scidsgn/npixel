package com.npixel.base.palette;

public class NStopPalette extends Palette {
    public NStopPalette(String name, int stops) {
        super(name);

        // TODO: naming
        generatePalette(stops);
    }

    private void generatePalette(int stops) {
        int n = 1;

        for (int r = 0; r < stops; r++) {
            for (int g = 0; g < stops; g++) {
                for (int b = 0; b < stops; b++) {
                    colors.add(new NamedColor(
                            name + " Color #" + n,
                            (double) r / (stops - 1),
                            (double) g / (stops - 1),
                            (double) b / (stops - 1)
                    ));

                    n += 1;
                }
            }
        }
    }
}
