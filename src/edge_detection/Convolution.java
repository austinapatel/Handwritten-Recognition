// Created by Austin Patel
// 6/13/2017

package edge_detection;

import java.awt.image.BufferedImage;

/**Contains functions for applying convolutions to an image.*/
public class Convolution {

    public Convolution() {
    }

    /**Applies a convolution on an image and returns a two-dimensional array containing the images intensities.*/
    public static int[][] getIntensityResult(BufferedImage image, double[][] kernel) {
        int radius = (kernel.length - 1) / 2;

        int[][] intensities = new int[image.getWidth()][image.getHeight()];

        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++) {
                double sum = 0;

                for (int kX = -radius; kX <= radius; kX++)
                    for (int kY = -radius; kY <= radius; kY++) {
                        int pX = Math.min(Math.max(x + kX, 0), image.getWidth() - 1);
                        int pY = Math.min(Math.max(y + kY, 0), image.getHeight() - 1);

                        sum += kernel[kX + radius][kY + radius] * ImageManager.getPixelIntensity(image, pX, pY);
                    }

                intensities[x][y] = (int) sum;
            }

        return intensities;
    }

    public static BufferedImage getImageResult(BufferedImage image, double[][] kernel) {
        return ImageManager.intensitiesToImage(Convolution.getIntensityResult(image, kernel));
    }
}
