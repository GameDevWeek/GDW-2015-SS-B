package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.data.Team;
import de.hochschuletrier.gdw.ss15.game.manager.TeamManager;

/**
 * Not a system but must be called every frame, so it is close to a System ;)
 * 
 * This class is rendering the HUD with the update method
 * 
 * @author meiert
 *
 */

public class HudRender {
    
    private SmoothCamera camera;
    private BitmapFont font;
    private int goalred = 0;
    private int goalblue = 0;
    
    private TeamManager teamManager = new TeamManager();
    
    
    public HudRender(SmoothCamera camera) {
    	
       
        this.camera = camera;
    }
    
    public void update() {
        Main.getInstance().screenCamera.bind();
        
        /* 
         * new fliped bitmapFont to display the hud
         * have not found a way to clear a bitmapFont so every frame a new one is needed
         */
        //font = new BitmapFont(true);
        font = new BitmapFont(Gdx.files.internal("data/fonts/quartz_50.fnt"),true);
        font.setUseIntegerPositions(false);
        font.setScale(1f);
        
        
        
        // EXAMPLE how to display text for the hud //
        
        String tmp = "BLUE "+ teamManager.getScore(Team.BLUE);
        String tmp1 = "RED "+teamManager.getScore(Team.RED);
        String time = "time:";
        font.setColor(Color.RED);
        font.draw(DrawUtil.batch, tmp, 50, 50);
        font.setColor(Color.BLUE);
        font.draw(DrawUtil.batch, tmp1,Gdx.graphics.getWidth()-200, 50);
        font.setColor(Color.GREEN);
        font.draw(DrawUtil.batch, time, Gdx.graphics.getWidth()/2, 50);
//        font.draw(DrawUtil.batch,"" )
        
    }
}
