package de.hochschuletrier.gdw.ss15.game.components.factories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;

public class PhysixBodyComponentFactory extends ComponentFactory<EntityFactoryParam> {

    private static final Logger logger = LoggerFactory.getLogger(PhysixBodyComponentFactory.class); 
    private PhysixSystem physixSystem;

    @Override
    public String getType() {
        return "PhysixBody";
    }

    @Override
    public void init(PooledEngine engine, AssetManagerX assetManager) {
        super.init(engine, assetManager);

        physixSystem = engine.getSystem(PhysixSystem.class);
    }

    @Override
    public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
        if(!param.allowPhysics)
            return;
        
        final PhysixModifierComponent modifyComponent = engine.createComponent(PhysixModifierComponent.class);
        modifyComponent.schedule(() -> {
            String type = properties.getString("type", "");
            switch(type) {
                case "circle": addCircle(param, entity, properties); break;
                case "box": addBox(param, entity, properties); break;
                case "player":addPlayer(param, entity,properties);break;
                case "magnet":addMagnet(param,entity,properties);break;
                default: logger.error("Unknown type: {}", type); break;
            }
        });
        entity.add(modifyComponent);
    }

    private void addMagnet(EntityFactoryParam param, Entity entity,
			SafeProperties properties) {
    	System.out.println("erstelle MagnetBody als Sensor");
    	
//       	PhysixBodyComponent magnetBody = getBodyComponent(param, entity);
//        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.StaticBody, physixSystem).position(param.x, param.y).fixedRotation(true);
////			 magnetBody.init(bodyDef, physixSystem, magnet);
//        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).shapeCircle(200).sensor(true);
//        magnetBody.createFixture(fixtureDef);
//        entity.add(magnetBody);
//    	PhysixBodyComponent magnetBody  = getBodyComponent(param, entity);
//        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyType.DynamicBody, physixSystem).position(500, 100).fixedRotation(true);
//        magnetBody.init(bodyDef, physixSystem, magnet);
    	  PhysixBodyComponent magnetBody = engine.createComponent(PhysixBodyComponent.class);
          PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody, physixSystem)
          .position(param.x, param.y).fixedRotation(false);
          magnetBody.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDefMagnetRange = new PhysixFixtureDef(physixSystem).density(5).friction(0.2f).restitution(0.4f).shapeCircle(properties.getFloat("sensor", 5)).sensor(true);
        magnetBody.createFixture(fixtureDefMagnetRange);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(5).friction(0.2f).restitution(0.4f).shapeCircle(properties.getFloat("size", 5));
        magnetBody.createFixture(fixtureDef);
        
        entity.add(magnetBody);

		
	}
    private void addCircle(EntityFactoryParam param, Entity entity, SafeProperties properties) {
        PhysixBodyComponent bodyComponent = getBodyComponent(param, entity);
        PhysixFixtureDef fixtureDef = getFixtureDef(properties)
                .shapeCircle(properties.getFloat("size", 5));
        bodyComponent.createFixture(fixtureDef);
       // bodyComponent.applyImpulse(0, 50000);
        entity.add(bodyComponent);
        
        System.out.println("test");
    }

	private void addPlayer(EntityFactoryParam param, Entity entity, SafeProperties properties){
	    PhysixBodyComponent playerBody = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef playerDef = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, physixSystem).position(param.x, param.y).fixedRotation(true);
        
        playerBody.init(playerDef, physixSystem, entity);
      
        
        
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem).density(100).friction(0.0f).restitution(0.0f).shapeCircle(40.0f);
        playerBody.createFixture(fixtureDef);
        
        fixtureDef = new PhysixFixtureDef(physixSystem).density(100).friction(0.0f).restitution(0.0f).shapeCircle(40.0f, new Vector2(-5,45));
        playerBody.createFixture(fixtureDef);
        
        fixtureDef = new PhysixFixtureDef(physixSystem).density(100).friction(0.0f).restitution(0.0f).shapeCircle(20.0f, new Vector2(50,55));
        playerBody.createFixture(fixtureDef);
        
        fixtureDef = new PhysixFixtureDef(physixSystem).density(100).friction(0.0f).restitution(0.0f).shapeCircle(20.0f, new Vector2(-50,45));
        playerBody.createFixture(fixtureDef);
        
        fixtureDef = new PhysixFixtureDef(physixSystem).density(100).friction(0.0f).restitution(0.0f).shapeCircle(30.0f, new Vector2(0,-50));
        playerBody.createFixture(fixtureDef);
      
      entity.add(playerBody);
	}


    private void addBox(EntityFactoryParam param, Entity entity, SafeProperties properties) {
        PhysixBodyComponent bodyComponent = getBodyComponent(param, entity);
        PhysixFixtureDef fixtureDef = getFixtureDef(properties)
                .shapeBox(properties.getFloat("size", 5), properties.getFloat("size", 5));
        bodyComponent.createFixture(fixtureDef);
        // bodyComponent.applyImpulse(0, 500);
        entity.add(bodyComponent);
    }

    private PhysixBodyComponent getBodyComponent(EntityFactoryParam param, Entity entity) {
        PhysixBodyComponent bodyComponent = engine.createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = getBodyDef(param);
        bodyComponent.init(bodyDef, physixSystem, entity);
        return bodyComponent;
    }

    private PhysixBodyDef getBodyDef(EntityFactoryParam param) {
        return new PhysixBodyDef(BodyDef.BodyType.DynamicBody, physixSystem)
                .position(param.x, param.y).fixedRotation(false)
                .linearDamping(0.25f).angularDamping(0.25f);
    }

    private PhysixFixtureDef getFixtureDef(SafeProperties properties) {
        return new PhysixFixtureDef(physixSystem)
                .density(properties.getFloat("density", 5))
                .friction(properties.getFloat("friction", 5))
                .restitution(properties.getFloat("restitution", 0))
                ;
    }
}
