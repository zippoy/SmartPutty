package com.sp.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Created by eric on 2017/4/16.
 */
public class Splash {
    static void renderSplashFrame(Graphics2D g, String msg) {
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(120, 140, 200, 40);
        g.setPaintMode();
        g.setColor(Color.BLACK);
        g.drawString("加载 " + msg + "...", 120, 150);
    }

}
