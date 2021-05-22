package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Enemigo3 extends EnemigoBase{
    Enemigo3() {
        texture = new Texture("heavy_0.png");
        x = 640;
        y = Utils.random.nextInt(480);
        w = 50 * 2;
        h = 35 * 2;
        vx = -2;
        vidas = 3;
    }

    public void update() {
        x += vx;

        if (y < 0) y=0;
        if (y > 480 - h) y = 480 - h;
    }
}
