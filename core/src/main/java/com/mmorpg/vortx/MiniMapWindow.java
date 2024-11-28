package com.mmorpg.vortx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mmorpg.vortx.entiter.Joueur;

public class MiniMapWindow extends Window {
    private ShapeRenderer shapeRenderer;
    private Joueur joueur;
    private Carte carte;
    private Stage stage;
    private boolean afficherMiniMap;

    public MiniMapWindow(Joueur joueur, Carte carte, Skin skin) {
        super("MiniMap", skin);

        this.shapeRenderer = new ShapeRenderer();
        this.joueur = joueur;
        this.carte = carte;
        this.afficherMiniMap = true; // On commence avec la minimap visible

        // Configuration de la fenêtre de minimap
        this.setSize(600, 600);
        this.setPosition(Gdx.graphics.getWidth() / 2f - this.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - this.getHeight() / 2f);
        this.setMovable(true); // Permet de déplacer la fenêtre
        this.setResizable(true); // Permet de redimensionner la fenêtre

        // Crée un stage pour gérer l'affichage de la fenêtre
        stage = new Stage(new ScreenViewport());
        stage.addActor(this);

        // Activer la gestion des inputs pour le stage
        Gdx.input.setInputProcessor(stage);
    }

    // Méthode pour afficher la minimap
    private void afficherMiniMap() {
        // Si la minimap n'est pas active, ne rien afficher
        if (!afficherMiniMap) return;

        // Définir les limites de la carte en fonction de la taille et de la position de la fenêtre
        float windowX = getX();
        float windowY = getY();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Dessine une version simplifiée de la carte au-dessus de la fenêtre
        for (int x = 0; x < Carte.LARGEUR_CARTE; x++) {
            for (int y = 0; y < Carte.HAUTEUR_CARTE; y++) {
                shapeRenderer.setColor(Color.GRAY); // Par défaut pour les zones normales

                if (carte.getTypeSol(x, y) == Carte.EAU) {
                    shapeRenderer.setColor(Color.BLUE);
                } else if (carte.getTypeSol(x, y) == Carte.MONTAGNE) {
                    shapeRenderer.setColor(Color.BROWN);
                }

                // Dessine un carré représentant chaque case sur la minimap
                shapeRenderer.rect(windowX + x * 5, windowY + y * 5, 5, 5);
            }
        }

        // Dessine la position du joueur en rouge
        shapeRenderer.setColor(Color.RED);
        Vector2 positionJoueur = joueur.getPosition();
        shapeRenderer.rect(windowX + positionJoueur.x * 5, windowY + positionJoueur.y * 5, 5, 5);

        shapeRenderer.end();
    }

    // Méthode pour gérer l'affichage et les inputs
    public void render(float delta) {
        // Mettre à jour et dessiner le stage (fenêtre)
        stage.act(delta);
        stage.draw();

        // Dessiner la minimap si elle est active, après avoir dessiné le stage
        if (afficherMiniMap) {
            afficherMiniMap();
        }
    }

    public Stage getStage() {
        return stage;
    }


    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
}
