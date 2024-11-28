package com.mmorpg.vortx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends Game {
    public SpriteBatch batch;
    private Musique musique;

    @Override
    public void create() {
        batch = new SpriteBatch();
        musique = new Musique();
        musique.jouer();  // Démarrer la musique
        this.setScreen(new MenuScreen(this));  // Démarrer avec l'écran du menu
    }

    @Override
    public void render() {
        super.render();  // Appeler la méthode de rendu de l'écran actuel
    }

    @Override
    public void dispose() {
        batch.dispose();
        musique.dispose();  // Libérer la musique lorsqu'elle n'est plus utilisée
    }
}
