package com.mmorpg.vortx.equipement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mmorpg.vortx.entiter.Joueur;

public class InventoryWindow extends Window {
    private Joueur joueur;
    private Stage stage;
    private Table equipmentTable;
    private Table inventoryTable;
    private Skin skin;

    public InventoryWindow(Joueur joueur, Skin skin) {
        super("Inventory", skin);
        this.joueur = joueur;
        this.skin = skin;

        // Configuration de la fenêtre
        this.setSize(600, 400);
        this.setPosition(Gdx.graphics.getWidth() / 2f - this.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - this.getHeight() / 2f);
        this.setMovable(true);
        this.setResizable(true);

        // Crée un stage pour gérer l'affichage de la fenêtre
        stage = new Stage(new ScreenViewport());
        stage.addActor(this);
        Gdx.input.setInputProcessor(stage);

        // Création des tables pour l'équipement et l'inventaire
        equipmentTable = new Table();
        inventoryTable = new Table();

        // Ajoute l'équipement équipé à la table
        updateEquipmentTable();

        // Ajoute les items de l'inventaire à la table
        updateInventoryTable();

        // Organise l'affichage
        this.add(new Label("Equipments", skin)).row();
        this.add(equipmentTable).fillX().expandX().row();
        this.add(new Label("Inventory", skin)).row();
        this.add(inventoryTable).fillX().expandX().row();
    }

    private void updateEquipmentTable() {
        equipmentTable.clear();
        for (Equipement equip : joueur.getEquipements()) {
            TextButton equipButton = new TextButton(equip.getNom() + " (Équipé)", skin);
            equipButton.setColor(Color.GREEN);  // Met en évidence l'équipement équipé avec une couleur différente
            equipButton.addListener(new ClickListener(Input.Buttons.LEFT) {  // Déséquiper avec un clic gauche
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    joueur.desequiper(equip.getTypeEquipement());
                    updateEquipmentTable();
                    updateInventoryTable();
                }
            });
            equipmentTable.add(equipButton).pad(10);
        }
    }

    private void updateInventoryTable() {
        inventoryTable.clear();
        for (Item item : joueur.getInventaire()) {
            if (item instanceof Equipement) {
                Equipement equip = (Equipement) item;
                TextButton itemButton = new TextButton(equip.getNom(), skin);

                // Si l'objet est équipé, on change sa couleur
                if (isEquipped(equip)) {
                    itemButton.setColor(Color.GREEN);  // Met en vert les objets équipés
                }

                itemButton.addListener(new ClickListener(Input.Buttons.RIGHT) {  // Clic droit pour équiper
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        joueur.equiper(equip);
                        updateEquipmentTable();
                        updateInventoryTable();
                    }
                });
                inventoryTable.add(itemButton).pad(10);
            }
        }
    }

    // Méthode pour vérifier si l'objet est déjà équipé
    private boolean isEquipped(Equipement equip) {
        for (Equipement e : joueur.getEquipements()) {
            if (e.equals(equip)) {
                return true;
            }
        }
        return false;
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
