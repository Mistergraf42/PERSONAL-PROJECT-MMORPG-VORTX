package com.mmorpg.vortx.entiter;

//affichage pseudo
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mmorpg.vortx.Carte;
import com.mmorpg.vortx.equipement.Equipement;
import com.mmorpg.vortx.equipement.Item;

import java.util.ArrayList;
import java.util.List;

import static com.mmorpg.vortx.Carte.TAILLE_CASE;



public class Joueur extends Entiter {
    public static final int IDE = 0;
    public static final int HAUT = 1;
    public static final int BAS = 2;
    public static final int GAUCHE = 3;
    public static final int DROITE = 4;

    public static final int HD = 5;
    public static final int HG = 6;
    public static final int BD = 7;
    public static final int BG = 8;

    private String pseudo;
    private String guilde;
    private int niveau;
    private float xp;
    private int classe;
    private int direction;
    private List<Item> inventaire;
    private List<Equipement> equipements;  // Liste des équipements équipés


    private Equipement casque;
    private Equipement armure;
    private Equipement arme;
    private Equipement bouclier;
    private Equipement bottes;

    private int attaqueTotale;  // Attribut pour l'attaque totale du joueur
    private int armureTotale;   // Attribut pour l'armure totale du joueur
    private int argent;


    // Constructeur
    public Joueur(int id,String pseudo, int pointsDeVieMax, int pointsDAttaque, int pointsDArmure, float x, float y) {
        super(id,pointsDeVieMax, pointsDAttaque, pointsDArmure, x, y);

        this.pseudo = pseudo;
        this.guilde = "Aucune"; // Guilde par défaut
        this.niveau = 60; // Niveau initial
        this.xp = 0.0f; // XP initial
        this.classe = 0; // Classe par défaut
        this.direction = 0;
        this.inventaire = new ArrayList<>(); // Créer un inventaire vide
        this.argent = 100;
        this.equipements = new ArrayList<>();
        this.casque = null;
        this.armure = null;
        this.arme = null;
        this.bouclier = null;
        this.bottes = null;


        calculerAttaqueTotale();  // Initialiser l'attaque totale
        calculerArmureTotale();   // Initialiser l'armure totale
    }

    public void setXp(float xp) {
        this.xp = xp;
        if (this.xp>100){
            this.xp=0;
            this.niveau=niveau+1;
        }
    }


    // Getters et Setters
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getattaqueTotale(){return attaqueTotale;}
    public int getarmureTotale(){return armureTotale;}


    public String getGuilde() {
        return guilde;
    }

    public void setGuilde(String guilde) {
        this.guilde = guilde;
    }

    public int getNiveau() {
        return niveau;
    }

    public float getXp() {
        return xp;
    }

    public void ajouterXp(float xpGagne) {
        this.xp += xpGagne;
        while (this.xp >= 100) {
            this.xp -= 100; // Réinitialise l'XP pour le niveau suivant
            niveau++; // Incrémente le niveau
        }
    }

    public int getClasse() {
        return classe;
    }

    public void setClasse(int classe) {
        this.classe = classe;
    }

    // Affichage des informations du joueur
    public void afficherInfoJoueur() {
        System.out.println("Pseudo: " + pseudo);
        System.out.println("Guilde: " + guilde);
        System.out.println("Niveau: " + niveau);
        System.out.println("XP: " + xp + "/100");
        System.out.println("Classe: " + classe);
        System.out.println("Position Matrice: " + "[" + getMapX() + "][" + getMapY() + "]");
        System.out.println("Position reel: " + "x " + getPosition().x + " y " + getPosition().y);



    }


    public void afficherPseudo(SpriteBatch batch, BitmapFont font) {
        // Position de l'affichage du pseudo
        float posX = (position.x * TAILLE_CASE)-19; // Ajuste la position en fonction de la taille de l'entité
        float posY = (position.y * TAILLE_CASE) + TAILLE_CASE + 8; // Juste au-dessus de l'entité, au-dessus de la barre de vie

        // Définir la couleur du texte (par exemple, blanc)
        font.setColor(Color.WHITE);

        // Afficher le pseudo
        font.draw(batch, pseudo, posX, posY);
    }


    public int getArgent() {
        return argent;
    }

    public void setArgent(int argent) {
        this.argent = argent;
    }



