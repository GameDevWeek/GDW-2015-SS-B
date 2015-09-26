package de.hochschuletrier.gdw.ss15.game.components.factories;


import box2dLight.PointLight;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.PointLightComponent;
import de.hochschuletrier.gdw.ss15.game.systems.LightRenderSystem;

public class PointLightComponentFactory extends ComponentFactory<EntityFactoryParam> {

    @Override
    public String getType() {
        return "PointLight";
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        PointLightComponent plc = engine.createComponent(PointLightComponent.class);
        Color color = Color.valueOf(properties.getString("color", "FFFFFF"));
        float distance = properties.getFloat("distance", 250) / GameConstants.BOX2D_SCALE;
        boolean staticLight = properties.getBoolean("static", true);
        plc.pointLight = new PointLight(engine.getSystem(LightRenderSystem.class).getRayHandler(),
                GameConstants.LIGHT_RAYS, color,distance,0,0);
        plc.pointLight.setStaticLight(staticLight);
        if(!properties.getBoolean("active", true))
            plc.pointLight.setActive(false);
        entity.add(plc);
    }

}
