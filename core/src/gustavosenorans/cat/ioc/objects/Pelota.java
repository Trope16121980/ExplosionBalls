package gustavosenorans.cat.ioc.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;

import java.util.Random;

import gustavosenorans.cat.ioc.helpers.AssetManager;
import gustavosenorans.cat.ioc.utils.Methods;
import gustavosenorans.cat.ioc.utils.Settings;

public class Pelota extends Scrollable {

    private Circle collisionCircle;

    Random r;

    int assetPelota;

    private boolean pause;
    private Action pauseAction;
    private RepeatAction repeatAction;


    public Pelota(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);

        collisionCircle = new Circle();

        r = new Random();
        assetPelota = r.nextInt(8);

        setOrigin();

        RotateByAction rotateAction = new RotateByAction();
        rotateAction.setAmount(-90f);
        rotateAction.setDuration(0.2f);

        repeatAction = new RepeatAction();
        repeatAction.setAction(rotateAction);
        repeatAction.setCount(RepeatAction.FOREVER);

        this.addAction(repeatAction);

        pause = false;
    }

    public void setOrigin() {

        this.setOrigin(width/2 + 1, height/2);

    }

    @Override
    public void act(float delta) {
        if(!pause){
            super.act(delta);
            collisionCircle.set(position.x + width / 2.0f, position.y + width / 2.0f, width / 2.0f);
        }
    }

    public void reset() {
        float newSize = Methods.randomFloat(Settings.MIN_PELOTA, Settings.MAX_PELOTA);
        width = height = 34 * newSize;
        float newX = Settings.GAME_WIDTH + width;

        super.reset(newX);
        position.y =  new Random().nextInt(Settings.GAME_HEIGHT - (int) height);

        assetPelota = r.nextInt(8);
        setOrigin();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetManager.pelota[assetPelota], position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
    }

    public boolean collides(Doll nau) {

        if (position.x <= nau.getX() + nau.getWidth()) {
            return (Intersector.overlaps(collisionCircle, nau.getCollisionRect()));
        }
        return false;
    }

    public boolean collides(Laser laser) {

        if (position.x <= laser.getX() + laser.getWidth()) {
            return (Intersector.overlaps(collisionCircle, laser.getCollisionRect()));
        }
        return false;
    }

    public void startPause() {
        pause = true;
        pauseAction = Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.alpha(0.5f, 0.2f), Actions.alpha(1.0f, 0.2f)));
        this.addAction(pauseAction);
        this.removeAction(repeatAction);
    }
    public void stopPause() {
        pause = false;
        this.clearActions();
        this.addAction(repeatAction);
        Color color = this.getColor();
        this.setColor(color.r, color.g, color.b, 1.0f);
    }

}
