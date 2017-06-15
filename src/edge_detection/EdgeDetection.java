// Created by Austin Patel
// 6/14/2017

package edge_detection;

import java.awt.image.BufferedImage;
import edge_detection.DoubleThreshold.EdgeType;

/**Contains functions for determining the edges of an image.*/
public class EdgeDetection {

    public EdgeDetection() {
    }

    public static BufferedImage apply(BufferedImage image) {
        return applyHysteresis(new DoubleThreshold(thinEdges(getRoughEdges(ImageManager.blur(ImageManager.convertToGrayScale(image), 2)))));
    }

    public static SobelOperator getRoughEdges(BufferedImage image) {
        return new SobelOperator(image);
    }

    public static BufferedImage thinEdges(SobelOperator sobelOperator) {
        int[][] angles = sobelOperator.getAngles();
        double[][] magnitudes = sobelOperator.getConvolutionMagnitudes();

        BufferedImage initial = sobelOperator.getResult();
        BufferedImage result = new BufferedImage(initial.getWidth(), initial.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 1; x < initial.getWidth() - 1; x++)
            for (int y = 1; y < initial.getHeight() - 1; y++) {
                // Compute the coordinate of the points to compare the gradients with
                int angle = angles[x][y] - 90;

                double x1 = Math.cos(Math.toRadians(angle)), y1 = Math.sin(Math.toRadians(angle));
                double max1 = Math.max(x1, y1);
                x1 = Math.round(x1 * max1);
                y1 = Math.round(y1 * max1);

                angle += 180;

                double x2 = Math.cos(Math.toRadians(angle)), y2 = Math.sin(Math.toRadians(angle));
                double max2 = Math.max(x2, y2);
                x2 = Math.round(x2 * max2);
                y2 = Math.round(y2 * max2);

                double currentMagnitude = magnitudes[x][y];
                double p1Magnitude = magnitudes[x + (int) x1][y + (int) y1];
                double p2Magnitude = magnitudes[x + (int) x2][y + (int) y2];

                // Keep the edge only if it has the greatest magnitude
                if (currentMagnitude >= p1Magnitude && currentMagnitude >= p2Magnitude)
                    ImageManager.setGrayScalePixelIntensity(result, x, y, ImageManager.getPixelIntensity(initial, x, y));
            }

        return result;
    }

    public static BufferedImage applyHysteresis(DoubleThreshold doubleThreshold) {
        BufferedImage initial = doubleThreshold.getResult();
        BufferedImage result = ImageManager.copy(initial);
        EdgeType[][] edgeTypes = doubleThreshold.getEdgeTypes();

        for (int x = 1; x < initial.getWidth() - 1; x++)
            for (int y = 1; y < initial.getHeight() - 1; y++) {
                boolean isAttached = false;

                for (int bX = -1; bX <= 1; bX++)
                    for (int bY = -1; bY <= 1; bY++)
                        if (edgeTypes[x + bX][y + bY] == EdgeType.Strong)
                            isAttached = true;

                if (!isAttached)
                    ImageManager.setGrayScalePixelIntensity(result, x, y, 0);
            }

        return result;
    }
}
