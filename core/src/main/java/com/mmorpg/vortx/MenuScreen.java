package com.mmorpg.vortx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {

    private MainGame game;
    private Stage stage;
    private Skin skin;
    private TextButton playButton;
    private TextButton settingsButton;
    private SpriteBatch batch;
    private Animation<TextureRegion> backgroundAnimation;
    private float elapsedTime = 0f;
    public static int SON = 50;
    private Window settingsWindow;
    private Window selectionWindow;


    // Variables globales pour les stats du joueur
    public static int pvMax_joueur;
    public static int ptsAttaque_joueur;
    public static int ptsArmure_joueur;

    // Variable pour stocker le label de classe sélectionnée
    private Label classLabel;

    public MenuScreen(MainGame game) {
        this.game = game;
        batch = new SpriteBatch();

        // Charger les frames du fond et créer une animation
        TextureRegion[] backgroundFrames = new TextureRegion[9];
        for (int i = 0; i < 9; i++) {
            backgroundFrames[i] = new TextureRegion(new Texture("fond/fond_frame_" + (i + 1) + ".png"));
        }
        backgroundAnimation = new Animation<>(0.1f, backgroundFrames);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(new ScreenViewport());

        // Créer le bouton "Jouer"
        playButton = new TextButton("Jouer", skin);
        playButton.setSize(300, 100);
        playButton.setPosition(Gdx.graphics.getWidth() / 2f - playButton.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);

        // Listener pour le bouton "Jouer"
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openCharacterSelection(); // Ouvrir le menu de sélection de personnage
            }
        });
        stage.addActor(playButton);

        // Créer le bouton "Paramètres"
        settingsButton = new TextButton("Paramètres", skin);
        settingsButton.setSize(300, 100);
        settingsButton.setPosition(Gdx.graphics.getWidth() / 2f - settingsButton.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - 150);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openSettingsWindow();
            }
        });
        stage.addActor(settingsButton);

        Gdx.input.setInputProcessor(stage);
    }

    // Méthode pour afficher le menu de sélection de personnage
    private void openCharacterSelection() {
        selectionWindow = new Window("Sélection de personnage", skin);

        // Créer les boutons de sélection mutuelle
        final TextButton guerrierButton = new TextButton("Guerrier", skin, "toggle");
        final TextButton tankButton = new TextButton("Tank", skin, "toggle");

        guerrierButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pvMax_joueur = 100;
                ptsAttaque_joueur = 20;
                ptsArmure_joueur = 30;
                guerrierButton.setChecked(true);
                tankButton.setChecked(false);
                showClassChoice("Guerrier");
            }
        });

        tankButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pvMax_joueur = 150;
                ptsAttaque_joueur = 10;
                ptsArmure_joueur = 10;
                tankButton.setChecked(true);
                guerrierButton.setChecked(false);
                showClassChoice("Tank");
            }
        });

        // Ajouter les boutons à la fenêtre
        selectionWindow.add(guerrierButton).pad(10);
        selectionWindow.add(tankButton).pad(10);

        // Ajouter un bouton pour valider et commencer le jeu
        TextButton confirmButton = new TextButton("Confirmer", skin);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen());  // Lancer le jeu avec les paramètres choisis
                selectionWindow.remove();  // Fermer la fenêtre de sélection
            }
        });
        selectionWindow.row();
        selectionWindow.add(confirmButton).colspan(2).center().pad(10);

        selectionWindow.setSize(400, 600);
        selectionWindow.setPosition(Gdx.graphics.getWidth() / 2f - selectionWindow.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - selectionWindow.getHeight() / 2f);
        stage.addActor(selectionWindow);
    }

    // Méthode pour afficher la classe sélectionnée
    private void showClassChoice(String classe) {
        // Si un ancien label existe, le supprimer
        if (classLabel != null) {
            classLabel.remove();
        }

        // Créer un nouveau label et l'afficher
        classLabel = new Label("Vous avez choisi: " + classe, skin);
        classLabel.setPosition(Gdx.graphics.getWidth() / 2f - classLabel.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - 200);
        stage.addActor(classLabel);
    }

    // Ouvrir la fenêtre de paramètres
    private void openSettingsWindow() {
        settingsWindow = new Window("Paramètres", skin);

        // Créer une barre de volume (Slider)
        final Slider volumeSlider = new Slider(0, 100, 1, false, skin);
        volumeSlider.setValue(SON);

        // Créer un label pour afficher la valeur du volume
        final Label volumeLabel = new Label("Volume: " + SON, skin);

        // Ajouter un listener pour mettre à jour la valeur de la variable SON et le label
        volumeSlider.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SON = (int) volumeSlider.getValue();
                volumeLabel.setText("Volume: " + SON);
            }
        });

        settingsWindow.add(volumeSlider).row();
        settingsWindow.add(volumeLabel).row();

        TextButton backButton = new TextButton("X", skin);
        backButton.setSize(30, 30);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsWindow.remove();
            }
        });
        settingsWindow.getTitleTable().add(backButton).left().top();

        settingsWindow.setSize(400, 300);
        settingsWindow.setPosition(Gdx.graphics.getWidth() / 2f - settingsWindow.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - settingsWindow.getHeight() / 2f);
        stage.addActor(settingsWindow);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        batch.begin();
        TextureRegion currentFrame = backgroundAnimation.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if (settingsWindow != null && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            settingsWindow.remove();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && selectionWindow!=null) {
            if(classLabel != null) {
                classLabel.remove();
            }
            selectionWindow.remove();
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        batch.dispose();

        for (TextureRegion frame : backgroundAnimation.getKeyFrames()) {
            frame.getTexture().dispose();
        }
    }
}
