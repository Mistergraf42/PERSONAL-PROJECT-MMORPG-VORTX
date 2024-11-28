package com.mmorpg.vortx.entiter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mmorpg.vortx.Carte;

import static com.mmorpg.vortx.Carte.TAILLE_CASE;

public class Monstre extends Entiter {
    private int niveau;
    private String nomMonstre;
    private Direction directionActuelle = Direction.BAS;
    private float tempsDansDirection = 0;
    private float tempsPourChangerDirection = 2.5f; // Changement de direction toutes les 2.5 secondes
    private Vector2 positionInitiale; // Position de référence pour revenir en arrière

    // Animations pour chaque direction
    private Animation<TextureRegion> animationHaut;
    private Animation<TextureRegion> animationBas;
    private Animation<TextureRegion> animationGauche;
    private Animation<TextureRegion> animationDroite;

    public Monstre(int id, String nomMonstre, int pointsDeVieMax, int pointsDAttaque, int pointsDArmure, float x, float y, int niveau){
        super(id, pointsDeVieMax, pointsDAttaque, pointsDArmure, x, y);
        this.niveau = niveau;
        this.nomMonstre = nomMonstre;
        this.positionInitiale = new Vector2(x, y);
        this.hitbox = new Rectangle(x * TAILLE_CASE, y * TAILLE_CASE, LARGEUR_HITBOX, HAUTEUR_HITBOX); // Initialisation de la hitbox avec la position


        // Initialisation des animations
        this.animationHaut = animationHaut;
        this.animationBas = animationBas;
        this.animationGauche = animationGauche;
        this.animationDroite = animationDroite;
    }

    // Méthode pour afficher l'animation du monstre
    public void afficherAnimation(SpriteBatch batch, float elapsedTime,
                                  Animation<TextureRegion> animationHaut,
                                  Animation<TextureRegion> animationBas,
                                  Animation<TextureRegion> animationGauche,
                                  Animation<TextureRegion> animationDroite) {
        TextureRegion currentFrame;

        switch (directionActuelle) {
            case HAUT:
                currentFrame = animationHaut.getKeyFrame(elapsedTime, true);
                break;
            case BAS:
                currentFrame = animationBas.getKeyFrame(elapsedTime, true);
                break;
            case GAUCHE:
                currentFrame = animationGauche.getKeyFrame(elapsedTime, true);
                break;
            case DROITE:
                currentFrame = animationDroite.getKeyFrame(elapsedTime, true);
                break;
            default:
                currentFrame = animationBas.getKeyFrame(elapsedTime, true);
                break;
        }

        float posX = (getMapX() - 1) * Carte.TAILLE_CASE;
        float posY = (getMapY() - 1) * Carte.TAILLE_CASE;
        batch.draw(currentFrame, posX, posY, Carte.TAILLE_CASE * 2, Carte.TAILLE_CASE * 2);
    }


    // Getters et Setters
    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public String getNomMonstre() {
        return nomMonstre;
    }

    public void setNomMonstre(String nomMonstre) {
        this.nomMonstre = nomMonstre;
    }

    // Affichage des informations du monstre
    public void afficherInfoMonstre() {
        System.out.println("Nom du Monstre: " + nomMonstre);
        System.out.println("Niveau: " + niveau);
        System.out.println("Points de Vie: " + getPointsDeVie() + "/" + getPointsDeVieMax());
        System.out.println("Points d'Attaque: " + getPointsDAttaque());
        System.out.println("Points d'Armure: " + getPointsDArmure());
        System.out.println("Vitesse: " + getVitesse());
        System.out.println("Position Matrice: " + "[" + getMapX() + "][" + getMapY() + "]");
        System.out.println("Position reel: " + "x " + getPosition().x + " y " + getPosition().y);

    }
    public void seDeplacerVersJoueur(Joueur joueur, Carte carte, float delta) {
        tempsDansDirection += delta;

        // Calculer la distance entre le monstre et le joueur
        float distance = (float) Math.sqrt(Math.pow(joueur.getMapX() - this.getMapX(), 2) + Math.pow(joueur.getMapY() - this.getMapY(), 2));

        // Si le joueur est proche (distance <= 8), le monstre le suit
        if (distance <= 8) {
            float deltaX = 0, deltaY = 0;
            if (joueur.getMapX() > this.getMapX()) deltaX = 1;
            else if (joueur.getMapX() < this.getMapX()) deltaX = -1;
            if (joueur.getMapY() > this.getMapY()) deltaY = 1;
            else if (joueur.getMapY() < this.getMapY()) deltaY = -1;

            this.deplacer(deltaX * delta * this.getVitesse(), deltaY * delta * this.getVitesse(), carte);
        }
        // Sinon, il revient à sa position de départ
        else if (distance > 10 && tempsDansDirection >= tempsPourChangerDirection) {
            tempsDansDirection = 0;
            retournerPositionInitiale(carte, delta);
        }
    }

