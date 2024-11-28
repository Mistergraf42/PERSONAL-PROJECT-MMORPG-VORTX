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



import java.util.ArrayList;
import java.util.List;

import static com.mmorpg.vortx.Carte.TAILLE_CASE;

public class Boss extends Entiter {


    private String nomBoss;
    private int taille; // Taille du boss en nombre de cases (par exemple, 5 pour 5x5)
    private List<Vector2> casesOccupees; // Liste des cases occupées par le boss

    private Direction directionActuelle = Direction.DROITE; // Le boss commence par aller vers le haut
    private float tempsDansDirection = 0; // Temps passé à se déplacer dans la direction actuelle
    private float tempsPourChangerDirection = 3.0f; // Temps avant de changer de direction (en secondes)
    private float positionInitialeX;
    private float positionInitialeY;

    public Boss(int id,String nomBoss, int pointsDeVieMax, int pointsDAttaque, int pointsDArmure, float x, float y, int taille, Carte carte) {
        super(id,pointsDeVieMax, pointsDAttaque, pointsDArmure, x, y);
        this.nomBoss = nomBoss;
        this.taille = taille;
        this.casesOccupees = new ArrayList<>();
        this.positionInitialeX = x;
        this.positionInitialeY = y;

        if(getId()==2) {
            this.hitbox = new Rectangle((x * TAILLE_CASE)-(TAILLE_CASE*10), y * TAILLE_CASE, LARGEUR_HITBOX * 40, HAUTEUR_HITBOX * 10); // Initialisation de la hitbox avec la position
        }
        if(getId()==1) {
            this.hitbox = new Rectangle(x, y, 261/2f, 311/2f); // Initialisation de la hitbox avec la position
        }

        mettreAJourCasesOccupees();
    }

    public String getNomBoss() {
        return nomBoss;
    }


    // Met à jour les cases occupées par le boss
    private void mettreAJourCasesOccupees() {
        casesOccupees.clear();
        int demiTaille = taille / 2;
        for (int i = -demiTaille; i <= demiTaille; i++) {
            for (int j = -demiTaille; j <= demiTaille; j++) {
                casesOccupees.add(new Vector2(getMapX() + i, getMapY() + j));
            }
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        mettreAJourCasesOccupees(); // Mettre à jour les cases occupées lors du déplacement
    }

    // Méthode pour savoir si une compétence touche le boss
    public boolean estToucheParCompetence(Vector2 positionCompetence) {
        for (Vector2 caseOccupee : casesOccupees) {
            if (caseOccupee.equals(positionCompetence)) {
                return true; // La compétence touche le boss
            }
        }
        return false;
    }

    public void afficherInfoBoss() {
        System.out.println("Boss sur les cases: " + casesOccupees);
        System.out.println("Points de Vie: " + getPointsDeVie() + "/" + getPointsDeVieMax());
        System.out.println("matriceCentral [" + getMapX() + "]["+getMapY() + "]");
    }


//    // Fonction pour afficher un carré jaune autour du boss
//    public void afficherBoss(ShapeRenderer shapeRenderer) {
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.YELLOW); // Couleur jaune
//
//        for (Vector2 caseOccupee : casesOccupees) {
//            float posX = caseOccupee.x * Carte.TAILLE_CASE;
//            float posY = caseOccupee.y * Carte.TAILLE_CASE;
//            shapeRenderer.rect(posX, posY, Carte.TAILLE_CASE, Carte.TAILLE_CASE); // Dessine un carré pour chaque case occupée
//        }
//
//        shapeRenderer.end();
//    }


