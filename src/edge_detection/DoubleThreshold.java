// Created by Austin Patel
// 6/14/2017

package edge_detection;

import java.awt.image.BufferedImage;

/**Contains functions for applying the double threshold edge detection for removing some edges
 * and classifying edges as 1) not an edge 2) weak edge or 3) strong edge.*/
public class DoubleThreshold {

    private static final int LOWER_THRESHOLD = 30, UPPER_THRESHOLD = 90;

    public enum EdgeType {
        Suppressed,
        Weak,
        Strong
    }

    private EdgeType[][] edgeTypes;
    private BufferedImage result;

    public DoubleThreshold(BufferedImage image) {
        edgeTypes = new EdgeType[image.getWidth()][image.getHeight()];
        result = ImageManager.copy(image);

        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++) {
                int intensity = ImageManager.getPixelIntensity(result, x, y);

                if (intensity < LOWER_THRESHOLD) {
                    ImageManager.setGrayScalePixelIntensity(result, x, y, 0);
                    edgeTypes[x][y] = EdgeType.Suppressed;
                } else if (intensity < UPPER_THRESHOLD)
                    edgeTypes[x][y] = EdgeType.Weak;
                else
                    edgeTypes[x][y] = EdgeType.Strong;
            }
    }

    public EdgeType[][] getEdgeTypes() {
        return edgeTypes;
    }

    public BufferedImage getResult() {
        return result;
    }
}
