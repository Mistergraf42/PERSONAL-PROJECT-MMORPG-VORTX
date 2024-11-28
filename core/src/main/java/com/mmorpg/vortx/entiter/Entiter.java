package com.mmorpg.vortx.entiter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mmorpg.vortx.Carte;

import static com.mmorpg.vortx.Carte.TAILLE_CASE;

public class Entiter {


    private int id;
    private int pointsDeVie;
    private int pointsDeVieMax;
    protected Vector2 position; // Position en float
    int mapX;
    int mapY; // Position matricielle
    private int pointsDAttaque;
    private int pointsDArmure;
    private float vitesse; // Vitesse de l'entité
    private boolean enVie;
    protected Rectangle hitbox;
    public static final int LARGEUR_HITBOX = TAILLE_CASE/2;
    public static final int HAUTEUR_HITBOX = TAILLE_CASE-2;



    // Constructeur
    public Entiter(int id, int pointsDeVieMax, int pointsDAttaque, int pointsDArmure, float x, float y) {
        this.id = id;
        this.pointsDeVieMax = pointsDeVieMax;
        this.pointsDeVie = pointsDeVieMax; // Commence avec la vie max
        this.pointsDAttaque = pointsDAttaque;
        this.pointsDArmure = pointsDArmure;
        this.vitesse = 1;
        this.position = new Vector2(x, y);
        this.mapX = (int) x; // Calcul de la position dans la matrice
        this.mapY = (int) y;
        this.enVie = true; // L'entité commence vivante
        this.hitbox = new Rectangle(x * TAILLE_CASE, y * TAILLE_CASE, LARGEUR_HITBOX, HAUTEUR_HITBOX); // Initialisation de la hitbox avec la position
    }
    public int getId() {
        return id;
    }

    public void mettreAJourHitbox() {
        this.hitbox.setPosition(position.x * TAILLE_CASE, position.y * TAILLE_CASE); // Mise à jour avec la position en pixels
    }



    // Getters et Setters
    public int getPointsDeVie() {
        return pointsDeVie;
    }

    public void setPointsDeVie(int pointsDeVie) {
        this.pointsDeVie = Math.max(0, Math.min(pointsDeVie, pointsDeVieMax)); // Assure que les points de vie restent dans les limites
        if (this.pointsDeVie == 0) {
            enVie = false; // L'entité meurt si les points de vie tombent à 0
        }
    }

    public int getPointsDeVieMax() {
        return pointsDeVieMax;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
        // Mise à jour des coordonnées matricielles (en cases)
        this.mapX = (int) (x / Carte.TAILLE_CASE);
        this.mapY = (int) (y / Carte.TAILLE_CASE);
        mettreAJourHitbox(); // Mettre à jour la hitbox lorsque la position est modifiée
    }


    public void setPositionMatrice(float x, float y) {
        this.position.set(x, y);
        this.mapX = (int) x;
        this.mapY = (int) y;
        mettreAJourHitbox(); // Mise à jour de la hitbox après modification de la position matricielle
    }

    public int getMapX() {
        return mapX;
    }

    public int getMapY() {
        return mapY;
    }

    public int getPointsDAttaque() {
        return pointsDAttaque;
    }

    public void setPointsDAttaque(int pointsDAttaque) {
        this.pointsDAttaque = pointsDAttaque;
    }

    public int getPointsDArmure() {
        return pointsDArmure;
    }

    public void setPointsDArmure(int pointsDArmure) {
        this.pointsDArmure = pointsDArmure;
    }

    public float getVitesse() {
        return vitesse;
    }

    public void setVitesse(float vitesse) {
        this.vitesse = vitesse;
    }

    public boolean isEnVie() {
        return enVie;
    }

    // Méthode pour infliger des dégâts à l'entité
    public void recevoirDegats(int degats) {
        int degatsReduits = degats - pointsDArmure;
        if (degatsReduits < 0) {
            degatsReduits = 0;
        }
        setPointsDeVie(pointsDeVie - degatsReduits);
    }

