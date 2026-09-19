package Level;

import Engine.Config;
import GameObject.ImageEffect;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Reads and writes a map's decorations file.
 * Decorations are kept in their own file next to the map's tile file so the tile file format does not change
 * (for example, "test_map.txt" has its decorations in "test_map_decorations.txt").
 * A map without a decorations file simply has no decorations.
 *
 * Each line of the file is one decoration, in this format:
 *   x y scale flip layer crop image
 * for example:
 *   312 480 3 NONE GROUND full Flower.png
 *   96 144 3 FLIP_HORIZONTAL DEPTH_SORTED 16,32,16,16 CommonTileset.png
 * crop is "full" to use the whole image, or x,y,width,height of the area of the image file to use.
 * The image file name is last so it can contain spaces. Lines starting with # are comments.
 */
public class DecorationFile {
    private static final String FULL_IMAGE_CROP = "full";

    // prevents DecorationFile from being instantiated
    private DecorationFile() { }

    // gets the decorations file name that goes along with a map file name
    public static String getDecorationFileName(String mapFileName) {
        String baseName = mapFileName.endsWith(".txt") ? mapFileName.substring(0, mapFileName.length() - 4) : mapFileName;
        return baseName + "_decorations.txt";
    }

    // loads the decorations for a map file
    // any line that can't be understood is skipped (with a message printed) instead of crashing the game
    public static ArrayList<Decoration> load(String mapFileName) {
        ArrayList<Decoration> decorations = new ArrayList<>();
        String decorationFileName = getDecorationFileName(mapFileName);
        File file = new File(Config.MAP_FILES_PATH + decorationFileName);
        if (!file.exists()) {
            return decorations;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    decorations.add(parseLine(line));
                } catch (RuntimeException e) {
                    System.out.println("Skipping invalid line " + lineNumber + " in " + Config.MAP_FILES_PATH + decorationFileName + " (" + e.getMessage() + "): " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to read decorations file " + Config.MAP_FILES_PATH + decorationFileName);
        }
        return decorations;
    }

    private static Decoration parseLine(String line) {
        String[] parts = line.split("\\s+", 7);
        if (parts.length < 7) {
            throw new IllegalArgumentException("expected 7 values but found " + parts.length);
        }
        float x = Float.parseFloat(parts[0]);
        float y = Float.parseFloat(parts[1]);
        float scale = Float.parseFloat(parts[2]);
        if (scale <= 0) {
            throw new IllegalArgumentException("scale must be greater than 0");
        }
        ImageEffect imageEffect = ImageEffect.valueOf(parts[3]);
        DecorationLayer layer = DecorationLayer.valueOf(parts[4]);
        Rectangle crop = parseCrop(parts[5]);
        String imageFileName = parts[6].trim();
        return new Decoration(imageFileName, crop, x, y, scale, imageEffect, layer);
    }

    private static Rectangle parseCrop(String cropText) {
        if (cropText.equalsIgnoreCase(FULL_IMAGE_CROP)) {
            return null;
        }
        String[] values = cropText.split(",");
        if (values.length != 4) {
            throw new IllegalArgumentException("crop must be \"full\" or x,y,width,height");
        }
        Rectangle crop = new Rectangle(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3]));
        if (crop.width <= 0 || crop.height <= 0) {
            throw new IllegalArgumentException("crop width and height must be greater than 0");
        }
        return crop;
    }

    // writes a map's decorations to its decorations file
    // a map with no decorations and no existing decorations file does not get an (empty) file created for it
    public static void save(String mapFileName, List<Decoration> decorations) throws IOException {
        String decorationFileName = getDecorationFileName(mapFileName);
        File file = new File(Config.MAP_FILES_PATH + decorationFileName);
        if (decorations.isEmpty() && !file.exists()) {
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("# Decorations for map file " + mapFileName + " -- edit these with the Map Editor's Decorations tab\n");
            fileWriter.write("# x y scale flip layer crop image\n");
            for (Decoration decoration : decorations) {
                Rectangle crop = decoration.getCrop();
                fileWriter.write(
                        formatNumber(decoration.getX()) + " " +
                        formatNumber(decoration.getY()) + " " +
                        formatNumber(decoration.getScale()) + " " +
                        decoration.getImageEffect().name() + " " +
                        decoration.getLayer().name() + " " +
                        (crop == null ? FULL_IMAGE_CROP : Decoration.formatCrop(crop)) + " " +
                        decoration.getImageFileName() + "\n"
                );
            }
        }
    }

    // writes whole numbers without a trailing ".0" to keep the file easy to read
    private static String formatNumber(float number) {
        if (number == Math.round(number)) {
            return String.valueOf(Math.round(number));
        }
        return String.valueOf(number);
    }
}
