package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BoostDobleDisparo {
    Texture texture;
    float x, y, w, h, vx;
    boolean pillao;
    BoostDobleDisparo() {
        texture = new Texture("doble disparo.png");
        x = 640;
        y = Utils.random.nextInt(480);
        w = 40 * 2;
        h = 40 * 2;
        vx = -4;
        pillao = false;
    }

    public void update() {
        x += vx;

        if (y < 0) y=0;
        if (y > 480 - h) y = 480 - h;
    }

    void render(SpriteBatch batch) {
        batch.draw(texture, x, y, w, h);
    }
}