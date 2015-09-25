/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.data.Team;


public class RenderBallAtPlayerSystem extends IteratingSystem  {
    private Texture ball_red, ball_base, ball_blue;
    private final float speed = 200.0f;
    private float rotation = 0;
    String effectName;
    
    public RenderBallAtPlayerSystem(int priority, AssetManagerX assetManager) {
        super(Family.all(PositionComponent.class, PlayerComponent.class, TeamComponent.class).get(), priority);
        ball_red = assetManager.getTexture("ball_plus");
        ball_base = assetManager.getTexture("ball_base");
        ball_blue = assetManager.getTexture("ball_minus");
    }
    
    private void drawTexture(Texture texture, float x, float y, float scale,Team team) {
        int w = texture.getWidth();
        int h = texture.getHeight();
        x -= (w / 2.0f)* scale;
        y -= (h / 2.0f)* scale;
        int offsetx, offsety;
        offsetx = -50;
        offsety= -200;
        
       DrawUtil.batch.draw(texture, x + offsetx, y + offsety, (w / 2.0f) * scale  - offsetx ,(h / 2.0f) * scale - offsety, w, h, scale, scale, rotation,0, 0, w, h, false, true);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime){
        PlayerComponent player = ComponentMappers.player.get(entity);
        if(!player.hasBall) return;
      
        PositionComponent pos = ComponentMappers.position.get(entity);
        TeamComponent team = ComponentMappers.team.get(entity);
        this.rotation += deltaTime * speed;
        
        drawTexture(ball_base, pos.x,pos.y,0.6f, Team.BLUE);
        
  
        if(team.team == Team.BLUE){
             drawTexture(ball_blue, pos.x,pos.y,0.6f, Team.BLUE);
        }else{
            drawTexture(ball_red, pos.x,pos.y,0.6f, Team.BLUE);
        }
    }
    
}
