package com.mmorpg.vortx.equipement;

public class Item {
    private String nom;
    private String description;
    private String imagePath;

    public Item(String nom, String description,String cheminImage) {
        this.nom = nom;
        this.description = description;
        this.imagePath = cheminImage;

    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return nom + ": " + description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }



}
