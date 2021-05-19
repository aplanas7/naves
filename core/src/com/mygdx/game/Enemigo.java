package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemigo extends EnemigoBase{
    Texture texture = new Texture("nimbus_0.png");
    float x, y, w, h, vx, vy;
    Temporizador cambioVelocidad = new Temporizador(60);

    Enemigo() {
        x = 640;
        y = Utils.random.nextInt(480);
        w = 64 * 2;
        h = 48 * 2;
        vx = -2;
        vy = 0;
    }

    public void update() {
        y += vy;
        x += vx;

        if (cambioVelocidad.suena()) {
            vy = Utils.random.nextInt(6) - 3;
            vx = -(Utils.random.nextInt(3)+1);
        }
        //if (x < 0) x=0;
        if (y < 0) y=0;
        //if (x > 640 - w) x = 640 - w;
        if (y > 480 - h) y = 480 - h;
    }

    void render(SpriteBatch batch) {
        batch.draw(texture, x, y, w, h);
    }
}

