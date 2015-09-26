package de.hochschuletrier.gdw.ss15.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TextManager {
private BitmapFont bfont;
private java.lang.CharSequence text;
private float x;
private float y;
private float transparency = 0;
private float gravity = 0.07F;

public TextManager(java.lang.CharSequence mag, float x, float y){
bfont = new BitmapFont(Gdx.files.internal("Entities/Scene2D/damage.font"));
mag = text;
this.x = (x + bfont.getBounds(mag).width/2.0F);
this.y = y;

}
public void update(){
transparency=0.0F;
bfont.setColor(255, 255, 255, 0);

}

public void render(Batch batch) {
	bfont.draw(batch,text,x,y);
	}
public void dispose(){
	bfont.dispose();
}
}