    private void retournerPositionInitiale(Carte carte, float delta) {
        float deltaX = 0, deltaY = 0;
        if (this.getMapX() < positionInitiale.x) deltaX = 1;
        else if (this.getMapX() > positionInitiale.x) deltaX = -1;
        if (this.getMapY() < positionInitiale.y) deltaY = 1;
        else if (this.getMapY() > positionInitiale.y) deltaY = -1;

        this.deplacer(deltaX * delta * this.getVitesse(), deltaY * delta * this.getVitesse(), carte);
    }

    public void afficherNomMonstre(SpriteBatch batch, BitmapFont font) {
        font.setColor(Color.RED);
        float posX = (getMapX() * Carte.TAILLE_CASE) - 50;
        float posY = (getMapY() + 1) * Carte.TAILLE_CASE +40;
        font.draw(batch, nomMonstre, posX, posY);
        font.setColor(Color.WHITE);
    }

    public void afficherBarreDeVie(ShapeRenderer shapeRenderer) {
        float largeurBarre = 50f;
        float hauteurBarre = 5f;
        if (getPointsDeVie() > 0) {
            float pourcentageVie = (float) getPointsDeVie() / getPointsDeVieMax();
            float posX = getPosition().x * Carte.TAILLE_CASE;
            float posY = getPosition().y * Carte.TAILLE_CASE + Carte.TAILLE_CASE;

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(posX - largeurBarre / 2, posY, largeurBarre, hauteurBarre);

            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(posX - largeurBarre / 2, posY, largeurBarre * pourcentageVie, hauteurBarre);
        }
    }


    public void seDeplacer(Joueur joueur, Carte carte, float delta) {
        tempsDansDirection += delta;

        // Calculer la distance entre le monstre et le joueur
        float distance = (float) Math.sqrt(Math.pow(joueur.getMapX() - this.getMapX(), 2) + Math.pow(joueur.getMapY() - this.getMapY(), 2));

        if (distance <= 8) { // Le joueur est proche, le monstre le suit
            float deltaX = 0, deltaY = 0;

            if (joueur.getMapX() > this.getMapX()) {
                deltaX = 1;
                directionActuelle = Direction.DROITE;
            } else if (joueur.getMapX() < this.getMapX()) {
                deltaX = -1;
                directionActuelle = Direction.GAUCHE;
            }

            if (joueur.getMapY() > this.getMapY()) {
                deltaY = 1;
                directionActuelle = Direction.HAUT;
            } else if (joueur.getMapY() < this.getMapY()) {
                deltaY = -1;
                directionActuelle = Direction.BAS;
            }

            this.deplacer(deltaX * delta * this.getVitesse(), deltaY * delta * this.getVitesse(), carte);
            mettreAJourHitbox();

        }
        else if (distance > 10) { // Joueur trop loin, le monstre revient à sa position initiale
            retournerPositionInitiale(carte, delta);
        }
        else { // Déplacement aléatoire si le joueur est dans une distance intermédiaire
            if (tempsDansDirection >= tempsPourChangerDirection) {
                changerDirectionAleatoire();
                tempsDansDirection = 0;
            }

            float deltaX = 0, deltaY = 0;
            switch (directionActuelle) {
                case HAUT:
                    deltaY = 1;
                    break;
                case BAS:
                    deltaY = -1;
                    break;
                case GAUCHE:
                    deltaX = -1;
                    break;
                case DROITE:
                    deltaX = 1;
                    break;
            }

            this.deplacer(deltaX * delta * this.getVitesse(), deltaY * delta * this.getVitesse(), carte);
            mettreAJourHitbox();

        }
    }

    private void changerDirectionAleatoire() {
        int randomDirection = (int) (Math.random() * 4);
        switch (randomDirection) {
            case 0:
                directionActuelle = Direction.HAUT;
                break;
            case 1:
                directionActuelle = Direction.BAS;
                break;
            case 2:
                directionActuelle = Direction.GAUCHE;
                break;
            case 3:
                directionActuelle = Direction.DROITE;
                break;
        }
    }


}

