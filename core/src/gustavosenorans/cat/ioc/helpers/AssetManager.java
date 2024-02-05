package gustavosenorans.cat.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

    // Sprite Sheet
    public static Texture sheet;

    // Background
    public static TextureRegion background;

    //Doll
    public static TextureRegion[] doll;
    public static Animation dollAnim;

    // Pelota
    public static TextureRegion[] pelota;
    public static Animation pelotaAnim;

    // Sanwich
    public static TextureRegion[] sanwich;
    public static Animation sanwichAnim;

    // Sanwich
    public static TextureRegion[] moneda;
    public static Animation monedaAnim;

    // Explosió
    public static TextureRegion[] explosion;
    public static Animation explosionAnim;

    // Sons
    public static Sound explosionSound;
    public static Sound explosionPremio;
    public static Music music;

    // Font
    public static BitmapFont font;
    public static BitmapFont fontStart;

    public static BitmapFont fontScore;


    public static TextureRegion pause;

    public static TextureRegion laserButton;
    public static TextureRegion laser;
    public static Sound laserSound;

    public static void load() {
        sheet = new Texture(Gdx.files.internal("sheet.png"));
        sheet.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);


        pause = new TextureRegion(sheet, 180, 0, 36, 36);
        pause.flip(false, true);

        laser = new TextureRegion(sheet, 216, 0, 36, 36);
        laser.flip(false, true);
        laserButton = new TextureRegion(sheet, 252, 0, 36, 36);
        laserButton.flip(false,true);

        doll = new TextureRegion[5];
        for (int i = 0; i < doll.length; i++){
            doll[i] = new TextureRegion(sheet,  i * 36, 0,36,36);
            doll[i].flip(false, true);
        }
        dollAnim = new Animation<>( 0.5f,doll );
        dollAnim.setPlayMode( Animation.PlayMode.LOOP);



        pelota = new TextureRegion[8];
        for (int i = 0; i < pelota.length; i++) {

            pelota[i] = new TextureRegion(sheet, 0, 72, 36, 36);
            pelota[i].flip(false, true);

        }

        pelotaAnim = new Animation(0.03f, pelota);
        pelotaAnim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        sanwich = new TextureRegion[7];
        for (int i = 0; i < sanwich.length; i++) {

            sanwich[i] = new TextureRegion(sheet, 288, 0, 36, 36);
            sanwich[i].flip(false, true);

        }

        sanwichAnim = new Animation(0.03f, sanwich);
        sanwichAnim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);

        moneda = new TextureRegion[8];
        for (int i = 0; i < moneda.length; i++) {

            moneda[i] = new TextureRegion(sheet, 0, 36, 36, 36);
            moneda[i].flip(false, true);

        }

        monedaAnim = new Animation(0.03f, moneda);
        monedaAnim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);



        explosion = new TextureRegion[16];

        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                explosion[index++] = new TextureRegion(sheet, j * 36,  i * 36 + 108, 36, 36);
                explosion[index-1].flip(false, true);
            }
        }

        explosionAnim = new Animation(0.03f, explosion);


        background = new TextureRegion(sheet, 0, 188, 510, 132);
        background.flip(false, true);

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        explosionPremio = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));


        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser-shot.wav"));

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Afterburner.mp3"));
        music.setVolume(0.2f);
        music.setLooping(true);

        FileHandle fontFile = Gdx.files.internal("fonts/space.fnt");
        font = new BitmapFont(fontFile, true);
        font.getData().setScale(0.4f);

        fontStart = new BitmapFont(fontFile, true);
        fontStart.getData().setScale(0.2f);

        fontScore = new BitmapFont(fontFile, true);
        fontScore.getData().setScale(0.4f);
    }

    public static void dispose() {

        sheet.dispose();
        explosionSound.dispose();
        music.dispose();

    }
}
