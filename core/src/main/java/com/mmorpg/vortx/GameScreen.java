package com.mmorpg.vortx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mmorpg.vortx.entiter.*;
import com.mmorpg.vortx.equipement.Equipement;
import com.mmorpg.vortx.equipement.EquipementWindow;
import com.mmorpg.vortx.equipement.InventoryWindow;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Texture sableTexture, neigeTexture, paveTexture, cheminTexture, cheminVilleTexture;


    private Texture herbeTexture_1;
    private Texture herbeTexture_2;
    private Texture herbeTexture_3;
    private Texture herbeTexture_4;



    private Texture arbreTexture, montagneTexture, eauTexture, glacierTexture, rocheTexture;
    private Texture laveTexture, cactusTexture, sableMouvantTexture;
    private Texture teleporteurTexture;

    private Texture walkTextureRight;  // Texture pour l'animation de marche
    private Texture walkTextureLeft;  // Texture pour l'animation de marche
    private Texture walkTextureUp;  // Texture pour l'animation de marche
    private Texture walkTextureBottom;  // Texture pour l'animation de marche

    private Texture walkTextureRight_boss;  // Texture pour l'animation de marche
    private Texture walkTextureLeft_boss;  // Texture pour l'animation de marche
    private Texture walkTextureUp_boss;  // Texture pour l'animation de marche
    private Texture walkTextureBottom_boss;  // Texture pour l'animation de marche


    private Texture walkTextureRight_monstre;
    private Texture walkTextureLeft_monstre;
    private Texture walkTextureBottom_monstre;
    private Texture walkTextureUp_monstre;

    Animation<TextureRegion> monstreAnimationDroite ;
    Animation<TextureRegion> monstreAnimationGauche ;
    Animation<TextureRegion> monstreAnimationBas ;
    Animation<TextureRegion> monstreAnimationHaut ;



    private Texture joueurTexture;  // Texture pour le joueur
    private Texture monstreTexture;
    private Texture terreAbsoTexture;

    private Texture contour_texture;
    private Texture contour_hp_texture;
    private Texture contour_xp_texture;
    private Texture menu_ui_texture;
    private Texture coeur_ui;
    private Texture epee_ui;
    private Texture amure_ui;


    private Texture CoupSolTexture;
    private Texture MeteoreTexture;
    private Texture MurTexture;
    private Texture OffensiveTexture;
    private Texture TerreObasobtionTexture;
    private Texture VortexTexture;
    private Texture ArgentTexture;


    private MiniMapWindow miniMapWindow;
    private Skin skin;
    private boolean afficherMiniMap;

    private ShapeRenderer shapeRenderer;
    private ShapeRenderer shapeRendererMenu;

    private BitmapFont font;
    private BitmapFont fontPseudo;


    private Boss boss;
    private Boss boss2;

    private InventoryWindow inventoryWindow;
    private boolean afficherInventaire = false;  // Pour gérer l'affichage de l'inventaire

    private EquipementWindow equipementWindow;
    private boolean afficherEquipement = false;  // Pour gérer l'affichage de la fenêtre d'équipement






    //camera
    private OrthographicCamera camera;
    private CameraController cameraController;

    //LES SORT



    private Carte carte = new Carte();  // La matrice représentant la carte, avec les types de terrain et d'obstacles
    private static final int TILE_SIZE = 32;  // Taille d'une tuile en pixels

    private GestionnaireEntites gestionnaireEntites;
    private Joueur joueur;

    private Animation<TextureRegion> walkAnimationRight;
    private Animation<TextureRegion> walkAnimationLeft;
    private Animation<TextureRegion> walkAnimationUp;
    private Animation<TextureRegion> walkAnimationBottom;

    private Animation<TextureRegion> walkAnimationRight_boss;
    private Animation<TextureRegion> walkAnimationLeft_boss;
    private Animation<TextureRegion> walkAnimationUp_boss;
    private Animation<TextureRegion> walkAnimationBottom_boss;

    private Texture animationTexture;

    private Animation<TextureRegion> bossAnimation;


    private Animation<TextureRegion> eauAnimation;
    private Animation<TextureRegion> meteorAnimation;
    private Animation<TextureRegion> coupsolAnimation;
    private Animation<TextureRegion> vortexAnimation;
    private Animation<TextureRegion> fireAnimation;


    private Animation<TextureRegion> boss_2_animation;









    //-------------------------------------------------------------------
    //                      TIMEUR POUR ANIMATION
    //                UTILISER POUR GERER LA VISUALISION DES
    //                          HITBOX
    //-------------------------------------------------------------------

    private int hitbox = 0;
    private float elapsedTime = 0f;

    //-------------------------------------------------------------------
    //
    //                  UTILISER POUR NOS TYPE DE TERRAIN
    //
    //-------------------------------------------------------------------

    public static final int HERBE = 1;
    public static final int SABLE = 2;
    public static final int NEIGE = 3;
    public static final int PAVE = 4;
    public static final int CHEMIN = 5;
    public static final int CHEMIN_VILLE = 6;
    public static final int ARBRE = 7;
    public static final int MONTAGNE = 8;
    public static final int EAU = 9;
    public static final int GLACIER = 10;
    public static final int ROCHE = 11;
    public static final int LAVE = 12;
    public static final int CACTUS = 13;
    public static final int SABLE_MOUVANT = 14;
    public static final  int TELEPORTEUR = 15;

    private Pnj pnj;  // Ajoute cette ligne dans les attributs de GameScreen
    private Monstre monstre;

    private Equipement epeeAcierTurk;
    private Equipement bouclier;
    private Equipement armure;
    private Equipement casque;
    private Equipement bottes;
    private Equipement armure_bis;

    public GameScreen() {

        //-------------------------------------------------------------------
        //
        //                   MISE EN PLACE DE NOS CALQUES
        //
        //-------------------------------------------------------------------
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        shapeRendererMenu = new ShapeRenderer();

        //-------------------------------------------------------------------
        //
        //           CREATION D UN JOUEURS
        //
        //-------------------------------------------------------------------
      //   casqueFer = new Equipement("Casque en Fer", "Un casque robuste en fer", Equipement.TypeEquipement.CASQUE, 0, 5);
      //   epeeAcier = new Equipement("Épée en Acier", "Une épée tranchante en acier", Equipement.TypeEquipement.ARME, 10, 0);
        joueur = new Joueur(1,"Player", 100, 20, 10, 20, 7);
        //   casqueFer = new Equipement("Casque en Fer", "Un casque robuste en fer", Equipement.TypeEquipement.CASQUE, 0, 5);
        //   epeeAcier = new Equipement("Épée en Acier", "Une épée tranchante en acier", Equipement.TypeEquipement.ARME, 10, 0);
        epeeAcierTurk = new Equipement("Épée en Acier Turk", "Une épée tranchante en acier", Equipement.TypeEquipement.ARME, 20, 0,"equipement/sword_01.png");
        bouclier = new Equipement("Bouclier de bg", "Bouclier anti ozan", Equipement.TypeEquipement.BOUCLIER, 0, 20,"equipement/shield_02.png");
        armure = new Equipement("Armure de chasseur", "Armure anti gros", Equipement.TypeEquipement.ARMURE, 0, 50,"equipement/iron_armor.png");
        casque = new Equipement("Lunette", "Lunette d ozan", Equipement.TypeEquipement.CASQUE, 0, 50,"equipement/glasses.png");
        bottes = new Equipement("Bottes", "Botte si y a la police", Equipement.TypeEquipement.BOTTES, 20, 30,"equipement/boots_02.png");
        armure_bis = new Equipement("Armure de chasseur boss", "Armure anti gros", Equipement.TypeEquipement.ARMURE, 0, 60,"equipement/fablic_clothe.png");

        joueur.setVitesse(3);
        joueur.ajouterItem(epeeAcierTurk);
        joueur.ajouterItem(bouclier);
        joueur.ajouterItem(armure);
        joueur.ajouterItem(casque);
        joueur.ajouterItem(bottes);
        joueur.ajouterItem(armure_bis);

        pnj = new Pnj(3,"Garde", "Bienvenue dans notre village !", 100, 0, 0, 10, 10, true);  // Initialise un PNJ

//
//        joueur.afficherInventaire();
//        joueur.afficherEquipement();
//
//        // Calculer les statistiques avec les équipements
//        System.out.println("Attaque avec équipement : " + joueur.calculerAttaque());
//        System.out.println("Armure avec équipement : " + joueur.calculerArmure());


        //-------------------------------------------------------------------
        // CAMERA INITIALER SUR LA TAILLE FENETRE EST CENTRER SUR JOUEUR
        //
        // POLICE DE BASE DE LIBGDX
        //-------------------------------------------------------------------
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraController = new CameraController(camera, joueur); // Le joueur est maintenant initialisé
        font = new BitmapFont();
        fontPseudo = new BitmapFont();

        //-------------------------------------------------------------------
        //
        //                     CREATION DU GESTIONAIRE DE NOS ENTITÉ
        //  ET LEURS AJOUTS
        //-------------------------------------------------------------------
        boss = new Boss(1,"boss",300,10,10,25,10,5,carte);
        boss.afficherInfoBoss();
        boss2 = new Boss(2,"SQUELLETOR",300,100,10,25,29,5,carte);
        boss2.afficherInfoBoss();
        // Création d'un nouveau monstre
        Monstre monstre = new Monstre(3, "Goblin", 50, 15, 5, 20, 9, 1);



        gestionnaireEntites = new GestionnaireEntites(carte);
        gestionnaireEntites.ajouterEntite(joueur);
        gestionnaireEntites.ajouterEntite(boss);
        gestionnaireEntites.ajouterEntite(pnj);  // Ajoute le PNJ à la liste des entités
        gestionnaireEntites.ajouterEntite(boss2);
        gestionnaireEntites.ajouterEntite(monstre);
        monstre.afficherInfoMonstre();


        //-------------------------------------------------------------------
        //
        //                     INITIALISATION DES COMPETENCES
        //
        //-------------------------------------------------------------------

        skin = new Skin(Gdx.files.internal("ui/uiskin.json")); // Charge le skin pour la fenêtre
        afficherMiniMap = false;
gestionnaireEntites.ajouterEntite(monstre);
        inventoryWindow = new InventoryWindow(joueur, skin);






    }


    @Override
    public void show() {
        //-------------------------------------------------------------------
        //
        //                    CHARGEMENT DES TEXTURE PNG
        //
        //-------------------------------------------------------------------

        contour_texture = new Texture("contour.png");
        contour_hp_texture = new Texture("contour_hp.png");
        menu_ui_texture = new Texture("menu_ui.png");
        coeur_ui = new Texture("coeur.png");
        epee_ui = new Texture("epee_iu.png");
        amure_ui = new Texture("amure_iu.png");
        contour_xp_texture = new Texture("contour_xp.png");
        ArgentTexture = new Texture("argent_ui.png");



        CoupSolTexture = new Texture("competence_image/competence_coup_sol.png");
        MeteoreTexture = new Texture("competence_image/competence_meteorite.png");
        MurTexture = new Texture("competence_image/competence_mur_glace.png");
        OffensiveTexture = new Texture("competence_image/competence_offensive.png");
        TerreObasobtionTexture = new Texture("competence_image/competence_vol_sort.png");
        VortexTexture = new Texture("competence_image/competence_vortex.png");



        herbeTexture_1 = new Texture("grass_teste_xd.png");
        herbeTexture_2 = new Texture("grass_2.png");
        herbeTexture_3 = new Texture("grass_3.png");
        herbeTexture_4 = new Texture("grass_4.png");

        sableTexture = new Texture("sable.png");
        neigeTexture = new Texture("neige.png");
        paveTexture = new Texture("pave.png");
        cheminTexture = new Texture("chemin.png");
        cheminVilleTexture = new Texture("chemin_ville.png");
        arbreTexture = new Texture("arbre.png");
        montagneTexture = new Texture("montagne.png");
        eauTexture = new Texture("eau.png");
        glacierTexture = new Texture("glacier.png");
        rocheTexture = new Texture("roche.png");
        laveTexture = new Texture("lave.png");
        cactusTexture = new Texture("cactus.png");
        sableMouvantTexture = new Texture("sable_mouvant.png");
        walkTextureRight = new Texture("deplacement_droite.png");
        walkTextureLeft = new Texture("deplacement_gauche.png");
        walkTextureBottom = new Texture("deplacement_bas.png");
        walkTextureUp = new Texture("deplacement_haut.png");

        walkTextureRight_boss = new Texture("boss_deplacement/droite.png");
        walkTextureLeft_boss = new Texture("boss_deplacement/gauche.png");
        walkTextureBottom_boss = new Texture("boss_deplacement/bas.png");
        walkTextureUp_boss = new Texture("boss_deplacement/haut.png");

        joueurTexture = new Texture("ide.png");
        animationTexture = new Texture("animation.png");
        terreAbsoTexture = new Texture("terre_abso.png");



        //----------------------Monstre--------------------------
        monstreTexture = new Texture("ide_monstre.png");
        teleporteurTexture = new Texture("teleporteur.png");


        walkTextureRight_monstre = new Texture("monstre/monstre_droite.png");
        walkTextureLeft_monstre = new Texture("monstre/monstre_gauche.png");
        walkTextureBottom_monstre = new Texture("monstre/monstre_bas.png");
        walkTextureUp_monstre = new Texture("monstre/monstre_haut.png");


        TextureRegion[][] framesRight = TextureRegion.split(walkTextureRight_monstre, 48, 48);
        TextureRegion[][] framesLeft = TextureRegion.split(walkTextureLeft_monstre, 48, 48);
        TextureRegion[][] framesBottom = TextureRegion.split(walkTextureBottom_monstre, 48, 48);
        TextureRegion[][] framesUp = TextureRegion.split(walkTextureUp_monstre, 48, 48);

        monstreAnimationDroite = new Animation<>(0.1f, framesRight[0]);
        monstreAnimationGauche = new Animation<>(0.1f, framesLeft[0]);
        monstreAnimationBas = new Animation<>(0.1f, framesBottom[0]);
        monstreAnimationHaut = new Animation<>(0.1f, framesUp[0]);

        //-------------------------------------------------------------------
        // SI PLUSIEURS GIF
        //                     CREE UNE TEXTURE REGION
        //                                      AVEC TOUTES LES FRAME
        //-------------------------------------------------------------------



        TextureRegion[] fireballFrame = new TextureRegion[5]; // Suppose qu'il y a 10 frames, ajuste selon ton GIF
        for (int i = 0; i < 5; i++) {
            fireballFrame[i] = new TextureRegion(new Texture("fireball/fireball_frame_" + (i + 1) + ".png"));  // boss1.png, boss2.png, ...
        }

        TextureRegion[] meteorFrames = new TextureRegion[13]; // Suppose qu'il y a 10 frames, ajuste selon ton GIF
        for (int i = 0; i < 13; i++) {
            meteorFrames[i] = new TextureRegion(new Texture("meteorite/meteorite_frame_" + (i + 1) + ".png"));  // boss1.png, boss2.png, ...
        }

        TextureRegion[] bossFrames = new TextureRegion[11]; // Suppose qu'il y a 10 frames, ajuste selon ton GIF
        for (int i = 0; i < 11; i++) {
            bossFrames[i] = new TextureRegion(new Texture("boss_walk/demon_walk_frame_" + (i + 1) + ".png"));  // boss1.png, boss2.png, ...
        }
        TextureRegion[] backgroundBossAnimationFrame = new TextureRegion[10]; // Suppose qu'il y a 10 frames, ajuste selon ton GIF
        for (int i = 0; i < 10; i++) {
            backgroundBossAnimationFrame[i] = new TextureRegion(new Texture("background/background_frame_" + (i + 1) + ".png"));  // boss1.png, boss2.png, ...
        }
        TextureRegion[] eauAnimationFrame = new TextureRegion[10]; // Suppose qu'il y a 10 frames, ajuste selon ton GIF
        for (int i = 0; i < 10; i++) {
            eauAnimationFrame[i] = new TextureRegion(new Texture("eau/eau_frame_" + (i + 1) + ".png"));  // boss1.png, boss2.png, ...
        }
        TextureRegion[] auraAnimationFrame = new TextureRegion[119]; // Suppose qu'il y a 10 frames, ajuste selon ton GIF
        for (int i = 0; i < 119; i++) {
            auraAnimationFrame[i] = new TextureRegion(new Texture("buff/buff_frame_" + (i + 1) + ".png"));  // boss1.png, boss2.png, ...
        }

        TextureRegion[] coupsolAnimationFrame = new TextureRegion[14]; // Suppose qu'il y a 10 frames, ajuste selon ton GIF
        for (int i = 0; i < 14; i++) {
            coupsolAnimationFrame[i] = new TextureRegion(new Texture("coupsol/coupsol_frame_" + (i + 1) + ".png"));  // boss1.png, boss2.png, ...
        }

        TextureRegion[] boss_2_animation_frame = new TextureRegion[16]; // Suppose qu'il y a 10 frames, ajuste selon ton GIF
        for (int i = 0; i < 16; i++) {
            boss_2_animation_frame[i] = new TextureRegion(new Texture("boss_val/boss_val_frame_" + (i + 1) + ".png"));  // boss1.png, boss2.png, ...
        }

        TextureRegion[] vortexAnimationFrame = new TextureRegion[9]; // Suppose qu'il y a 10 frames, ajuste selon ton GIF
        for (int i = 0; i < 9; i++) {
            vortexAnimationFrame[i] = new TextureRegion(new Texture("trou_noir/trou_noir_frame_" + (i + 1) + ".png"));  // boss1.png, boss2.png, ...
        }


        //-------------------------------------------------------------------
        // POUR SPRITE
        //                    DECOUPE EN PARTIE
        //                                            48X48PX
        //-------------------------------------------------------------------
        TextureRegion[][] walkFramesRight = TextureRegion.split(walkTextureRight, 48, 48);
        TextureRegion[][] walkFramesLeft = TextureRegion.split(walkTextureLeft, 48, 48);
        TextureRegion[][] walkFramesBottom = TextureRegion.split(walkTextureBottom, 48, 48);
        TextureRegion[][] walkFramesUp = TextureRegion.split(walkTextureUp, 48, 48);

        TextureRegion[][] walkFramesRight_boss = TextureRegion.split(walkTextureRight_boss, 48, 48);
        TextureRegion[][] walkFramesLeft_boss = TextureRegion.split(walkTextureLeft_boss, 48, 48);
        TextureRegion[][] walkFramesBottom_boss = TextureRegion.split(walkTextureBottom_boss, 48, 48);
        TextureRegion[][] walkFramesUp_boss = TextureRegion.split(walkTextureUp_boss, 48, 48);

        TextureRegion[][] skillFrames = TextureRegion.split(animationTexture, 48, 48);




        //-------------------------------------------------------------------
        //
        //           CREE UNE ANIMATION AVEC 0.1S ENTRE CHAQUE IMAGE
        //
        //-------------------------------------------------------------------
        walkAnimationRight = new Animation<>(0.1f, walkFramesRight[0]);
        walkAnimationLeft = new Animation<>(0.1f, walkFramesLeft[0]);
        walkAnimationBottom = new Animation<>(0.1f, walkFramesBottom[0]);
        walkAnimationUp = new Animation<>(0.1f, walkFramesUp[0]);

        walkAnimationRight_boss = new Animation<>(0.1f, walkFramesRight_boss[0]);
        walkAnimationLeft_boss = new Animation<>(0.1f, walkFramesLeft_boss[0]);
        walkAnimationBottom_boss = new Animation<>(0.1f, walkFramesBottom_boss[0]);
        walkAnimationUp_boss = new Animation<>(0.1f, walkFramesUp_boss[0]);

        //----------------OPTIONEL ANIMATION--------------------------
        bossAnimation = new Animation<>(0.1f, bossFrames);
        eauAnimation = new Animation<>(0.1f, eauAnimationFrame);
        meteorAnimation = new Animation<>(0.1f, meteorFrames);
        coupsolAnimation = new Animation<>(0.1f, coupsolAnimationFrame);
        vortexAnimation= new Animation<>(0.1f, vortexAnimationFrame);
        fireAnimation= new Animation<>(0.1f, fireballFrame);
        boss_2_animation =new Animation<>(0.1f, boss_2_animation_frame);




        // Générer une police Bitmap à partir d'un fichier .ttf
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Daydream.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 16;  // Taille de la police
        font = generator.generateFont(parameter); // Crée une police BitmapFont à partir du .ttf
        generator.dispose(); // Libère le générateur après usage


    }





    //----------------------------------------------//
    //                                              //
    //              RENDU GRAPHIQUE                 //
    //                                              //
    //----------------------------------------------//

    @Override
    public void render(float delta) {


        //-------------------------------------------------------------------
        //
        //                  GESTION DE LA SOURIS
        //
        //-------------------------------------------------------------------

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY(); // Inverser l'axe Y

        Vector3 worldCoordinates = new Vector3(mouseX, mouseY, 0);
        camera.unproject(worldCoordinates); // Convertir les coordonnées écran en coordonnées monde

        float tileX = worldCoordinates.x / Carte.TAILLE_CASE;
        float tileY = worldCoordinates.y / Carte.TAILLE_CASE;


        //-------------------------------------------------------------------
        //
        //                  TEMPS POUR ANIMATION
        //
        //-------------------------------------------------------------------
        elapsedTime += delta;

        //-------------------------------------------------------------------
        //
        //                     CAMERA
        //
        //-------------------------------------------------------------------
        cameraController.update();
        batch.setProjectionMatrix(camera.combined);


        joueur.verifierTeleportation(carte, 25, 10);
        // Applique la matrice de projection de la caméra au batch
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRendererMenu.setProjectionMatrix(camera.combined);
        // Appliquer la caméra au batch
        batch.begin();


        //-------------------------------------------------------------------
        //
        //         DESSINE LE FOND D'EAU POUR EVITER LE NOIR BUGGER
        //
        //-------------------------------------------------------------------
        TextureRegion currentEau = eauAnimation.getKeyFrame(elapsedTime, true);
        batch.draw(currentEau, -30 * TILE_SIZE, -30 * TILE_SIZE, 5000, 5000);


        //-------------------------------------------------------------------
        //
        //                  ON DESSINE LA CARTE
        //
        //-------------------------------------------------------------------

        carte.renderCarte(batch, this);


        //-------------------------------------------------------------------
        //
        //                 MET A JOURS LES ENTITER
        //
        //-------------------------------------------------------------------

        gestionnaireEntites.mettreAJour(delta);

        //-------------------------------------------------------------------
        //
        //                 IU
        //
        //-------------------------------------------------------------------

// Récupérer la largeur et la hauteur de la fenêtre
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();


        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        batch.draw(menu_ui_texture, 10, 10, 300, 150);  // Position en bas à gauche, avec un léger décalage
        batch.draw(contour_hp_texture, 45, 30, joueur.getPointsDeVie() * 2, 32);  // Barre de HP à une position fixe
        batch.draw(contour_texture, 45, 30, joueur.getPointsDeVieMax() * 2, 32);  // Barre de HP max à la même position
        batch.draw(coeur_ui, (joueur.getPointsDeVieMax() * 2) + 45, 30);
        batch.draw(epee_ui, 45, 70);
        batch.draw(amure_ui, 45, 110);

        font.draw(batch, Integer.toString(joueur.getattaqueTotale()), 100, 100);
        font.draw(batch, Integer.toString(joueur.getarmureTotale()), 100, 140);

//        System.out.println(joueur.getarmureTotale());
//        System.out.println(joueur.getattaqueTotale());


        batch.draw(menu_ui_texture, screenWidth - 310, 10, 300, 150);  // Position en bas à gauche, avec un léger décalage
        batch.draw(contour_xp_texture, screenWidth - (75 + (100 * 2)), 30, joueur.getXp() * 2, 32);  // Barre de HP à une position fixe
        batch.draw(contour_texture, screenWidth - (75 + (100 * 2)), 30, joueur.getPointsDeVieMax() * 2, 32);  // Barre de HP max à la même position
        batch.draw(menu_ui_texture, screenWidth - 274, 62, 60, 30);
        batch.draw(ArgentTexture, screenWidth - 274, 95, 40, 40);
        font.draw(batch, Integer.toString(joueur.getNiveau()), screenWidth - 260, 92);
        font.draw(batch, Integer.toString(joueur.getArgent()), screenWidth - 230, 128);


        batch.draw(menu_ui_texture, 448, 0, 64, 64);
        batch.draw(menu_ui_texture, 512, 0, 64, 64);
        batch.draw(menu_ui_texture, 576, 0, 64, 64);
        batch.draw(menu_ui_texture, 640, 0, 64, 64);
        batch.draw(menu_ui_texture, 704, 0, 64, 64);
        batch.draw(menu_ui_texture, 768, 0, 64, 64);
        batch.draw(menu_ui_texture, 832, 0, 64, 64);

        batch.draw(CoupSolTexture, (448 + 8), 8, 50, 50);
        batch.draw(MeteoreTexture, 512 + 8, 8, 50, 50);
        batch.draw(MurTexture, 576 + 8, 8, 50, 50);
        batch.draw(OffensiveTexture, 640 + 8, 8, 50, 50);
        batch.draw(TerreObasobtionTexture, 704 + 8, 8, 50, 50);
        batch.draw(VortexTexture, 768 + 8, 8, 50, 50);
        batch.draw(CoupSolTexture, 832 + 8, 8, 50, 50);

        font.draw(batch, "G", 448 + 26, 92);
        font.draw(batch, "F", 512 + 26, 92);
        font.draw(batch, "J", 576 + 26, 92);
        font.draw(batch, "E", 640 + 26, 92);
        font.draw(batch, "O", 704 + 26, 92);
        font.draw(batch, "V", 768 + 26, 92);
        font.draw(batch, "/", 832 + 26, 92);


        batch.setProjectionMatrix(camera.combined);


        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            joueur.setXp(joueur.getXp() + 10f);
//            joueur.setPointsDeVie(joueur.getPointsDeVie()-10);
            // Équiper les objets


            joueur.afficherInventaire();
            joueur.afficherEquipement();

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
            joueur.desequiper(Equipement.TypeEquipement.ARME);


        }


        //-------------------------------------------------------------------
        //
        //                  DEPLACEMENT DU JOUEURS
        //
        //-------------------------------------------------------------------

        boolean moving = false;
        boolean CurrentMoving = false;
        joueur.gererDeplacement(delta, carte, walkAnimationUp, walkAnimationBottom,
            walkAnimationLeft, walkAnimationRight,
            batch, rocheTexture, joueurTexture,
            TILE_SIZE, moving, CurrentMoving, elapsedTime);



        //-------------------------------------------------------------------
        //
        //                     AFFICHAGE PSEUDO
        //
        //-------------------------------------------------------------------
        for (Entiter entite : gestionnaireEntites.getEntites()) {
            if (entite instanceof Joueur) {
                ((Joueur) entite).afficherPseudo(batch, fontPseudo);
            }
        }

        // Affichage du boss
        for (Entiter entite : gestionnaireEntites.getEntites()) {
            if (entite instanceof Boss) {
                // ((Boss) entite).afficherBoss(shapeRenderer);
                if(((Boss) entite).getId() ==2){
                    ((Boss) entite).afficherAnimation(batch, boss_2_animation,walkAnimationBottom_boss,walkAnimationLeft_boss,walkAnimationRight_boss, elapsedTime,((Boss) entite).getId()); // Remplace 'vortexAnimation' par l'animation désirée
                }else{
                    ((Boss) entite).afficherAnimation(batch, walkAnimationUp_boss,walkAnimationBottom_boss,walkAnimationLeft_boss,walkAnimationRight_boss, elapsedTime,((Boss) entite).getId()); // Remplace 'vortexAnimation' par l'animation désirée
                }
                ((Boss) entite).afficherNomBoss(batch, font);  // Affiche le nom du boss
                //((Boss) entite).afficherBoss(shapeRenderer);  // Affiche le nom du boss


            }
        }

        for (Entiter entite : gestionnaireEntites.getEntites()) {
            if (entite instanceof Monstre) {
                ((Monstre) entite).afficherAnimation(batch, elapsedTime,monstreAnimationHaut,monstreAnimationBas,monstreAnimationGauche,monstreAnimationDroite);
                ((Monstre) entite).afficherNomMonstre(batch, font);
                ((Monstre) entite).seDeplacer(joueur, carte, delta); // Déplace le monstre vers le joueur ou sa position initiale
            }
        }


        for (Entiter entite : gestionnaireEntites.getEntites()) {
            if (entite instanceof Boss) {
                if(((Boss) entite).getId() ==1){
                    ((Boss) entite).seDeplacer(joueur, carte, delta, gestionnaireEntites, batch, coupsolAnimation, meteorAnimation, elapsedTime);  // Déplacement et attaque du boss
                }
            }
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {  // Si le bouton gauche de la souris est cliqué
            // Convertir la position de la souris en coordonnées monde
            Vector2 positionClic = new Vector2(worldCoordinates.x / Carte.TAILLE_CASE, worldCoordinates.y / Carte.TAILLE_CASE);

            System.out.println("=========/" + pnj.getMapX() + "][" + pnj.getMapY() + "==============");
            System.out.println(positionClic);

            // Vérifier si le clic est sur la case du PNJ
            if (pnj.getMapX() == (int) positionClic.x && pnj.getMapY() == (int) positionClic.y) {
                // Si le joueur est à proximité du PNJ, afficher le message
                System.out.println("TESTEEEEEEEEEEEEEEEEEE");
                pnj.cliquerSurPnj(positionClic);
            }


        }


        // Affiche le dialogue du PNJ s'il est actif
        pnj.afficherDialogue(batch, fontPseudo, delta);

        if (joueurProcheDuPnj(pnj, joueur, 2)) {
            System.out.println("A COTER");// Par exemple, distance de 1 case
            pnj.changerEtat();
            pnj.afficherDialogue(batch, fontPseudo, delta);
        }







        batch.end();

        // Gérer l'entrée utilisateur pour le chat


        // Si le chat est actif, empêcher le joueur de se déplacer ou d'utiliser des compétences




        //-------------------------------------------------------------------
        //
        //                          NOS HITBOX
        //
        //-------------------------------------------------------------------

        if(hitbox==0) {

                carte.afficherObstacles(shapeRenderer);
                joueur.afficherHitbox(shapeRenderer);
                boss.afficherHitbox(shapeRenderer);
                boss2.afficherHitbox(shapeRenderer);
                pnj.afficherHitbox(shapeRenderer);
            //monstre.afficherHitbox(shapeRenderer);

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
            hitbox++;
            hitbox = hitbox % 2;


        }

        //-------------------------------------------------------------------
        //
        //             AFFICHAGE DES POINTS DE VIE DE CHAQUE ENTITÉ
        //
        //-------------------------------------------------------------------

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entiter entite : gestionnaireEntites.getEntites()) {
            entite.afficherBarreDeVie(shapeRenderer);
        }


        //---------------------------------------------------------
        //
        //              Teste minicarte
        //
        //---------------------------------------------------------






        shapeRenderer.end();
        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            afficherMiniMap = !afficherMiniMap;
            if (afficherMiniMap) {
                // Créer la fenêtre de minimap
                miniMapWindow = new MiniMapWindow(joueur, carte, skin);
                Gdx.input.setInputProcessor(miniMapWindow.getStage());
            } else if (miniMapWindow != null) {
                // Fermer la fenêtre si elle est déjà affichée
                miniMapWindow.remove();
                miniMapWindow = null;
                Gdx.input.setInputProcessor(null);
            }
        }

        // Si la fenêtre minimap est affichée, appeler son render
        if (afficherMiniMap && miniMapWindow != null) {
            miniMapWindow.render(delta);
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            afficherInventaire = !afficherInventaire;
            if (afficherInventaire) {
                Gdx.input.setInputProcessor(inventoryWindow.getStage());  // Active l'inventaire
            } else {
                Gdx.input.setInputProcessor(null);  // Désactive l'inventaire
            }
        }


        // Afficher l'inventaire si nécessaire
        if (afficherInventaire) {
            inventoryWindow.render(delta);  // Appelle le rendu de l'inventaire
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {  // Par exemple, la touche 'E' pour afficher/masquer
            equipementWindow = new EquipementWindow(joueur, skin);
            afficherEquipement = !afficherEquipement;
            if (afficherEquipement) {
                Gdx.input.setInputProcessor(equipementWindow.getStage());  // Active l'équipement
            } else {
                Gdx.input.setInputProcessor(null);  // Désactive l'équipement
            }
        }

// Afficher l'équipement si nécessaire
        if (afficherEquipement) {
            equipementWindow.render(delta);
        }


        shapeRendererMenu.end();




    }


    //-------------------------------------------------------------------
    //
    //         PERMET D ENVOYER LA TEXTURE CORRESPONDANT AU SOL
    //
    //-------------------------------------------------------------------

    Texture getTextureForType(int type) {
        switch (type) {
            case HERBE: return herbeTexture_1;
            case SABLE: return sableTexture;
            case NEIGE: return neigeTexture;
            case PAVE: return paveTexture;
            case CHEMIN: return cheminTexture;
            case CHEMIN_VILLE: return cheminVilleTexture;
            case ARBRE: return arbreTexture;
            case MONTAGNE: return montagneTexture;
            case EAU: return eauTexture;
            case GLACIER: return glacierTexture;
            case ROCHE: return rocheTexture;
            case LAVE: return laveTexture;
            case CACTUS: return cactusTexture;
            case SABLE_MOUVANT: return sableMouvantTexture;
            case TELEPORTEUR: return teleporteurTexture;
            default: return herbeTexture_1;  // Retourne "herbe" par défaut si le type n'est pas reconnu
        }
    }



    // Méthode pour vérifier si le joueur est à proximité du PNJ
    private boolean joueurProcheDuPnj(Pnj pnj, Joueur joueur, int distanceProximite) {
        int joueurX = joueur.getMapX();
        int joueurY = joueur.getMapY();
        int pnjX = pnj.getMapX();
        int pnjY = pnj.getMapY();

        // Calculer la distance entre le joueur et le PNJ
        int distanceX = Math.abs(joueurX - pnjX);
        int distanceY = Math.abs(joueurY - pnjY);

        // Si la distance entre le joueur et le PNJ est inférieure ou égale à la distance de proximité
        return distanceX <= distanceProximite && distanceY <= distanceProximite;
    }


    @Override
    public void resize(int width, int height) {
        // Ici tu peux gérer la redimension de l'écran
    }

    @Override
    public void pause() {
        // Méthode appelée lorsque le jeu est en pause
    }

    @Override
    public void resume() {
        // Méthode appelée lorsque le jeu reprend après une pause
    }

    @Override
    public void hide() {
        // Méthode appelée lorsque l'écran n'est plus visible
    }


    //-------------------------------------------------------------------
    //
    //                  ON DECHARGE LES TEXTURE
    //
    //-------------------------------------------------------------------
    @Override
    public void dispose() {
        herbeTexture_1.dispose();
        herbeTexture_2.dispose();
        herbeTexture_3.dispose();
        herbeTexture_4.dispose();

        sableTexture.dispose();
        neigeTexture.dispose();
        paveTexture.dispose();
        cheminTexture.dispose();
        cheminVilleTexture.dispose();

        arbreTexture.dispose();
        montagneTexture.dispose();
        eauTexture.dispose();
        glacierTexture.dispose();
        rocheTexture.dispose();

        laveTexture.dispose();
        cactusTexture.dispose();
        sableMouvantTexture.dispose();

        walkTextureRight.dispose();  // Libérer la texture d'animation
        walkTextureBottom.dispose();
        walkTextureLeft.dispose();
        walkTextureUp.dispose();
        joueurTexture.dispose();
        animationTexture.dispose();
        monstreTexture.dispose();
        teleporteurTexture.dispose();
        terreAbsoTexture.dispose();

        contour_texture.dispose();
        contour_hp_texture.dispose();


        for (TextureRegion frame : bossAnimation.getKeyFrames()) {
            frame.getTexture().dispose();
        }

        for (TextureRegion frame : meteorAnimation.getKeyFrames()) {
            frame.getTexture().dispose();
        }
        for (TextureRegion frame : coupsolAnimation.getKeyFrames()) {
            frame.getTexture().dispose();
        }
        for (TextureRegion frame : vortexAnimation.getKeyFrames()) {
            frame.getTexture().dispose();
        }

        for (TextureRegion frame : fireAnimation.getKeyFrames()) {
            frame.getTexture().dispose();
        }
        shapeRenderer.dispose();
        batch.dispose();  // Libérer le batch
        shapeRendererMenu.dispose();

        if (miniMapWindow != null) {
            miniMapWindow.getStage().dispose();
        }
        skin.dispose();
    }
}



