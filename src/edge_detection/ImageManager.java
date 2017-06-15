// Created by Austin Patel
// 6/13/2017

package edge_detection;

import java.awt.image.BufferedImage;

/**Handles various functions pertaining to images.*/
public class ImageManager {

    private static final double STANDARD_DEVIATION = 0.84089642;

    public ImageManager() {

    }

    /**Converts the RGB value at a pixel location in a BufferedImage to a grayscale intensity
     * value.*/
    public static int getPixelIntensity(BufferedImage image, int x, int y) {
        int rgb = image.getRGB(x, y);

        // Cut off the first 8 bits (unused)
        rgb = (rgb << 8) >>> 8;

        int r = rgb >>> 16;
        int g = rgb << 16 >>> 24;
        int b = rgb << 24 >>> 24;

        return  (r + b + g) / 3;
    }

    /**Converts a grayscale intensity (0-255) into a rgb int and assigns that value to a
     * certain location on a given image.*/
    public static void setGrayScalePixelIntensity(BufferedImage image, int x, int y, int intensity) {
        int rgb = intensity;

        rgb = rgb << 8;
        rgb += intensity;
        rgb = rgb << 8;
        rgb += intensity;

        image.setRGB(x, y, rgb);
    }

    public static BufferedImage intensitiesToImage(int[][] intensities) {
        BufferedImage result = new BufferedImage(intensities.length, intensities[0].length, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < result.getWidth(); x++)
            for (int y = 0; y < result.getHeight(); y++)
                ImageManager.setGrayScalePixelIntensity(result, x, y, intensities[x][y]);

        return result;
    }

    public static BufferedImage convertToGrayScale(BufferedImage image) {
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++)
                ImageManager.setGrayScalePixelIntensity(result, x, y, ImageManager.getPixelIntensity(image, x, y));

        return result;
    }

    public static BufferedImage blur(BufferedImage image, int radius) {
        // Determine the Gaussian kernel
        int kernalSize = 2 * radius + 1;
        double[][] kernel = new double[kernalSize][kernalSize];

        double twoPiStdDevSquaredRecip = 1.0 / (2 * Math.PI * Math.pow(STANDARD_DEVIATION, 2));
        double twoStudDevSquared = 2 * Math.pow(STANDARD_DEVIATION, 2);

        for (int x = -radius; x <= radius; x++)
            for (int y = -radius; y <= radius; y++) {
                int xSquaredYsquared = (int) (Math.pow(x, 2) + Math.pow(y, 2));

                kernel[x + radius][y + radius] = twoPiStdDevSquaredRecip * Math.exp(-(xSquaredYsquared / twoStudDevSquared));
            }

        // Perform the convolution with the kernel
        return Convolution.getImageResult(image, kernel);
    }

    /**Creates a copy of an image.*/
    public static BufferedImage copy(BufferedImage image) {
        BufferedImage result = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++)
                result.setRGB(x, y, image.getRGB(x, y));

        return result;
    }
}
