package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.ChangeGameStateEvent;
import de.hochschuletrier.gdw.ss15.events.ScoreChangedEvent;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.data.GameState;

/**
 * This class is rendering the HUD with the update method
 *
 * @author meiert
 *
 */
public class HudRenderSystem extends EntitySystem implements ScoreChangedEvent.Listener,
        ChangeGameStateEvent.Listener {

    private final BitmapFont font;
    private String scoreRed = "0";
    private String scoreBlue = "0";
    private float countdown;

    public HudRenderSystem(AssetManagerX assetManager, int priority) {
        super(priority);
        font = assetManager.getFont("quartz_50");
    }

    @Override
    public void addedToEngine(Engine engine) {
        ScoreChangedEvent.register(this);
        ChangeGameStateEvent.register(this);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        ScoreChangedEvent.unregister(this);
        ChangeGameStateEvent.unregister(this);
    }

    @Override
    public void update(float deltaTime) {
        countdown -= deltaTime;
        if(countdown <= 0)
            countdown = 0;

        Main.getInstance().screenCamera.bind();

        String time = "time:" + (int)Math.ceil(countdown);
        font.setColor(Color.RED);
        font.draw(DrawUtil.batch, scoreRed, 50, 50);
        font.setColor(Color.BLUE);
        font.draw(DrawUtil.batch, scoreBlue, Gdx.graphics.getWidth() - 200, 50);
        font.setColor(Color.GREEN);
        font.draw(DrawUtil.batch, time, Gdx.graphics.getWidth() / 2 - 100, 50);
    }

    @Override
    public void onScoreChangedEvent(int scoreBlue, int scoreRed) {
        this.scoreBlue = "BLUE " + scoreBlue;
        this.scoreRed = "RED " + scoreRed;
    }

    @Override
    public void onChangeGameStateEvent(GameState newState, float gameTime) {
        countdown = gameTime;
    }
}