    // Méthode pour déplacer l'entité
    public void deplacer(float deltaX, float deltaY, Carte carte) {
        if (enVie) {
            // Calcul des nouvelles positions en pixels
            float nouvellePositionX = this.position.x + deltaX * vitesse;
            float nouvellePositionY = this.position.y + deltaY * vitesse;

            // Mise à jour temporaire de la hitbox pour vérifier une collision
            Rectangle hitboxTemp = new Rectangle(
                (nouvellePositionX * TAILLE_CASE) - (LARGEUR_HITBOX / 2),
                (nouvellePositionY * TAILLE_CASE) - LARGEUR_HITBOX,
                hitbox.width,
                hitbox.height
            );

            boolean collisionDetectee = false;

            // Vérifie la collision avec les obstacles
            for (Rectangle obstacle : carte.obstacles) {
                if (hitboxTemp.overlaps(obstacle)) {
                    collisionDetectee = true;
                    // Ajuste la position en fonction de la collision
                    if (deltaX > 0) { // Collision à droite
                        nouvellePositionX = obstacle.x / TAILLE_CASE - hitbox.width / TAILLE_CASE;
                    } else if (deltaX < 0) { // Collision à gauche
                        nouvellePositionX = (obstacle.x + obstacle.width) / TAILLE_CASE;
                    }

                    if (deltaY > 0) { // Collision en haut
                        nouvellePositionY = obstacle.y / TAILLE_CASE - hitbox.height / TAILLE_CASE;
                    } else if (deltaY < 0) { // Collision en bas
                        nouvellePositionY = (obstacle.y + obstacle.height) / TAILLE_CASE;
                    }
                    break;
                }
            }

            // Si pas de collision, on déplace l'entité
            if (!collisionDetectee) {
                this.position.set(nouvellePositionX, nouvellePositionY);
            }

            // Mise à jour de la hitbox après déplacement
            mettreAJourHitbox();

            // Mettre à jour les coordonnées matricielles
            this.mapX = (int) this.position.x;
            this.mapY = (int) this.position.y;
        }
    }


    public void afficherBarreDeVie(ShapeRenderer shapeRenderer) {
        // Taille barre
        float largeurBarre = 40f;
        float hauteurBarre = 5f;

        if (pointsDeVie > 0) {
            // Pourcentage de vie restant
            float pourcentageVie = (float) pointsDeVie / pointsDeVieMax;

            float posX = (position.x * TAILLE_CASE) ;
            float posY = (position.y * TAILLE_CASE) + TAILLE_CASE + 5-LARGEUR_HITBOX; // Juste au-dessus de l'entité

            // Fond rouge
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(posX - largeurBarre / 2, posY, largeurBarre, hauteurBarre);

            // HP en vert
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(posX - largeurBarre / 2, posY, largeurBarre * pourcentageVie, hauteurBarre);
        }
    }

    public boolean verifierCollisionAvecObstacles(Carte carte) {
        for (Rectangle obstacle : carte.obstacles) {
            if (this.hitbox.overlaps(obstacle)) {
                return true; // Collision détectée
            }
        }
        return false; // Pas de collision
    }

    public void afficherHitbox(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); // Démarre le mode rempli
        shapeRenderer.setColor(Color.RED); // Définit la couleur rouge pour la hitbox

        // Dessine la hitbox du joueur
        shapeRenderer.rect(hitbox.x-((float) LARGEUR_HITBOX /2), hitbox.y-((float) HAUTEUR_HITBOX /2), hitbox.width, hitbox.height); // dessin position
        shapeRenderer.setColor(Color.BLACK); // Définit la couleur rouge pour la hitbox

        shapeRenderer.circle(hitbox.x, hitbox.y,1);

        shapeRenderer.end(); // Termine le mode de dessin
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

}
