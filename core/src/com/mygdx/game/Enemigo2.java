package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Enemigo2 extends EnemigoBase{
    Enemigo2() {
        texture = new Texture("nimbus_0.png");
        x = 640;
        y = Utils.random.nextInt(480);
        w = 50 * 2;
        h = 35 * 2;
        vx = -6;
        vidas = 1;
    }

    public void update() {
        x += vx;

        if (y < 0) y=0;
        if (y > 480 - h) y = 480 - h;
    }
}