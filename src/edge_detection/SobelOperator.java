// Created by Austin Patel
// 6/13/2017

package edge_detection;

import java.awt.image.BufferedImage;

/**Contains functions for performing the Sobel convolution on an image.*/
public class SobelOperator {

    private static final double[][] X_KERNAL = {{1, 0, -1},
                                                {2, 0, -2},
                                                {1, 0, -1}};
    private static final double[][] Y_KERNAL = {{1, 2, 1},
                                                {0, 0, 0},
                                                {-1, -2, -1}};

    private int[][] convolutionX, convolutionY;
    private double[][] convolutionMagnitudes;
    private int[][] angles;
    private BufferedImage result;

    public SobelOperator(BufferedImage image) {
        convolutionX = Convolution.getIntensityResult(image, X_KERNAL);
        convolutionY = Convolution.getIntensityResult(image, Y_KERNAL);

        result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        angles = new int[image.getWidth()][image.getHeight()];
        convolutionMagnitudes = new double[image.getWidth()][image.getHeight()];

        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++) {
                int gradientX = convolutionX[x][y];
                int gradientY = convolutionY[x][y];

                // Calculate the magnitude of the gradient
                convolutionMagnitudes[x][y] = Math.sqrt(Math.pow(gradientX, 2) + Math.pow(gradientY, 2));
                ImageManager.setGrayScalePixelIntensity(result, x, y, (int) convolutionMagnitudes[x][y]);

                // Calculate the angle of the gradient
                double angle = Math.toDegrees(Math.atan((double) gradientY / gradientX));

                // Round the angle to either 0, 45, 90 or 135 degrees
                int increment = 45;

                angle = Math.round(angle / increment) * increment;

                if (angle >= 180)
                    angle -= 180;

                angles[x][y] = (int) angle;
            }
    }

    public int[][] getConvolutionX() {
        return convolutionX;
    }

    public int[][] getConvolutionY() {
        return convolutionY;
    }

    public double[][] getConvolutionMagnitudes() {
        return convolutionMagnitudes;
    }

    public BufferedImage getResult() {
        return result;
    }

    public int[][] getAngles() {
        return angles;
    }
}
