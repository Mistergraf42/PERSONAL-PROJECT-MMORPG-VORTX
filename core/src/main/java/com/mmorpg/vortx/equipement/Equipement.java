package com.mmorpg.vortx.equipement;

public class Equipement extends Item {
    public enum TypeEquipement {
        CASQUE, ARMURE, ARME, BOUCLIER, BOTTES
    }

    private TypeEquipement typeEquipement;
    private int bonusPointsDAttaque;
    private int bonusPointsDArmure;

    public Equipement(String nom, String description, TypeEquipement type, int bonusAttaque, int bonusArmure,String chemin) {
        super(nom, description,chemin);
        this.typeEquipement = type;
        this.bonusPointsDAttaque = bonusAttaque;
        this.bonusPointsDArmure = bonusArmure;
    }

    public TypeEquipement getTypeEquipement() {
        return typeEquipement;
    }

    public int getBonusPointsDAttaque() {
        return bonusPointsDAttaque;
    }

    public int getBonusPointsDArmure() {
        return bonusPointsDArmure;
    }

    @Override
    public String toString() {
        return super.toString() + " (Type: " + typeEquipement + ", Attaque: +" + bonusPointsDAttaque + ", Armure: +" + bonusPointsDArmure + ")";
    }
}
