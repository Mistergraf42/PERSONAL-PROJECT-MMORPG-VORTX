package com.mmorpg.vortx.entiter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mmorpg.vortx.Carte;

public class Pnj extends Entiter {
    private String nom;
    private String dialogue;
    private boolean interagissable;
    private boolean afficherDialogue;
    private float tempsAffichageDialogue; // Durée pendant laquelle le dialogue est affiché
    private static final float DUREE_AFFICHAGE_DIALOGUE = 2.0f; // 2 secondes

    // Constructeur
    public Pnj(int id,String nom, String dialogue, int pointsDeVieMax, int pointsDAttaque, int pointsDArmure, float x, float y, boolean interagissable) {
        super(id, pointsDeVieMax, pointsDAttaque, pointsDArmure, x, y);
        this.nom = nom;
        this.dialogue = dialogue;
        this.interagissable = interagissable;
        this.afficherDialogue = false;
        this.tempsAffichageDialogue = 0;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDialogue() {
        return dialogue;
    }




    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    public boolean isInteragissable() {
        return interagissable;
    }

    public void setInteragissable(boolean interagissable) {
        this.interagissable = interagissable;
    }

    // Fonction appelée lorsque l'utilisateur clique sur le PNJ
    public void cliquerSurPnj(Vector2 positionClic) {
        if (interagissable ) {
            afficherDialogue = true;
            tempsAffichageDialogue = DUREE_AFFICHAGE_DIALOGUE; // Réinitialiser la durée d'affichage
        }
    }

    public void changerEtat(){
        this.afficherDialogue = true;
        tempsAffichageDialogue = DUREE_AFFICHAGE_DIALOGUE;
    }

    // Méthode pour afficher le dialogue du PNJ (au-dessus de sa tête)
    public void afficherDialogue(SpriteBatch batch, BitmapFont font, float deltaTime) {
        if (afficherDialogue) {
            // Réduire le temps restant d'affichage
            tempsAffichageDialogue -= deltaTime;

            // Si le temps d'affichage n'est pas écoulé, afficher le dialogue
            if (tempsAffichageDialogue > 0) {
                float posX = (getPosition().x * Carte.TAILLE_CASE) - 20; // Ajuster la position en fonction de la taille de l'entité
                float posY = (getPosition().y * Carte.TAILLE_CASE) + Carte.TAILLE_CASE + 10; // Juste au-dessus du PNJ

                // Afficher le dialogue en blanc
                font.setColor(Color.WHITE);
                font.draw(batch, dialogue, posX, posY);
            } else {
                // Si le temps est écoulé, ne plus afficher le dialogue
                afficherDialogue = false;
            }
        }
    }

    // Affichage des informations du PNJ
    public void afficherInfoPnj() {
        System.out.println("Nom du PNJ: " + nom);
        System.out.println("Dialogue: " + dialogue);
        System.out.println("Interagissable: " + (interagissable ? "Oui" : "Non"));
        System.out.println("Points de Vie: " + getPointsDeVie() + "/" + getPointsDeVieMax());
        System.out.println("Position Matrice: " + "[" + getMapX() + "][" + getMapY() + "]");
        System.out.println("Position réelle: " + "x " + getPosition().x + " y " + getPosition().y);
    }
}
