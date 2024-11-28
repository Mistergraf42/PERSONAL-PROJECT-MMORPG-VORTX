package com.mmorpg.vortx.entiter;

import com.mmorpg.vortx.Carte;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GestionnaireEntites {
    private List<Entiter> entites;
    private Carte carte;

    public GestionnaireEntites(Carte carte) {
        this.carte = carte;
        this.entites = new ArrayList<>();
    }

    /****************************
     |   Permet d'ajouter Monstre ou Joueurs
     *****************************/
    public void ajouterEntite(Entiter entite) {
        // Vérifier si la position est accessible avant d'ajouter
        if (carte.estAccessible(entite.getMapX(), entite.getMapY())) {
            entites.add(entite);
        } else {
            System.out.println("Impossible d'ajouter l'entité à une position inaccessible.");
        }
    }

    /****************************
     |    Permet de supprimer Monstre ou Joueurs
     *****************************/
    public void supprimerEntite(Entiter entite) {
        entites.remove(entite);
    }


    /****************************
     |    Permet d'avoir la liste de toutes les entiter
     *****************************/
    public List<Entiter> getEntites() {
        return entites;
    }

    public void mettreAJour(float deltaTime) {
        Iterator<Entiter> iterator = entites.iterator();
        while (iterator.hasNext()) {
            Entiter entite = iterator.next();

            // Vérifier si l'entité est toujours en vie
            if (!entite.isEnVie()) {
                iterator.remove();  // Suppression sécurisée via l'itérateur
                continue;
            }

            // Ajoute ici la logique pour mettre à jour l'entité si nécessaire
        }
    }


    /****************************
     |    Affiche toutes les Monstres et Joueurs
     *****************************/
    public void afficherEntites() {
        for (Entiter entite : entites) {
            if (entite instanceof Joueur) {
                ((Joueur) entite).afficherInfoJoueur();
            } else if (entite instanceof Monstre) {
                ((Monstre) entite).afficherInfoMonstre();
            }
            System.out.println("-------------------------");
        }
    }

    /****************************
     |    Trouve entité par position Matricielle
     *****************************/
    public Entiter trouverEntite(int mapX, int mapY) {
        for (Entiter entite : entites) {
            if (entite.getMapX() == mapX && entite.getMapY() == mapY) {
                return entite;
            }
        }
        return null;
    }


//CELLE QUI MARCHE
//    public List<Entiter> trouverEntitesDansCase(int matX, int matY) {
//        List<Entiter> entitesDansCase = new ArrayList<>();
//        for (Entiter entite : entites) {
//            if (entite instanceof Boss) {
//                Boss boss = (Boss) entite;
//                if (boss.estToucheParCompetence(new Vector2(matX, matY))) {
//                    entitesDansCase.add(boss);
//                }
//            } else {
//                if (entite.getMapX() == matX && entite.getMapY() == matY) {
//                    entitesDansCase.add(entite);
//                }
//            }
//        }
//        return entitesDansCase;
//    }

    public List<Entiter> trouverEntitesDansCase(int matX, int matY) {
        List<Entiter> entitesDansCase = new ArrayList<>();
        for (Entiter entite : entites) {
            // Affichage des coordonnées de l'entité et de la case à comparer
            System.out.println("Entité à [" + entite.getMapX() + ", " + entite.getMapY() + "], case cible : [" + matX + ", " + matY + "]");

            if (entite.getMapX() == matX && entite.getMapY() == matY) {
                entitesDansCase.add(entite);
                System.out.println("Entité trouvée dans la case : " + entite.getClass().getSimpleName());
            }
        }
        return entitesDansCase;
    }





    public Joueur getJoueur() {
        for (Entiter entite : entites) {
            if (entite instanceof Joueur) {
                return (Joueur) entite;
            }
        }
        return null; // Retourne null si aucun joueur n'est trouvé
    }

}

