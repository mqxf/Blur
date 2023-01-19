package dev.mqxf;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        try {
            BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream("/Maxium.png"));
            System.out.println(Arrays.toString(getARGB(image.getRGB(0, 0))));
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        return colours[0] << 24 + colours[1] << 16 + colours[2] << 8 + colours[3];
    }

}