package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.SmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;

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
    
    public HudRender(SmoothCamera camera) {
    	
       
        this.camera = camera;
    }
    
    public void update() {
        Main.getInstance().screenCamera.bind();
        
        /* 
         * new fliped bitmapFont to display the hud
         * have not found a way to clear a bitmapFont so every frame a new one is needed
         */
        font = new BitmapFont(true);
       
        
        
        // EXAMPLE how to display text for the hud // 
        String tmp = "team"+"var";
        font.setColor(Color.RED);
        font.draw(DrawUtil.batch, tmp, 100, 50);
        font.setColor(Color.BLUE);
        font.draw(DrawUtil.batch, tmp, 500, 50);
//        font.draw(DrawUtil.batch,"" )
        
    }
}
