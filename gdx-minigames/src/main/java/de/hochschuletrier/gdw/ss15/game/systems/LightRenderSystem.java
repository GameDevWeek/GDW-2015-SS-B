package de.hochschuletrier.gdw.ss15.game.systems;

import box2dLight.ChainLight;
import box2dLight.ConeLight;
import box2dLight.PointLight;
import com.badlogic.ashley.systems.IteratingSystem;
import de.hochschuletrier.gdw.ss15.game.ComponentMappers;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.components.PointLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Filter;

import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarBool;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarFloat;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarInt;
import de.hochschuletrier.gdw.commons.devcon.cvar.ICVarListener;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.ChangeAnimationStateEvent;
import de.hochschuletrier.gdw.ss15.game.components.ChainLightComponent;
import de.hochschuletrier.gdw.ss15.game.data.EntityAnimationState;
import de.hochschuletrier.gdw.ss15.game.data.Team;

public class LightRenderSystem extends IteratingSystem implements ChangeAnimationStateEvent.Listener, EntityListener {

    public RayHandler rayHandler;
    private final Matrix4 scaleMatrix = new Matrix4();
    private final CVarFloat ambientLight = new CVarFloat("light_ambient", 0.5f, 0, 1, 0, "Ambient light value");
    private final CVarBool culling = new CVarBool("light_culling", true, 0, "Light Culling");
    private final CVarBool shadows = new CVarBool("light_shadows", true, 0, "Light Shadows");
    private final CVarInt blur = new CVarInt("light_blur", 2, 0, 3, 0, "Light blur value");
    private final LimitedSmoothCamera camera;
    private final DevConsole console;

    public LightRenderSystem(LimitedSmoothCamera camera, PhysixSystem physixSystem, int priority) {
        super(Family.one(PointLightComponent.class).get(), priority);

        this.camera = camera;
        rayHandler = new RayHandler(physixSystem.getWorld());;

        rayHandler.setAmbientLight(GameConstants.LIGHT_AMBIENT);
        rayHandler.setBlur(GameConstants.LIGHT_BLUR);
        rayHandler.setBlurNum(GameConstants.LIGHT_BLURNUM);
        rayHandler.setShadows(GameConstants.LIGHT_SHADOW);
        rayHandler.useDiffuseLight(GameConstants.LIGHT_DIFFUSE);
        Filter lightfilter = new Filter();
        lightfilter.categoryBits = GameConstants.MASK_WORLDSENSOR;
        lightfilter.maskBits = (short) (GameConstants.MASK_EVERYTHING & ~GameConstants.MASK_WORLDSENSOR);
        lightfilter.groupIndex = -1;
        PointLight.setContactFilter(lightfilter);
        console = Main.getInstance().console;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        ChangeAnimationStateEvent.register(this);

        addCVar(ambientLight, this::onAmbientLightChange);
        addCVar(culling, this::onCullingChange);
        addCVar(blur, this::onBlurChange);
        addCVar(shadows, this::onShadowsChange);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        super.removedFromEngine(engine);

        ChangeAnimationStateEvent.unregister(this);
        console.unregister(ambientLight);
        console.unregister(culling);
        console.unregister(blur);
        console.unregister(shadows);
        rayHandler.removeAll();
    }

    private void addCVar(CVar cvar, ICVarListener listener) {
        console.register(cvar);
        cvar.addListener(listener);
        listener.modified(cvar);
    }

    private void onAmbientLightChange(CVar cvar) {
        rayHandler.setAmbientLight(ambientLight.get());
    }

    private void onCullingChange(CVar cvar) {
        rayHandler.setCulling(culling.get());
    }

    private void onBlurChange(CVar cvar) {
        rayHandler.setBlurNum(blur.get());
    }

    private void onShadowsChange(CVar cvar) {
        rayHandler.setShadows(shadows.get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        DrawUtil.batch.end();
        updateRayHandler();
        DrawUtil.batch.begin();
    }

    private void updateRayHandler() {
        OrthographicCamera camera = this.camera.getOrthographicCamera();
        scaleMatrix.set(camera.combined).scl(GameConstants.BOX2D_SCALE);
        rayHandler.setCombinedMatrix(scaleMatrix,
                camera.position.x / GameConstants.BOX2D_SCALE,
                camera.position.y / GameConstants.BOX2D_SCALE,
                camera.viewportWidth * camera.zoom / GameConstants.BOX2D_SCALE,
                camera.viewportHeight * camera.zoom / GameConstants.BOX2D_SCALE);
        rayHandler.updateAndRender();
    }

    public RayHandler getRayHandler() {
        return this.rayHandler;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = ComponentMappers.position.get(entity);
        PointLightComponent pointLight = ComponentMappers.pointLight.get(entity);
        ChainLightComponent chainLight = ComponentMappers.chainLight.get(entity);

        if (pointLight != null) {
            pointLight.pointLight.setPosition(position.x / GameConstants.BOX2D_SCALE,
                    position.y / GameConstants.BOX2D_SCALE);
        }
        
        if(chainLight != null){
            chainLight.chainLight.setPosition((position.x)/GameConstants.BOX2D_SCALE,(position.y)/GameConstants.BOX2D_SCALE);
        }
    }

    @Override
    public void onAnimationStateChangedEvent(EntityAnimationState newState, Entity entity) {
        final PointLightComponent pointLight = ComponentMappers.pointLight.get(entity);
        if (newState != null && pointLight != null) {
            switch (newState) {
                case BALL_MINUS:
                    pointLight.pointLight.setColor(Team.BLUE.color);
                    break;
                case BALL_PLUS:
                    pointLight.pointLight.setColor(Team.RED.color);
                    break;
                case BALL_NEUTRAL:
                    pointLight.pointLight.setColor(Color.WHITE);
                    break;
            }
        }
    }

    @Override
    public void entityAdded(Entity entity) {
        final PointLightComponent pointLight = ComponentMappers.pointLight.get(entity);
        final PhysixBodyComponent physixBody = ComponentMappers.physixBody.get(entity);
        if (pointLight != null && physixBody != null) {
            pointLight.pointLight.attachToBody(physixBody.getBody());
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
    }
}
