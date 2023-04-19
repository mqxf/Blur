package dev.mqxf;

import dev.mv.engine.ApplicationLoop;
import dev.mv.engine.MVEngine;
import dev.mv.engine.gui.parsing.GuiConfig;
import dev.mv.engine.render.WindowCreateInfo;
import dev.mv.engine.render.shared.DrawContext2D;
import dev.mv.engine.render.shared.Window;
import dev.mv.engine.resources.R;
import dev.mv.engine.resources.ResourceLoader;
import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

public class Main implements ApplicationLoop {
    public static void main(String[] args) {
        new Gaussian().run();
        //try (MVEngine engine = MVEngine.init()) {
        //    WindowCreateInfo info = new WindowCreateInfo();
        //    Window window = engine.createWindow(info);
        //    window.run(new Main());
        //}
    }

    DrawContext2D ctx2d;
    int n = 400;
    int m = 5;
    FloatFFT_1D fft = new FloatFFT_1D(n);
    float[] cos = new float[n * 2];
    float[] ftCos = new float[n * 2];

    @Override
    public void start(MVEngine engine, Window window) {

        try {
            ResourceLoader.markFont("defaultFont", "/assets/mvengine/font/defaultfont.png", "/assets/mvengine/font/defaultfont.fnt");
            ResourceLoader.load(engine, new GuiConfig("/gui/guiConfig.xml"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ctx2d = new DrawContext2D(window);
        ctx2d.font(R.fonts.get("defaultFont"));
        ctx2d.chromaCompress(1.0f);
        ctx2d.chromaTilt(-0.5f);
        for (int i = 0; i < n; i++) {
            float use = (float) Math.cos(2 * Math.PI * i * m / n);
            cos[2 * i] = use;
            ftCos[2 * i] = use;
        }
        //[r, i, r, i ...]
        fft.complexForward(ftCos);
    }

    @Override
    public void update(MVEngine mvEngine, Window window) {

    }

    @Override
    public void draw(MVEngine mvEngine, Window window) {
        //ctx2d.color(255, 0, 0, 255);
        //ctx2d.rectangle(0, 0, 100, 100);
        for (int i = 0; i < n; i++) {
            float r = cos[2 * i];
            ctx2d.color((int) (r * 255), 0, 0, 255);
            ctx2d.rectangle(i + 100, 300, 1, 50);
            float g = ftCos[2 * i];
            ctx2d.color(0, (int) (g * 255), 0, 255);
            ctx2d.rectangle(i + 100, 200, 1, 50);
            float b = ftCos[2 * i + 1];
            ctx2d.color(0, 0, (int) (b * 255), 255);
            ctx2d.rectangle(i + 100, 100, 1, 50);
        }
        ctx2d.color(255, 0, 0, 255);
        ctx2d.text(false, 550, 305, 40, "Cos");
        ctx2d.color(0, 255, 0, 255);
        ctx2d.text(false, 550, 205, 40, "FFT Real");
        ctx2d.color(0, 0, 255, 255);
        ctx2d.text(false, 550, 105, 40, "FFT Imaginary");
        ctx2d.text(true, 150, 405, 40, ftCos[2 * m] + ", " + ftCos[2 * (n - m)]);
    }

    @Override
    public void exit(MVEngine mvEngine, Window window) throws Exception {

    }

}