    public void afficherAnimation(SpriteBatch batch, Animation<TextureRegion> animationHaut,
                                  Animation<TextureRegion> animationBas,
                                  Animation<TextureRegion> animationGauche,
                                  Animation<TextureRegion> animationDroite,
                                  float elapsedTime, int nomBoss) {

        if (nomBoss == 2) {
            float animationX = (getMapX() - 12) * Carte.TAILLE_CASE;
            float animationY = (getMapY() - 2) * Carte.TAILLE_CASE;

            TextureRegion currentFrame = animationHaut.getKeyFrame(elapsedTime, true); // Utilise animationHaut pour nomBoss == 2
            batch.draw(currentFrame, animationX, animationY, 800, 387);
        } else {
            float animationX = (getMapX() - 2) * Carte.TAILLE_CASE;
            float animationY = (getMapY() - 2) * Carte.TAILLE_CASE;
            float largeurAnimation = 5 * Carte.TAILLE_CASE;
            float hauteurAnimation = 5 * Carte.TAILLE_CASE;

            // Choisir l'animation en fonction de la direction actuelle
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
                    currentFrame = animationHaut.getKeyFrame(elapsedTime, true); // Par défaut, on prend l'animation HAUT
                    break;
            }

            // Dessiner l'animation pour les directions
            batch.draw(currentFrame, animationX, animationY, largeurAnimation, hauteurAnimation);
        }
    }




    public void afficherNomBoss(SpriteBatch batch, BitmapFont font) {

        if (getId() == 2){

            font.setColor(Color.PURPLE); // Définir la couleur du texte (par exemple, rouge)

            // Position du nom (au-dessus du boss)
            float posX = (getMapX() * 32) - 85;
            float posY = (getMapY() + 8) * 32;

            // Dessiner le nom du boss
            font.draw(batch, nomBoss, posX, posY);
            font.setColor(Color.WHITE);
        }else {

            font.setColor(Color.BLUE); // Définir la couleur du texte (par exemple, rouge)

            // Position du nom (au-dessus du boss)
            float posX = (getMapX() * 32) - 20;
            float posY = (getMapY() + 4) * 32;

            // Dessiner le nom du boss
            font.draw(batch, nomBoss, posX, posY);
            font.setColor(Color.WHITE);
        }
    }

    @Override
    public void afficherBarreDeVie(ShapeRenderer shapeRenderer) {
        // Taille barre pour le boss (plus grande)
        float largeurBarre = 100f;
        float hauteurBarre = 10f;

        if (getId()==2) {
            if (getPointsDeVie() > 0) {
                // Pourcentage de vie restant
                float pourcentageVie = (float) getPointsDeVie() / getPointsDeVieMax();

                float posX = (getMapX() * TAILLE_CASE); // Position centrale du boss
                float posY = (getMapY() * TAILLE_CASE) + TAILLE_CASE * 7f; // Position plus haute

                // Fond rouge (barre de vie vide)
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.rect(posX - largeurBarre / 2, posY, largeurBarre, hauteurBarre);

                // HP en vert (barre de vie restante)
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.rect(posX - largeurBarre / 2, posY, largeurBarre * pourcentageVie, hauteurBarre);
            }

        } else {
            if (getPointsDeVie() > 0) {
                // Pourcentage de vie restant
                float pourcentageVie = (float) getPointsDeVie() / getPointsDeVieMax();

                float posX = (getMapX() * TAILLE_CASE); // Position centrale du boss
                float posY = (getMapY() * TAILLE_CASE) + TAILLE_CASE * 2.5f; // Position plus haute

                // Fond rouge (barre de vie vide)
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.rect(posX - largeurBarre / 2, posY, largeurBarre, hauteurBarre);

                // HP en vert (barre de vie restante)
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.rect(posX - largeurBarre / 2, posY, largeurBarre * pourcentageVie, hauteurBarre);
            }
        }
    }


    public void seDeplacer(Joueur joueur, Carte carte, float delta, GestionnaireEntites gestionnaireEntites, SpriteBatch batch, Animation<TextureRegion> animation, Animation<TextureRegion> animationM, float elapsedTime) {
        // Incrémenter le temps passé dans la direction actuelle
        tempsDansDirection += delta;

        // Calculer la distance entre le boss et le joueur
        float distance = (float) Math.sqrt(Math.pow(joueur.getMapX() - this.getMapX(), 2) + Math.pow(joueur.getMapY() - this.getMapY(), 2));

        // Si le joueur est proche (<= 10), le boss le poursuit
        if (distance <= 10) {
            float deltaX = 0;
            float deltaY = 0;

            if (joueur.getMapX() > this.getMapX()) {
                deltaX = 1;
            } else if (joueur.getMapX() < this.getMapX()) {
                deltaX = -1;
            }

            if (joueur.getMapY() > this.getMapY()) {
                deltaY = 1;
            } else if (joueur.getMapY() < this.getMapY()) {
                deltaY = -1;
            }

            this.deplacer(deltaX * delta * this.getVitesse(), deltaY * delta * this.getVitesse(), carte);

            // Si le joueur est hors de portée (par exemple, > 15), le boss retourne à sa position initiale
        } else if (distance > 15) {
            float deltaX = 0;
            float deltaY = 0;

            if (this.getMapX() < positionInitialeX) {
                deltaX = 1;
            } else if (this.getMapX() > positionInitialeX) {
                deltaX = -1;
            }

            if (this.getMapY() < positionInitialeY) {
                deltaY = 1;
            } else if (this.getMapY() > positionInitialeY) {
                deltaY = -1;
            }

            // Si le boss est déjà à sa position initiale, il reste sur place
            if (deltaX == 0 && deltaY == 0) {
                // Le boss est revenu à sa position de départ
            } else {
                this.deplacer(deltaX * delta * this.getVitesse(), deltaY * delta * this.getVitesse(), carte);
            }
        } else {
            // Si le joueur est dans une zone moyenne, le boss continue dans sa direction actuelle
            if (tempsDansDirection >= tempsPourChangerDirection) {
                changerDirection();
                tempsDansDirection = 0;
            }

            float deltaX = 0;
            float deltaY = 0;

            switch (directionActuelle) {
                case HAUT:
                    deltaY = 1;
                    break;
                case DROITE:
                    deltaX = 1;
                    break;
                case BAS:
                    deltaY = -1;
                    break;
                case GAUCHE:
                    deltaX = -1;
                    break;
            }

            this.deplacer(deltaX * delta * this.getVitesse(), deltaY * delta * this.getVitesse(), carte);
        }

        // Le boss utilise sa compétence si le joueur est à portée
        if (distance <= 10) {
            // Logique pour activer la compétence du boss
        }
    }

    private void changerDirection() {
        // Passer à la direction suivante dans l'ordre : HAUT -> DROITE -> BAS -> GAUCHE -> HAUT...
        switch (directionActuelle) {
            case HAUT:
                directionActuelle = Direction.DROITE;
                break;
            case DROITE:
                directionActuelle = Direction.BAS;
                break;
            case BAS:
                directionActuelle = Direction.GAUCHE;
                break;
            case GAUCHE:
                directionActuelle = Direction.HAUT;
                break;
        }
      //  System.out.println("Le boss change de direction : " + directionActuelle);
    }





    public void afficherBoss(ShapeRenderer shapeRenderer) {
        // Définir la couleur violette pour les cases occupées par le boss
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.PURPLE);

        // Parcourir les cases occupées par le boss et dessiner un carré violet sur chaque case
        for (Vector2 caseOccupee : casesOccupees) {
            float posX = caseOccupee.x * Carte.TAILLE_CASE;
            float posY = caseOccupee.y * Carte.TAILLE_CASE;

            // Dessiner un carré sur chaque case occupée par le boss
            shapeRenderer.rect(posX, posY, Carte.TAILLE_CASE, Carte.TAILLE_CASE);
        }

        shapeRenderer.end();
    }


    @Override
    public void mettreAJourHitbox() {
        if (this.getId()==1) {
            this.hitbox.setPosition(position.x * TAILLE_CASE - 50, position.y * TAILLE_CASE-70);
        } else {
            this.hitbox.setPosition(position.x * TAILLE_CASE, position.y * TAILLE_CASE);
        }
    }



    @Override
    public void deplacer(float deltaX, float deltaY, Carte carte) {
        if (isEnVie()) {
            // Calcul des nouvelles positions en pixels
            float nouvellePositionX = this.position.x + deltaX * getVitesse();
            float nouvellePositionY = this.position.y + deltaY * getVitesse();

            // Configuration de la hitbox temporaire en fonction de l'ID du boss
            Rectangle hitboxTemp;
            switch (getId()) {
                case 1:
                    hitboxTemp = new Rectangle(
                        (nouvellePositionX * TAILLE_CASE) - (261 / 4f), // Ajustements spécifiques à l'ID 1
                        (nouvellePositionY * TAILLE_CASE) - (311 / 4f),
                        261 / 2f,
                        311 / 2f
                    );
                    break;

                case 2:
                    hitboxTemp = new Rectangle(
                        (nouvellePositionX * TAILLE_CASE) - (TAILLE_CASE * 10),
                        nouvellePositionY * TAILLE_CASE,
                        LARGEUR_HITBOX * 40,
                        HAUTEUR_HITBOX * 10
                    );
                    break;

                default:
                    // Par défaut, on utilise la hitbox normale d'une entité
                    hitboxTemp = new Rectangle(
                        (nouvellePositionX * TAILLE_CASE) - (LARGEUR_HITBOX / 2),
                        (nouvellePositionY * TAILLE_CASE) - LARGEUR_HITBOX,
                        hitbox.width,
                        hitbox.height
                    );
                    break;
            }

            // Logique de détection de collision avec les obstacles
            boolean collisionDetectee = false;
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

            // Si pas de collision, on déplace le boss
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





}



