package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.MagneticFieldComponent;

public class MagneticFieldComponentFactory extends ComponentFactory<EntityFactoryParam>  {



    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return "MagneticField";
    }

    @Override
    public void run(Entity entity, SafeProperties meta,
            SafeProperties properties, EntityFactoryParam param) {
        // TODO Auto-generated method stub
        MagneticFieldComponent  component = engine.createComponent(MagneticFieldComponent.class);
        component.range =properties.getFloat("range" ,0);
        
        assert (component.range !=0);
        entity.add(component);
    }
}
