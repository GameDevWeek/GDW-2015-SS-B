package de.hochschuletrier.gdw.ss15.game;

import com.badlogic.ashley.core.ComponentMapper;

import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ss15.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ss15.game.components.BallComponent;
import de.hochschuletrier.gdw.ss15.game.components.BallParticlesComponent;
import de.hochschuletrier.gdw.ss15.game.components.BallSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.ChainLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.GoalEffectComponent;
import de.hochschuletrier.gdw.ss15.game.components.GoalShotComponent;
import de.hochschuletrier.gdw.ss15.game.components.ImpactSoundComponent;
import de.hochschuletrier.gdw.ss15.game.components.InputBallComponent;
import de.hochschuletrier.gdw.ss15.game.components.MagneticFieldComponent;
import de.hochschuletrier.gdw.ss15.game.components.MagneticInfluenceComponent;
import de.hochschuletrier.gdw.ss15.game.components.MovableComponent;
import de.hochschuletrier.gdw.ss15.game.components.NotReceptiveComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ss15.game.components.PlayerSpawnComponent;
import de.hochschuletrier.gdw.ss15.game.components.PointLightComponent;
import de.hochschuletrier.gdw.ss15.game.components.PositionComponent;
import de.hochschuletrier.gdw.ss15.game.components.SetupComponent;
import de.hochschuletrier.gdw.ss15.game.components.SoundEmitterComponent;
import de.hochschuletrier.gdw.ss15.game.components.StateRelatedAnimationsComponent;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;
import de.hochschuletrier.gdw.ss15.game.components.TextureComponent;
import de.hochschuletrier.gdw.ss15.game.components.TriggerComponent;

public class ComponentMappers {
    public static final ComponentMapper<BallComponent> ball = ComponentMapper.getFor(BallComponent.class);
    public static final ComponentMapper<GoalShotComponent> goalShot = ComponentMapper.getFor(GoalShotComponent.class);
    public static final ComponentMapper<TeamComponent> team = ComponentMapper.getFor(TeamComponent.class);
    public static final ComponentMapper<SoundEmitterComponent> soundEmitter = ComponentMapper.getFor(SoundEmitterComponent.class);
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<TriggerComponent> trigger = ComponentMapper.getFor(TriggerComponent.class);
    public static final ComponentMapper<PhysixBodyComponent> physixBody = ComponentMapper.getFor(PhysixBodyComponent.class);
    public static final ComponentMapper<PhysixModifierComponent> physixModifier = ComponentMapper.getFor(PhysixModifierComponent.class);
    public static final ComponentMapper<ImpactSoundComponent> impactSound = ComponentMapper.getFor(ImpactSoundComponent.class);
    public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<StateRelatedAnimationsComponent> stateRelatedAnimations = ComponentMapper.getFor(StateRelatedAnimationsComponent.class);
    public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<SetupComponent> setup = ComponentMapper.getFor(SetupComponent.class);
    public static final ComponentMapper<InputBallComponent> input = ComponentMapper.getFor(InputBallComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<MagneticInfluenceComponent> magneticInfluence = ComponentMapper.getFor(MagneticInfluenceComponent.class);
    public static final ComponentMapper<MagneticFieldComponent> magneticField = ComponentMapper.getFor(MagneticFieldComponent.class);
    public static final ComponentMapper<BallParticlesComponent> ballParticles = ComponentMapper.getFor(BallParticlesComponent.class);
     public static final ComponentMapper<GoalEffectComponent> goalEffect = ComponentMapper.getFor(GoalEffectComponent.class);
    
    public static final ComponentMapper<MovableComponent> movable = ComponentMapper.getFor(MovableComponent.class);
    public static final ComponentMapper<NotReceptiveComponent>notReceptive=ComponentMapper.getFor(NotReceptiveComponent.class);

    /* Spawnpunkte für die Spieler */
    public static final ComponentMapper<PlayerSpawnComponent> playerSpawn = ComponentMapper.getFor(PlayerSpawnComponent.class);

    /* Spawnpunkte für den Ball */
    public static final ComponentMapper<BallSpawnComponent> ballSpawn = ComponentMapper.getFor(BallSpawnComponent.class);
    public static final ComponentMapper<PointLightComponent> pointLight = ComponentMapper.getFor(PointLightComponent.class);
    public static final ComponentMapper<ChainLightComponent> chainLight = ComponentMapper.getFor(ChainLightComponent.class);
}
