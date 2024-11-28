package com.mmorpg.vortx.equipement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mmorpg.vortx.entiter.Joueur;

import java.util.List;

public class EquipementWindow extends Window {
    private Joueur joueur;
    private Stage stage;
    private Table layoutTable; // Table pour organiser les équipements autour de l'image centrale
    private Skin skin;

    public EquipementWindow(Joueur joueur, Skin skin) {
        super("Equipement", skin);
        this.joueur = joueur;
        this.skin = skin;

        // Configuration de la fenêtre
        this.setSize(600, 600);
        this.setPosition(Gdx.graphics.getWidth() / 2f - this.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - this.getHeight() / 2f);
        this.setMovable(true);
        this.setResizable(true);

        // Crée un stage pour gérer l'affichage de la fenêtre
        stage = new Stage(new ScreenViewport());
        stage.addActor(this);
        Gdx.input.setInputProcessor(stage);

        // Création de la table pour l'organisation de l'équipement
        layoutTable = new Table();
        layoutTable.setFillParent(true);

        // Ajout de l'organisation des équipements autour du personnage
        updateEquipmentLayout();

        // Organise l'affichage
        this.add(layoutTable).fill().expand();
    }

    private void updateEquipmentLayout() {
        layoutTable.clear(); // Vider la table avant de la remplir

        // Image centrale du personnage
        Image personnageImage = new Image(new Texture("ide.png"));
        layoutTable.add(personnageImage).size(200, 200).colspan(3).row();

        // Disposer les images des équipements autour de l'image centrale
        List<Equipement> equipements = joueur.getEquipements();

        // Ajouter les images des équipements aux positions appropriées
        Image casqueImage = null;
        Image armureImage = null;
        Image armeImage = null;
        Image bouclierImage = null;
        Image bottesImage = null;

        // Parcourir les équipements du joueur et assigner les images
        for (Equipement equip : equipements) {
            Image equipImage = new Image(new Texture(equip.getImagePath())); // Récupérer le chemin d'image
            switch (equip.getTypeEquipement()) {
                case CASQUE:
                    casqueImage = equipImage;
                    break;
                case ARMURE:
                    armureImage = equipImage;
                    break;
                case ARME:
                    armeImage = equipImage;
                    break;
                case BOUCLIER:
                    bouclierImage = equipImage;
                    break;
                case BOTTES:
                    bottesImage = equipImage;
                    break;
            }
        }

        // Disposition du casque en haut
        layoutTable.add(casqueImage != null ? casqueImage : new Label("/", skin)).size(60, 60).colspan(3).padBottom(10).row();

        // Disposition de l'arme à gauche, armure au centre, bouclier à droite
        layoutTable.add(armeImage != null ? armeImage : new Label("/", skin)).size(60, 60).padRight(10);
        layoutTable.add(armureImage != null ? armureImage : new Label("/", skin)).size(60, 60).padRight(10);
        layoutTable.add(bouclierImage != null ? bouclierImage : new Label("/", skin)).size(60, 60).row();

        // Disposition des bottes en bas
        layoutTable.add(bottesImage != null ? bottesImage : new Label("/", skin)).size(60, 60).colspan(3).padTop(10).row();
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public void dispose() {
        stage.dispose();
    }
}
