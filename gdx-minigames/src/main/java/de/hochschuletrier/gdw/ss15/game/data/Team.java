package de.hochschuletrier.gdw.ss15.game.data;

import com.badlogic.gdx.graphics.Color;

public enum Team {
    BLUE("0078E8"),
    RED("F00303");
    
    public final Color color;
    Team(String color) {
        this.color = Color.valueOf(color);
    }
}
