package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;
import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;

public abstract class SimpleComponentFactory extends ComponentFactory<EntityFactoryParam> {

    private final String type;
    private final Class clazz;

    public SimpleComponentFactory(String type, Class clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        entity.add(engine.createComponent(clazz));
    }
}
