/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.hochschuletrier.gdw.ss15.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

/**
 *
 * @author goertzm <goertzm at hs-trier.de>
 */
public class TextureComponent extends Component implements Pool.Poolable  {
   public Texture texture;
    
    @Override
    public void reset() {
        texture = null;
    }
    
}
