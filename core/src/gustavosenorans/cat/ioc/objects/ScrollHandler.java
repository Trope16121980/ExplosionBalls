package gustavosenorans.cat.ioc.objects;

import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.Random;

import gustavosenorans.cat.ioc.utils.Methods;
import gustavosenorans.cat.ioc.utils.Settings;

public class ScrollHandler extends Group {

    Background bg, bg_back;

    private ArrayList<Pelota> pelotas;
    private ArrayList<Sanwich> sanwiches;
    private ArrayList<Moneda> monedas;

    private boolean pause;

    private float time;

    Random r;
    private Doll doll;

    public ScrollHandler() {

        bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);
        bg_back = new Background(bg.getTailX(), 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);

        addActor(bg);
        addActor(bg_back);

        r = new Random();

        pelotas = new ArrayList<Pelota>();
        sanwiches = new ArrayList<Sanwich>();
        monedas = new ArrayList<Moneda>();

        time = 0;
        pause = false;

        newPelota();
        newSanwich();
        newMonedas();



    }

    public void newPelota(){
        float newSize = Methods.randomFloat(Settings.MIN_PELOTA, Settings.MAX_PELOTA) * 34;

        Pelota pelota = new Pelota(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.PELOTA_SPEED);
        pelotas.add( pelota );
        addActor( pelota );
    }
    public void removePelota(Pelota pelota){
        pelotas.remove( pelota );
        pelota.remove();
    }

    public void newSanwich(){
        float newSize = Methods.randomFloat(Settings.MIN_PELOTA, Settings.MAX_PELOTA) * 34;

        Sanwich sanwich = new Sanwich(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.SANWICH_SPEED);
        sanwiches.add( sanwich );
        addActor( sanwich );
    }
    public void removeSanwich(Sanwich sanwich){
        sanwiches.remove( sanwich );
        sanwich.remove();
    }

    public void newMonedas(){
        float newSize = Methods.randomFloat(Settings.MIN_PELOTA, Settings.MAX_PELOTA) * 34;

        Moneda moneda = new Moneda(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.MONEDA_SPEED);
        monedas.add( moneda );
        addActor( moneda );
    }
    public void removeMoneda(Moneda moneda){
        monedas.remove( moneda );
        moneda.remove();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (bg.isLeftOfScreen()) {
            bg.reset(bg_back.getTailX());

        } else if (bg_back.isLeftOfScreen()) {
            bg_back.reset(bg.getTailX());

        }

        for (int i = 0; i < pelotas.size(); i++) {

            Pelota pelota = pelotas.get(i);
            if (pelota.isLeftOfScreen()) {
                pelota.reset();
            }
        }

        for (int i = 0; i < sanwiches.size(); i++) {

            Sanwich sanwich = sanwiches.get(i);
            if (sanwich.isLeftOfScreen()) {
                sanwich.reset();
            }
        }

        for (int i = 0; i < monedas.size(); i++) {

            Moneda moneda = monedas.get(i);
            if (moneda.isLeftOfScreen()) {
                moneda.reset();
            }
        }


        if(pause) time = 0;
        if(time > 1f && monedas.size() < 1){
            newMonedas();
            time = 0;
        }else time += delta;
        if(time > 1f && sanwiches.size() < 2){
            newSanwich();
            time = 0;
        }else time += delta;
        if(time > 1f && pelotas.size() < 4){
            newPelota();
            time = 0;
        }else time += delta;
    }

    public boolean collides(Doll doll) {
        this.doll = doll;

        for (Pelota pelota : pelotas) {
            if (pelota.collides(doll)) {
                return true;
            }
        }
        return false;
    }
    public boolean collidesSanwich(Doll doll) {
        this.doll = doll;

        for (Sanwich sanwich : sanwiches) {
            if (sanwich.collidesSanwich(doll)) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesMonedas(Doll doll) {
        this.doll = doll;

        for (Moneda moneda : monedas) {
            if (moneda.collidesMonedas(doll)) {
                return true;
            }
        }
        return false;
    }

    public Pelota collides(Laser laser) {

        for (Pelota pelota : pelotas) {
            if (pelota.collides(laser)) {
                return pelota;
            }
        }
        return null;
    }



    public void reset() {

        pelotas.get(0).reset(Settings.GAME_WIDTH);
        for (int j = 1; j < pelotas.size(); j++) {

            pelotas.get(j).reset( pelotas.get(j - 1).getTailX() + Settings.PELOTA_GAP);

        }

        sanwiches.get(0).reset(Settings.GAME_WIDTH);
        for (int j = 1; j < sanwiches.size(); j++) {

            sanwiches.get(j).reset( sanwiches.get(j - 1).getTailX() + Settings.PELOTA_GAP);

        }
        monedas.get(0).reset(Settings.GAME_WIDTH);
        for (int j = 1; j < monedas.size(); j++) {

            monedas.get(j).reset( monedas.get(j - 1).getTailX() + Settings.PELOTA_GAP);

        }
    }

    public ArrayList<Pelota> getPelotas() {
        return pelotas;
    }
    public ArrayList<Sanwich> getSanwiches() {
        return sanwiches;
    }
    public ArrayList<Moneda> getMonedas() {
        return monedas;
    }

    public void setPause(){
        pause = true;
        bg.startPause();
        bg_back.startPause();
        for (Pelota a : pelotas){
            a.startPause();
        }
        for (Sanwich a : sanwiches){
            a.startPause();
        }
        for (Moneda a : monedas){
            a.startPause();
        }
    }
    public void stopPause(){
        pause = false;
        bg.stopPause();
        bg_back.stopPause();
        for (Pelota a : pelotas){
            a.stopPause();
        }
        for (Sanwich a : sanwiches){
            a.stopPause();
        }
        for (Moneda a : monedas){
            a.stopPause();
        }
    }

}