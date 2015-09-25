package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;

/**
 * This class is rendering the HUD with the update method
 *
 * @author meiert
 *
 */
public class HudRenderSystem extends EntitySystem {

    private final BitmapFont font;
    private int scoreRed;
    private int scoreBlue;

    private float playTime;
    
    public HudRenderSystem(AssetManagerX assetManager, int priority) {
        super(priority);
        font = assetManager.getFont("quartz_50");
    }

    @Override
    public void update(float deltaTime) {
    	playTime+=deltaTime;
    	
        Main.getInstance().screenCamera.bind();
        
        /* 
         * new fliped bitmapFont to display the hud
         * have not found a way to clear a bitmapFont so every frame a new one is needed
         */
        //font = new BitmapFont(true);

        String tmp = "BLUE " + scoreBlue;
        String tmp1 = "RED " + scoreRed;
        String time = "time:" + (int)playTime;
        font.setColor(Color.BLUE);
        font.draw(DrawUtil.batch, tmp, 50, 50);
        font.setColor(Color.RED);
        font.draw(DrawUtil.batch, tmp1,Gdx.graphics.getWidth()-200, 50);
        font.setColor(Color.GREEN);
        font.draw(DrawUtil.batch, time, Gdx.graphics.getWidth()/2-100, 50);
    }
}
