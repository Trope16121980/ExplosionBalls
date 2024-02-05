package gustavosenorans.cat.ioc.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

import java.util.Random;

import gustavosenorans.cat.ioc.helpers.AssetManager;
import gustavosenorans.cat.ioc.utils.Methods;
import gustavosenorans.cat.ioc.utils.Settings;

public class Sanwich extends Scrollable {

    private Circle collisionCircle;

    Random r;

    int assetSanwich;

    private boolean pause;
    private Action pauseAction;
    private RepeatAction repeatAction;

    public Sanwich(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);

        collisionCircle = new Circle();

        r = new Random();
        assetSanwich = r.nextInt(7);

        setOrigin();

        repeatAction = new RepeatAction();

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

        assetSanwich = r.nextInt(7);
        setOrigin();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetManager.sanwich[assetSanwich], position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
    }

    public boolean collidesSanwich(Doll nau) {

        if (position.x <= nau.getX() + nau.getWidth()) {
            return (Intersector.overlaps(collisionCircle, nau.getCollisionRect()));
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
