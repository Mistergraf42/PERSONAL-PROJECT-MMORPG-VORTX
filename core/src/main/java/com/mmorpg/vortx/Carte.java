package com.mmorpg.vortx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Carte {
    public static final int TAILLE_CASE = 32;
    public static final int LARGEUR_CARTE = 51;
    public static final int HAUTEUR_CARTE = 51;

    // Types de terrain
    public static final int HERBE = 1;
    public static final int SABLE = 2;
    public static final int NEIGE = 3;
    public static final int PAVE = 4;
    public static final int CHEMIN = 5;
    public static final int CHEMIN_VILLE = 6;

    // Obstacles
    public static final int ARBRE = 7;
    public static final int MONTAGNE = 8;
    public static final int EAU = 9;
    public static final int GLACIER = 10;
    public static final int ROCHE = 11;

    // Sol avec debuffs
    public static final int LAVE = 12;
    public static final int CACTUS = 13;
    public static final int SABLE_MOUVANT = 14;


    //Sol téléporteur
    public static final int TELEPORTEUR = 15;
    public List<Rectangle> obstacles;



    // La matrice de la carte
    private int[][] matrice;

    // Constructeur
    public Carte() {
        matrice = new int[LARGEUR_CARTE][HAUTEUR_CARTE];
        this.obstacles = new ArrayList<>();
        genererCarte(); // Méthode pour initialiser la carte
    }

    // Génération initiale de la carte
//    private void genererCarte() {
//        for (int x = 0; x < LARGEUR_CARTE; x++) {
//            for (int y = 0; y < HAUTEUR_CARTE; y++) {
//                matrice[x][y] = HERBE;
//                matrice[8][5]=MONTAGNE;
//                matrice[8][6]=MONTAGNE;
//                matrice[8][7]=MONTAGNE;
//                matrice[8][8]=MONTAGNE;
//                matrice[8][9]=MONTAGNE;
//                matrice[8][10]=MONTAGNE;
//
//
//                matrice[25][25]=MONTAGNE;
//                matrice[26][25]=MONTAGNE;
//                matrice[24][25]=MONTAGNE;
//                matrice[27][25]=MONTAGNE;
//                matrice[23][25]=MONTAGNE;
//
//                matrice[25][26]=MONTAGNE;
//                matrice[26][26]=MONTAGNE;
//                matrice[24][26]=MONTAGNE;
//                matrice[27][26]=MONTAGNE;
//                matrice[23][26]=MONTAGNE;
//
//                matrice[25][27]=MONTAGNE;
//                matrice[26][27]=MONTAGNE;
//                matrice[24][27]=MONTAGNE;
//                matrice[27][27]=MONTAGNE;
//                matrice[23][27]=MONTAGNE;
//
//
//
//
//                // Si c'est un obstacle (ex: montagne, roche), ajouter un rectangle dans la liste
//                if (matrice[x][y] == ARBRE || matrice[x][y] == MONTAGNE || matrice[x][y] == ROCHE || matrice[x][y] == EAU || matrice[x][y] == GLACIER) {
//                    obstacles.add(new Rectangle(x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE));                }
//            }
//        }
//    }



    private void genererCarte() {
        // Initialisation : remplissage de base en herbe
        for (int x = 0; x < LARGEUR_CARTE; x++) {
            for (int y = 0; y < HAUTEUR_CARTE; y++) {
                matrice[x][y] = HERBE;
            }
        }

        // Routes de ville (croix centrale de 3 cases de largeur)
        for (int x = 0; x < LARGEUR_CARTE; x++) {
            for (int y = HAUTEUR_CARTE / 2 - 1; y <= HAUTEUR_CARTE / 2 + 1; y++) {
                matrice[x][y] = CHEMIN_VILLE; // Route horizontale
            }
        }
        for (int y = 0; y < HAUTEUR_CARTE; y++) {
            for (int x = LARGEUR_CARTE / 2 - 1; x <= LARGEUR_CARTE / 2 + 1; x++) {
                matrice[x][y] = CHEMIN_VILLE; // Route verticale
            }
        }

        // Biome de sable dans le coin inférieur gauche
        for (int x = 0; x < LARGEUR_CARTE / 3; x++) {
            for (int y = 0; y < HAUTEUR_CARTE / 3; y++) {
                matrice[x][y] = SABLE;
                // Ajout de cactus et sables mouvants aléatoirement
                if (Math.random() > 0.8) {
                    matrice[x][y] = (Math.random() > 0.5) ? CACTUS : SABLE_MOUVANT;
                    obstacles.add(new Rectangle(x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE));
                }
            }
        }

        // Biome de neige en haut à droite avec petits lacs et glaciers
        for (int x = LARGEUR_CARTE * 2 / 3; x < LARGEUR_CARTE; x++) {
            for (int y = HAUTEUR_CARTE * 2 / 3; y < HAUTEUR_CARTE; y++) {
                matrice[x][y] = NEIGE;
                // Ajouter des glaciers et des lacs de neige
                if (Math.random() > 0.7) {
                    matrice[x][y] = GLACIER;
                    obstacles.add(new Rectangle(x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE));
                } else if (Math.random() > 0.9) {
                    matrice[x][y] = EAU; // Petit lac dans la neige
                    obstacles.add(new Rectangle(x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE));
                }
            }
        }

        // Zone volcanique en bas à droite
        for (int x = LARGEUR_CARTE * 2 / 3; x < LARGEUR_CARTE; x++) {
            for (int y = 0; y < HAUTEUR_CARTE / 4; y++) {
                matrice[x][y] = LAVE;
                if (Math.random() > 0.8) {
                    matrice[x][y] = ROCHE; // Quelques roches dans la zone de lave
                }
                obstacles.add(new Rectangle(x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE));
            }
        }

        // Zone rocheuse avec montagnes au centre
        for (int x = LARGEUR_CARTE / 3; x < LARGEUR_CARTE * 2 / 3; x++) {
            for (int y = HAUTEUR_CARTE / 3; y < HAUTEUR_CARTE * 2 / 3; y++) {
                if (Math.random() > 0.6) {
                    matrice[x][y] = MONTAGNE;
                    obstacles.add(new Rectangle(x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE));
                } else if (Math.random() > 0.7) {
                    matrice[x][y] = ROCHE;
                }
            }
        }

        // Création d'une rivière sinueuse traversant la carte
        int riverStartX = 0;
        int riverStartY = (int) (Math.random() * (HAUTEUR_CARTE / 2) + HAUTEUR_CARTE / 4);
        for (int i = 0; i < LARGEUR_CARTE; i++) {
            matrice[riverStartX + i][riverStartY] = EAU;
            matrice[riverStartX + i][riverStartY + 1] = EAU;
            obstacles.add(new Rectangle((riverStartX + i) * TAILLE_CASE, riverStartY * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE));
            obstacles.add(new Rectangle((riverStartX + i) * TAILLE_CASE, (riverStartY + 1) * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE));

            // Ajustement de la rivière pour qu'elle soit sinueuse
            if (Math.random() > 0.5) riverStartY += (Math.random() > 0.5) ? 1 : -1;
            riverStartY = Math.max(1, Math.min(HAUTEUR_CARTE - 3, riverStartY));
        }

        // Ajouter un pont d'herbe au-dessus de la rivière sur la route centrale
        for (int y = riverStartY; y <= riverStartY + 1; y++) {
            for (int x = LARGEUR_CARTE / 2 - 1; x <= LARGEUR_CARTE / 2 + 1; x++) {
                matrice[x][y] = HERBE;
            }
        }

        // Marquer tous les obstacles pour éviter les collisions
        for (int x = 0; x < LARGEUR_CARTE; x++) {
            for (int y = 0; y < HAUTEUR_CARTE; y++) {
                if (matrice[x][y] == MONTAGNE || matrice[x][y] == CACTUS || matrice[x][y] == SABLE_MOUVANT ||
                    matrice[x][y] == GLACIER || matrice[x][y] == EAU || matrice[x][y] == LAVE) {
                    obstacles.add(new Rectangle(x * TAILLE_CASE, y * TAILLE_CASE, TAILLE_CASE, TAILLE_CASE));
                }
            }
        }
    }


    // Vérifie si une case est accessible
    public boolean estAccessible(int x, int y) {
        int type = matrice[x][y];
        if (type==15){
            return true;
        }
        if (type < 7) {
            return true;
        }
        if(type>11 && type<15) {
            return false;
        }
        return false;
    }

    // Obtient le type de sol pour une case donnée
    public int getTypeSol(int x, int y) {
        return matrice[x][y];
    }

    // Modifie le type de sol à une position donnée
    public void setTypeSol(int x, int y, int type) {
        matrice[x][y] = type;
    }

    // Affichage basique de la carte dans la console
    public void afficherCarte() {
        for (int y = 0; y < HAUTEUR_CARTE; y++) {
            for (int x = 0; x < LARGEUR_CARTE; x++) {
                System.out.print(matrice[x][y] + " ");
            }
            System.out.println();
        }
    }



    // Méthode pour afficher graphiquement la carte
    public void renderCarte(SpriteBatch batch, GameScreen gameScreen) {
        for (int y = 0; y < HAUTEUR_CARTE; y++) {
            for (int x = 0; x < LARGEUR_CARTE; x++) {
                int type = matrice[x][y];
                Texture texture = gameScreen.getTextureForType(type);
                batch.draw(texture, x * TAILLE_CASE, y * TAILLE_CASE,32,32);
            }
        }
    }

    public void afficherObstacles(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); // Démarre le mode rempli pour dessiner des rectangles pleins
        shapeRenderer.setColor(Color.PINK); // Définit la couleur rose

        // Dessine chaque obstacle sous forme de rectangle rose
        for (Rectangle obstacle : obstacles) {
            shapeRenderer.rect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        shapeRenderer.end(); // Termine le mode de dessin
    }

    public List<Rectangle> getObstacles() {
        return obstacles;
    }


}

