package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    BitmapFont font;
    Fondo fondo;
    Jugador jugador;
    BoostVelocidad boostVelocidad;
    BoostDobleDisparo boostDobleDisparo;
    List<EnemigoBase> enemigos;
    List<Disparo> disparosAEliminar;
    List<EnemigoBase> enemigosAEliminar;
    Temporizador nuevoEnemigo, nuevoEnemigo2, nuevoEnemigo3, nuevoBoostV, nuevoBoostD;
    ScoreBoard scoreboard;
    boolean gameover;
    private Music music;
    Sound explosion;


    @Override
    public void create() {
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.ogg"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(2f);

        inicializarJuego();
    }

    void inicializarJuego(){
        music.setVolume((float) 0.8);
        music.setLooping(true);
        music.play();
        fondo = new Fondo();
        jugador = new Jugador();
        enemigos = new ArrayList<>();
        boostVelocidad = new BoostVelocidad();
        boostDobleDisparo = new BoostDobleDisparo();
        disparosAEliminar = new ArrayList<>();
        enemigosAEliminar = new ArrayList<>();
        gameover = false;
        scoreboard = new ScoreBoard();
        // Temporizadores
        nuevoEnemigo = new Temporizador(120);
        nuevoEnemigo2 = new Temporizador(300);
        nuevoEnemigo3 = new Temporizador(360);
        nuevoBoostV = new Temporizador(Utils.random.nextInt(900));
        nuevoBoostD = new Temporizador(Utils.random.nextInt(1200));
    }

    void enemigoAnalizar() {

        // en la lista enemigos se ponen objetos tanto de Enemigo como de Enemigo2
        for (EnemigoBase enemigo : enemigos) {
            for (Disparo disparo : jugador.disparos) {
                if (Utils.solapan(disparo.x, disparo.y, disparo.w, disparo.h, enemigo.x, enemigo.y, enemigo.w, enemigo.h)) {
                    disparosAEliminar.add(disparo);
                    if (enemigo.vidas == 1) {enemigosAEliminar.add(enemigo);} else {enemigo.vidas -= 1;}

                    jugador.puntos++;
                    break;
                }
            }

            if (!gameover && !jugador.muerto && Utils.solapan(enemigo.x, enemigo.y, enemigo.w, enemigo.h, jugador.x, jugador.y, jugador.w, jugador.h)) {
                jugador.morir();
                if (jugador.vidas == 0){
                    gameover = true;
                    explosion.play();
                }
            }

            if (enemigo.x < -enemigo.w) {
                enemigosAEliminar.add(enemigo);
                if (jugador.puntos != 0){
                    if (gameover==false) {
                        jugador.puntos--;
                    }
                }
            }
        }
    }

    void update() {
        Temporizador.framesJuego += 1;

        if (nuevoEnemigo.suena()) enemigos.add(new Enemigo());
        if (nuevoEnemigo2.suena()) enemigos.add(new Enemigo2());
        if (nuevoEnemigo3.suena()) enemigos.add(new Enemigo3());

        if(!gameover) jugador.update();
        if(nuevoBoostV.aparece()) boostVelocidad.update();
        if(nuevoBoostD.aparece()) boostDobleDisparo.update();

        if (!gameover && !jugador.muerto && Utils.solapan(boostVelocidad.x, boostVelocidad.y, boostVelocidad.w, boostVelocidad.h, jugador.x, jugador.y, jugador.w, jugador.h)) {
            jugador.v = 10;
            boostVelocidad.pillao = true;
        }

        if (!gameover && !jugador.muerto && Utils.solapan(boostDobleDisparo.x, boostDobleDisparo.y, boostDobleDisparo.w, boostDobleDisparo.h, jugador.x, jugador.y, jugador.w, jugador.h)) {
            jugador.dobleDisparo = true;
            boostDobleDisparo.pillao = true;
        }

        for (EnemigoBase enemigo : enemigos) enemigo.update();              // enemigos.forEach(Enemigo::update);

        enemigoAnalizar();

        for (Disparo disparo : jugador.disparos)
            if (disparo.x > 640)
                disparosAEliminar.add(disparo);

        for (Disparo disparo : disparosAEliminar) jugador.disparos.remove(disparo);       // disparosAEliminar.forEach(disparo -> jugador.disparos.remove(disparo));
        for (EnemigoBase enemigo : enemigosAEliminar) enemigos.remove(enemigo);               // enemigosAEliminar.forEach(enemigo -> enemigos.remove(enemigo));
        disparosAEliminar.clear();
        enemigosAEliminar.clear();

        if(gameover) {
            int result = scoreboard.update(jugador.puntos);
            if(result == 1) {
                inicializarJuego();
            } else if (result == 2) {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();

        batch.begin();
        fondo.render(batch);
        jugador.render(batch);
        if (!boostVelocidad.pillao) boostVelocidad.render(batch);
        if (!boostDobleDisparo.pillao) boostDobleDisparo.render(batch);

        for (EnemigoBase enemigo : enemigos) enemigo.render(batch);  // enemigos.forEach(e -> e.render(batch));
        font.draw(batch, "Vidas: " + jugador.vidas, 520, 460);
        font.draw(batch, "Puntos: " + jugador.puntos, 15, 460);

        if (gameover){
            scoreboard.render(batch, font);
        }
        batch.end();
    }
}








/*
init();
create();
while(true) render();
 */