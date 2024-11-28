package com.mmorpg.vortx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Musique {
    private Music musique;

    public Musique() {
        // Charger le fichier audio
        musique = Gdx.audio.newMusic(Gdx.files.internal("musique.mp3"));
    }

    public void jouer() {
        musique.setLooping(true);  // Boucle la musique
        musique.play();            // Démarre la musique
    }

    public void arreter() {
        musique.stop();  // Arrête la musique
    }

    public void dispose() {
        musique.dispose();  // Libère les ressources
    }
}
