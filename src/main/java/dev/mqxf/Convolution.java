package dev.mqxf;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Convolution {

    public void run() {
        String filename = "mqxf.png";
        try {
            BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream("/" + filename));
            BufferedImage newImage = copyImage(image);

            float[][] data = new float[image.getWidth() * 2][image.getHeight() * 2];
            float[][] gaussianData = setupGaussian(image.getWidth(), image.getHeight(), 3);

            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    data[i * 2][j * 2] = image.getRGB(i, j);
                }
            }

            float[][] newData = convolution(data, gaussianData);

            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    newImage.setRGB(i, j, (int) newData[i * 2][j * 2]);
                }
            }

            File output = new File("out.png");
            output.createNewFile();

            ImageIO.write(newImage, "png", output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public float[][] convolution(float[][] a, float[][] b) {
        assert a.length == b.length;
        assert a[0].length == b[0].length;
        FloatFFT_2D fft = new FloatFFT_2D(a.length / 2, a[0].length / 2);
        fft.complexForward(a);
        fft.complexForward(b);
        float[][] result = new float[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                result[i][j] = a[i][j] * b[i][j];
            }
        }
        fft.complexInverse(result, true);
        return result;
    }

    public float[][] setupGaussian(int w, int h, float sigma) {
        float[][] gaussianData = new float[w * 2][h * 2];
        float dSigmaSqr = 2 * sigma * sigma;
        float piDSigmaSqr = (float) Math.PI * dSigmaSqr;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j <= i; j++) {
                gaussianData[i * 2][j * 2] = gaussianData[j * 2][i * 2] = (float) (Math.exp(-(i * i + j * j) / dSigmaSqr) / piDSigmaSqr);
            }
        }
        return gaussianData;
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
