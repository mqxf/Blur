package dev.mqxf;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    final float sigma = 3;
    final float sigmaWidth = 3;
    final int calcSigma = (int) (sigma * sigmaWidth);
    final float[][] gaussianData = new float[calcSigma][calcSigma];

    public void run() {
        new Test().run();
        System.exit(0);

        setupGaussian();
        String filename = "Maxium.png";
        try {
            BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream("/" + filename));
            BufferedImage newImage = copyImage(image);

            for (int i = 1; i < image.getWidth() - 1; i++) {
                for (int j = 1; j < image.getHeight() - 1; j++) {
                    newImage.setRGB(i, j, toARGB(blur(image, i, j)));
                }
            }

            File output = new File("out.png");
            output.createNewFile();

            ImageIO.write(newImage, "png", output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int[] blur(BufferedImage image, int i, int j) {
        int[] col = new int[] {0, 0, 0, 0};
        //k => (1, 2, 3) (0 = alpha, 1 = red, 2 = green, 3 = blue)
        for (int x = -calcSigma; x <= calcSigma; x++) {
            for (int y = -calcSigma; y <= calcSigma; y++) {
                try {
                    int[] colour = getARGB(image.getRGB(i + x, j + y));
                    for (int k = 1; k < 4; k++) {
                        col[k] += colour[k] * gaussian(x, y);
                    }
                }
                catch (Throwable t) {}
            }
        }
        return col;
    }

    public float gaussian(int x, int y) {
        return gaussianData[Math.abs(x)][Math.abs(y)];
    }

    public void setupGaussian() {
        float dSigmaSqr = 2 * sigma * sigma;
        float piDSigmaSqr = (float) Math.PI * dSigmaSqr;
        for (int i = 0; i < calcSigma; i++) {
            for (int j = 0; j <= i; j++) {
                gaussianData[i][j] = gaussianData[j][i] = (float) (Math.exp(-(i * i + j * j)/dSigmaSqr) / piDSigmaSqr);
            }
        }
    }

    public int[] getARGB(int encoded) {
        int a = (encoded & 0xff000000) >> 24;
        int r = (encoded & 0x00ff0000) >> 16;
        int g = (encoded & 0x0000ff00) >> 8;
        int b = encoded & 0x000000ff;
        return new int[] {a, r, g, b};
    }

    public int toARGB(int[] colours) {
        int col = 0;
        col += colours[0] << 24;
        col += colours[1] << 16;
        col += colours[2] << 8;
        col += colours[3];
        return col;
    }

    public BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

}