    public void verifierTeleportation(Carte carte, int destinationX, int destinationY) {
        // Vérifie si le joueur est sur une case de type 15
        int typeSol = carte.getTypeSol(this.getMapX(), this.getMapY());

        if (typeSol == 15) {
            // Téléporte le joueur à la destination choisie
            this.setPosition(destinationX * Carte.TAILLE_CASE, destinationY * Carte.TAILLE_CASE);
            this.setPositionMatrice(destinationX, destinationY);
            System.out.println("Le joueur a été téléporté à [" + destinationX + "][" + destinationY + "]");
        }
    }

    public int getDirection() {
        return direction;
    }

    public int gererDeplacement(float delta, Carte carte,
                                Animation<TextureRegion> walkAnimationUp,
                                Animation<TextureRegion> walkAnimationDown,
                                Animation<TextureRegion> walkAnimationLeft,
                                Animation<TextureRegion> walkAnimationRight,
                                SpriteBatch batch, Texture rocheTexture, Texture joueurTexture,
                                float TILE_SIZE, boolean moving, boolean CurrentMoving,
                                float elapsedTime) {
        float deltaX = 0;
        float deltaY = 0;

        // Vérifier les entrées utilisateur pour les mouvements
        boolean haut = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean bas = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean gauche = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean droite = Gdx.input.isKeyPressed(Input.Keys.RIGHT);

        // Calcul du delta de déplacement en fonction des touches appuyées
        if (haut) {
            deltaY += delta;
            CurrentMoving = true;
        }
        if (bas) {
            deltaY -= delta;
            CurrentMoving = true;
        }
        if (gauche) {
            deltaX -= delta;
            CurrentMoving = true;
        }
        if (droite) {
            deltaX += delta;
            CurrentMoving = true;
        }

        // Initialiser la direction comme "sans mouvement"
        int directionResult = IDE;

        // Déterminer la direction (les diagonales ont la priorité)
        if (haut && droite) {
            directionResult = HD; // Diagonale en haut à droite
        } else if (bas && droite) {
            directionResult = BD; // Diagonale en bas à droite
        } else if (haut && gauche) {
            directionResult = HG; // Diagonale en haut à gauche
        } else if (bas && gauche) {
            directionResult = BG; // Diagonale en bas à gauche
        } else if (haut) {
            directionResult = HAUT;
        } else if (bas) {
            directionResult = BAS;
        } else if (gauche) {
            directionResult = GAUCHE;
        } else if (droite) {
            directionResult = DROITE;
        }

        //System.out.println("Direction: " + directionResult);

        // Si le joueur se déplace, applique le déplacement et gère l'animation
        if (deltaX != 0 || deltaY != 0) {
            deplacer(deltaX, deltaY, carte); // Appliquer le déplacement en fonction de deltaX et deltaY
            moving = true;

            // Choisir l'animation en fonction de la direction finale
            switch (directionResult) {
                case HAUT:
                    batch.draw(walkAnimationUp.getKeyFrame(elapsedTime, true), ((getPosition().x * TILE_SIZE)-LARGEUR_HITBOX), ((getPosition().y * TILE_SIZE)-((float) HAUTEUR_HITBOX /2)));
                    break;
                case BAS:
                    batch.draw(walkAnimationDown.getKeyFrame(elapsedTime, true), ((getPosition().x * TILE_SIZE)-LARGEUR_HITBOX), ((getPosition().y * TILE_SIZE)-((float) HAUTEUR_HITBOX /2)));
                    break;
                case GAUCHE:
                    batch.draw(walkAnimationLeft.getKeyFrame(elapsedTime, true), ((getPosition().x * TILE_SIZE)-LARGEUR_HITBOX), ((getPosition().y * TILE_SIZE)-((float) HAUTEUR_HITBOX /2)));
                    break;
                case DROITE:
                    batch.draw(walkAnimationRight.getKeyFrame(elapsedTime, true), ((getPosition().x * TILE_SIZE)-LARGEUR_HITBOX), ((getPosition().y * TILE_SIZE)-((float) HAUTEUR_HITBOX /2)));
                    break;
                case HD: // Diagonale haut droite
                    batch.draw(walkAnimationRight.getKeyFrame(elapsedTime, true), ((getPosition().x * TILE_SIZE)-LARGEUR_HITBOX), ((getPosition().y * TILE_SIZE)-((float) HAUTEUR_HITBOX /2))); // Placeholder
                    break;
                case BD: // Diagonale bas droite
                    batch.draw(walkAnimationRight.getKeyFrame(elapsedTime, true), ((getPosition().x * TILE_SIZE)-LARGEUR_HITBOX), ((getPosition().y * TILE_SIZE)-((float) HAUTEUR_HITBOX /2))); // Placeholder
                    break;
                case HG: // Diagonale haut gauche
                    batch.draw(walkAnimationLeft.getKeyFrame(elapsedTime, true), ((getPosition().x * TILE_SIZE)-LARGEUR_HITBOX), ((getPosition().y * TILE_SIZE)-((float) HAUTEUR_HITBOX /2))); // Placeholder
                    break;
                case BG: // Diagonale bas gauche
                    batch.draw(walkAnimationLeft.getKeyFrame(elapsedTime, true), ((getPosition().x * TILE_SIZE)-LARGEUR_HITBOX), ((getPosition().y * TILE_SIZE)-((float) HAUTEUR_HITBOX /2))); // Placeholder
                    break;
            }
        } else {
            CurrentMoving = false;
            directionResult = IDE; // Aucun mouvement
        }

        // Si le joueur ne bouge pas, dessiner la texture statique
        if (!moving && !CurrentMoving) {
            batch.draw(joueurTexture, ((getPosition().x * TILE_SIZE)-LARGEUR_HITBOX), ((getPosition().y * TILE_SIZE)-((float) HAUTEUR_HITBOX /2)), TILE_SIZE, TILE_SIZE);
        }

        return directionResult; // Retourner la direction finale
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


    //--------------------------------------------------------------------------
    //
    //                      EQUIPEMENT
    //
    //---------------------------------------------------------------------------

    // Méthode pour ajouter un objet à l'inventaire
    public void ajouterItem(Item item) {
        inventaire.add(item);
        System.out.println(item.getNom() + " ajouté à l'inventaire.");
    }

    // Méthode pour équiper un objet
    public void equiper(Equipement equipement) {
        // Retirer l'objet de l'inventaire s'il est dans l'inventaire
        inventaire.remove(equipement);

        // Vérifier quel type d'équipement est ajouté et déséquiper l'ancien s'il y en a un
        switch (equipement.getTypeEquipement()) {
            case CASQUE:
                if (casque != null) {
                    // Si un casque est déjà équipé, on le remet dans l'inventaire
                    inventaire.add(casque);
                }
                casque = equipement;
                break;
            case ARMURE:
                if (armure != null) {
                    // Si une armure est déjà équipée, on la remet dans l'inventaire
                    inventaire.add(armure);
                }
                this.armure = equipement;
                break;
            case ARME:
                if (arme != null) {
                    // Si une arme est déjà équipée, on la remet dans l'inventaire
                    inventaire.add(arme);
                }
                arme = equipement;
                break;
            case BOUCLIER:
                if (bouclier != null) {
                    // Si un bouclier est déjà équipé, on le remet dans l'inventaire
                    inventaire.add(bouclier);
                }
                bouclier = equipement;
                break;
            case BOTTES:
                if (bottes != null) {
                    // Si des bottes sont déjà équipées, on les remet dans l'inventaire
                    inventaire.add(bottes);
                }
                bottes = equipement;
                break;
        }

        System.out.println(equipement.getNom() + " équipé.");
        calculerAttaqueTotale();  // Recalculer l'attaque totale après avoir équipé
        calculerArmureTotale();   // Recalculer l'armure totale après avoir équipé
    }

    // Méthode pour déséquiper un objet
    public void desequiper(Equipement.TypeEquipement type) {
        switch (type) {
            case CASQUE:
                if (casque != null) {
                    inventaire.add(casque);  // Ajouter le casque à l'inventaire
                    System.out.println(casque.getNom() + " déséquipé.");
                    casque = null;
                }
                break;
            case ARMURE:
                if (armure != null) {
                    inventaire.add(armure);  // Ajouter l'armure à l'inventaire
                    System.out.println(armure.getNom() + " déséquipée.");
                    armure = null;
                }
                break;
            case ARME:
                if (arme != null) {
                    inventaire.add(arme);  // Ajouter l'arme à l'inventaire
                    System.out.println(arme.getNom() + " déséquipée.");
                    arme = null;
                }
                break;
            case BOUCLIER:
                if (bouclier != null) {
                    inventaire.add(bouclier);  // Ajouter le bouclier à l'inventaire
                    System.out.println(bouclier.getNom() + " déséquipé.");
                    bouclier = null;
                }
                break;
            case BOTTES:
                if (bottes != null) {
                    inventaire.add(bottes);  // Ajouter les bottes à l'inventaire
                    System.out.println(bottes.getNom() + " déséquipées.");
                    bottes = null;
                }
                break;
        }

        calculerAttaqueTotale();  // Recalculer l'attaque totale après le déséquipement
        calculerArmureTotale();   // Recalculer l'armure totale après le déséquipement
    }


    // Méthode pour afficher l'inventaire
    public void afficherInventaire() {
        System.out.println("Inventaire du joueur :");
        for (Item item : inventaire) {
            System.out.println("- " + item);
        }
    }

    // Méthode pour afficher l'équipement actuel
//    public void afficherEquipement() {
//        System.out.println("Équipement du joueur :");
//        System.out.println("Casque : " + (casque != null ? casque : "Aucun"));
//        System.out.println("Armure : " + (armure != null ? armure : "Aucune"));
//        System.out.println("Arme : " + (arme != null ? arme : "Aucune"));
//        System.out.println("Bouclier : " + (bouclier != null ? bouclier : "Aucun"));
//        System.out.println("Bottes : " + (bottes != null ? bottes : "Aucune"));
//    }

    // Calculer les bonus d'attaque totale
    public void calculerAttaqueTotale() {
        int bonusAttaque = (arme != null ? arme.getBonusPointsDAttaque() : 0);
        this.attaqueTotale = this.getPointsDAttaque() + bonusAttaque;
    }

    // Calculer les bonus d'armure totale
    public void calculerArmureTotale() {
        int bonusArmure = (casque != null ? casque.getBonusPointsDArmure() : 0)
            + (armure != null ? armure.getBonusPointsDArmure() : 0)
            + (bouclier != null ? bouclier.getBonusPointsDArmure() : 0)
            + (bottes != null ? bottes.getBonusPointsDArmure() : 0);
        this.armureTotale = this.getPointsDArmure() + bonusArmure;
    }

    // Getter pour l'attaque totale
    public int getAttaqueTotale() {
        return attaqueTotale;
    }

    // Getter pour l'armure totale
    public int getArmureTotale() {
        return armureTotale;
    }

    // Méthode pour afficher les statistiques du joueur
    public void afficherStatistiques() {
        System.out.println("Statistiques du joueur :");
        System.out.println("Attaque totale : " + getAttaqueTotale());
        System.out.println("Armure totale : " + getArmureTotale());
        System.out.println("Points de vie : " + getPointsDeVie() + "/" + getPointsDeVieMax());
    }
    public List<Equipement> getEquipements() {
        List<Equipement> equipementsEquipes = new ArrayList<>();

        // Vérifier et ajouter chaque équipement s'il est équipé
        if (casque != null) {
            equipementsEquipes.add(casque);
        }
        if (armure != null) {
            equipementsEquipes.add(armure);
        }
        if (arme != null) {
            equipementsEquipes.add(arme);
        }
        if (bouclier != null) {
            equipementsEquipes.add(bouclier);
        }
        if (bottes != null) {
            equipementsEquipes.add(bottes);
        }

        return equipementsEquipes;
    }

    public List<Item> getInventaire() {
        return inventaire;
    }

    public void afficherEquipement() {
        System.out.println("Équipements équipés du joueur :");

        if (casque != null) {
            System.out.println("- Casque : " + casque.getNom());
        } else {
            System.out.println("- Casque : Aucun");
        }

        if (armure != null) {
            System.out.println("- Armure : " + armure.getNom());
        } else {
            System.out.println("- Armure : Aucune");
        }

        if (arme != null) {
            System.out.println("- Arme : " + arme.getNom());
        } else {
            System.out.println("- Arme : Aucune");
        }

        if (bouclier != null) {
            System.out.println("- Bouclier : " + bouclier.getNom());
        } else {
            System.out.println("- Bouclier : Aucun");
        }

        if (bottes != null) {
            System.out.println("- Bottes : " + bottes.getNom());
        } else {
            System.out.println("- Bottes : Aucune");
        }
    }



}







