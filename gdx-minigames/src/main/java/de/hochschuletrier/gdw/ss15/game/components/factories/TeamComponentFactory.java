package de.hochschuletrier.gdw.ss15.game.components.factories;

import com.badlogic.ashley.core.Entity;

import de.hochschuletrier.gdw.commons.gdx.ashley.ComponentFactory;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.commons.utils.SafeProperties;
import de.hochschuletrier.gdw.ss15.game.components.TeamComponent;

public class TeamComponentFactory extends ComponentFactory<EntityFactoryParam> {

	@Override
	public String getType() {
		return "Team";
	}

	@Override
	public void run(Entity entity, SafeProperties meta, SafeProperties properties, EntityFactoryParam param) {
		final TeamComponent teamComponent = engine.createComponent(TeamComponent.class);
		teamComponent.team = param.team;
		
		entity.add(teamComponent);
		
	}

}
