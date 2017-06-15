// Created by Austin Patel
// 5/31/2017

package edge_detection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**Frame for displaying an image during each step in the process of applying Canny edge detection.*/
public class EdgeDetectionInterface extends JFrame {

    private final static int WIDTH = 3500, HEIGHT = 500;

    private JPanel contentPane;

    public static void main(String[] args) {
        new EdgeDetectionInterface();
    }

    public EdgeDetectionInterface() {
        initFrame();
        addComponents();

        setVisible(true);
    }

    private void initFrame() {
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Edge Detection");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JScrollPane(contentPane = new JPanel()));

        try {
            setIconImage(ImageIO.read(new File("res/Icons/edge detection icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addComponents() {
        try {
            // Initial
            BufferedImage initialImage = ImageIO.read(new File("res/Images/machine.png"));
            contentPane.add(new JLabel("Initial"));
            contentPane.add(new JLabel(new ImageIcon(initialImage)));

            // Gray scale
            BufferedImage grayScaleImage = ImageManager.convertToGrayScale(initialImage);
            contentPane.add(new JLabel("Gray Scale"));
            contentPane.add(new JLabel(new ImageIcon(grayScaleImage)));

            // Gaussian
            BufferedImage blurredImage = ImageManager.blur(grayScaleImage, 2);
            contentPane.add(new JLabel("Gaussian Convolution"));
            contentPane.add(new JLabel(new ImageIcon(blurredImage)));

            // Sobel
            SobelOperator sobelOperator = EdgeDetection.getRoughEdges(blurredImage);
            BufferedImage sobelImage = sobelOperator.getResult();
            contentPane.add(new JLabel("Sobel Operator"));
            contentPane.add(new JLabel(new ImageIcon(sobelImage)));

            // Edge Thinning
            BufferedImage edgeThinningImage = EdgeDetection.thinEdges(sobelOperator);
            contentPane.add(new JLabel("Edge Thinning"));
            contentPane.add(new JLabel(new ImageIcon(edgeThinningImage)));

            // Double Threshold
            DoubleThreshold doubleThreshold = new DoubleThreshold(edgeThinningImage);
            BufferedImage doubleThresholdImage = doubleThreshold.getResult();
            contentPane.add(new JLabel("Double Threshold"));
            contentPane.add(new JLabel(new ImageIcon(doubleThresholdImage)));

            // Hysteresis
            BufferedImage hysteresisImage = EdgeDetection.applyHysteresis(doubleThreshold);
            contentPane.add(new JLabel("Hysteresis"));
            contentPane.add(new JLabel(new ImageIcon(hysteresisImage)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
