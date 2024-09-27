package de.duckoverflow.rpg.mapDesigner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ImageAnalyser {

    public static void analyseImage() {

        Color[] colors = loadPresets();
        ArrayList<StringBuilder> valueList = new ArrayList<>();
        String file = "output.txt";
        BufferedWriter writer = null;

        try {
            BufferedImage image = ImageIO.read(new File("src/res/worldimage/grid_image.png"));
            writer = new BufferedWriter(new FileWriter(file));

            final int imageWidth = image.getWidth();
            final int imageHeight = image.getHeight();
            final int blockSize = 16;

            final int blocksX = imageWidth / blockSize;
            final int blocksY = imageHeight / blockSize;

            for (int blockY = 0; blockY < blocksY; blockY++) {
                StringBuilder line = new StringBuilder();
                for (int blockX = 0; blockX < blocksX; blockX++) {

                    int startX = blockX * blockSize;
                    int startY = blockY * blockSize;

                    Color averageColor = getAverageColor(image, startX, startY, blockSize);

                    for (int i = 0; i < colors.length; i++) {

                        if (colors[i].getBlue() == averageColor.getBlue()
                                && colors[i].getRed() == averageColor.getRed()
                                && colors[i].getGreen() == averageColor.getGreen()) {
                            line.append(i).append(" ");
                            if (blockX == 49) {
                                System.out.println(line);
                                valueList.add(line);
                            }
                        }
                    }
                }
            }
            for (StringBuilder builder : valueList) {
                writer.write(builder.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private static Color getAverageColor(BufferedImage image, int startX, int startY, int blockSize) {
        int red = 0, green = 0, blue = 0;
        int totalPixels = blockSize * blockSize;

        for (int x = startX; x < startX + blockSize; x++) {
            for (int y = startY; y < startY + blockSize; y++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                red += pixelColor.getRed();
                green += pixelColor.getGreen();
                blue += pixelColor.getBlue();
            }
        }

        red /= totalPixels;
        green /= totalPixels;
        blue /= totalPixels;

        return new Color(red, green, blue);
    }

    private static Color[] loadPresets() {
        Color[] colors = new Color[9];

        Color c1 = new Color(82, 147, 125);
        Color c2 = new Color(86, 146, 122);
        Color c3 = new Color(221, 196, 171);
        Color c4 = new Color(90, 137, 39);
        Color c5 = new Color(131, 94, 72);
        Color c6 = new Color(68, 144, 173);
        Color c7 = new Color(99, 99, 99);
        Color c8 = new Color(107, 149, 63);
        Color c9 = new Color(255, 255, 255);

        colors[0] = c8;
        colors[1] = c7;
        colors[2] = c6;
        colors[3] = c5;
        colors[4] = c4;
        colors[5] = c3;
        colors[6] = c2;
        colors[7] = c1;
        colors[8] = c9;
        return colors;
    }

}
