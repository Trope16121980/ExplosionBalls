package gustavosenorans.cat.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import gustavosenorans.cat.ioc.helpers.AssetManager;


public class Laser extends Scrollable {

    private Rectangle collisionRect;

    protected boolean pause;
    private Action pauseAction;

    public Laser(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);

        collisionRect = new Rectangle();

        pause = false;

    }

    public void startPause() {
        pause = true;
        pauseAction = Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.alpha(0.2f, 0.2f), Actions.alpha(1.0f, 0.2f)));
        this.addAction(pauseAction);
    }
    public void stopPause() {
        pause = false;
        this.removeAction(pauseAction);
    }

    @Override
    public void act(float delta) {
        if(!pause){
            super.act(delta);
            collisionRect.set(position.x, position.y, width, 2);
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetManager.laser, position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }
}
