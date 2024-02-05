package gustavosenorans.cat.ioc.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;

import gustavosenorans.cat.ioc.helpers.AssetManager;
import gustavosenorans.cat.ioc.utils.Settings;

public class Doll extends Actor {

    public static final int DOLL_STRAIGHT = 0;
    public static final int DOLL_UP = 1;
    public static final int DOLL_DOWN = 2;

    private Action pauseAction;
    private boolean pause;

    private final Vector2 position;
    private final int width;
    private final int height;
    private int direction;

    private Rectangle collisionRect;

    public Doll(float x, float y, int width, int height) {

        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        direction = DOLL_STRAIGHT;

        collisionRect = new Rectangle();

        setBounds(position.x, position.y, width, height);
        setTouchable(Touchable.enabled);

        pause = false;
    }

    public void startPause() {
        pause = true;
        pauseAction = Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.alpha(0.5f, 0.2f), Actions.alpha(1.0f, 0.2f)));
        this.addAction(pauseAction);
    }
    public void stopPause() {
        pause = false;
        this.removeAction(pauseAction);
        Color color = this.getColor();
        this.setColor(color.r, color.g, color.b, 1.0f);
    }

    public void act(float delta) {
        super.act(delta);
        if(!pause) {

            switch (direction) {
                case DOLL_UP:
                    if (this.position.y - Settings.DOLLS_VELOCITY * delta >= 0) {
                        this.position.y -= Settings.DOLLS_VELOCITY * delta;
                    }
                    break;
                case DOLL_DOWN:
                    if (this.position.y + height + Settings.DOLLS_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                        this.position.y += Settings.DOLLS_VELOCITY * delta;
                    }
                    break;
                case DOLL_STRAIGHT:
                    break;
            }

            collisionRect.set(position.x , position.y + 5, width, 10);
            setBounds(position.x, position.y, width, height);
        }

    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void goUp() {
        direction = DOLL_UP;
    }

    public void goDown() {
        direction = DOLL_DOWN;
    }

    public void goStraight() {
        direction = DOLL_STRAIGHT;
    }


    public void reset() {

        position.x = Settings.DOLLS_STARTX;
        position.y = Settings.DOLLS_STARTY;
        direction = DOLL_STRAIGHT;
        collisionRect = new Rectangle();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a);
        batch.draw((TextureRegion) AssetManager.dollAnim.getKeyFrame( direction, true ), position.x, position.y, width,height);
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }
}
