package com.mmorpg.vortx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.mmorpg.vortx.entiter.Joueur;

public class CameraController {
    private OrthographicCamera camera;
    private Joueur joueur;

    public CameraController(OrthographicCamera camera, Joueur joueur) {
        this.camera = camera;
        this.joueur = joueur;
        centerOnPlayer();
    }

    public void update() {
        centerOnPlayer();
        camera.update(); // Mettre à jour la caméra après avoir modifié ses coordonnées
    }

    private void centerOnPlayer() {
        // Obtenir la position du joueur
        Vector2 joueurPosition = joueur.getPosition();

        // Centrer la caméra sur la position du joueur
        camera.position.set(joueurPosition.x * Carte.TAILLE_CASE + Carte.TAILLE_CASE / 2f,
            joueurPosition.y * Carte.TAILLE_CASE + Carte.TAILLE_CASE / 2f, 0);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
