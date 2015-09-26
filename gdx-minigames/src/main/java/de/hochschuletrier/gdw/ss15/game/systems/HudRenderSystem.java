package de.hochschuletrier.gdw.ss15.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.events.ChangeGameStateEvent;
import de.hochschuletrier.gdw.ss15.events.ScoreChangedEvent;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.data.GameState;
import de.hochschuletrier.gdw.ss15.game.data.Team;

/**
 * This class is rendering the HUD with the update method
 *
 * @author meiert
 *
 */
public class HudRenderSystem extends EntitySystem implements
		ScoreChangedEvent.Listener, ChangeGameStateEvent.Listener {
	private String winner;
	private final BitmapFont font;
	private String scoreRed = "0";
	private String scoreBlue = "0";
	private float countdown;
	private GameState gamestate;

	// private PooledEngine engine;

	public HudRenderSystem(AssetManagerX assetManager, int priority) {
		super(priority);
		font = assetManager.getFont("quartz_50");
	}

	@Override
	public void addedToEngine(Engine engine) {
		// this.engine=(PooledEngine) engine;
		ScoreChangedEvent.register(this);
		ChangeGameStateEvent.register(this);
	}

	@Override
	public void removedFromEngine(Engine engine) {

		ScoreChangedEvent.unregister(this);
		ChangeGameStateEvent.unregister(this);
		// this.engine=null;
	}

	public void winner() {

	}

	@Override
	public void update(float deltaTime) {
		countdown -= deltaTime;

		if (countdown <= 0)
			countdown = 0;

		Main.getInstance().screenCamera.bind();
		String warmup = "WARMUP";
		String gameover = winner + " Victory";
		String time = "time:" + (int) Math.ceil(countdown);

		if (gamestate != null) {
			switch (gamestate) {
			case WARMUP:
				font.setColor(Team.RED.color);
				font.draw(DrawUtil.batch, scoreBlue, 50, 50);
				font.setColor(Team.BLUE.color);
				font.draw(DrawUtil.batch, scoreRed,
						Gdx.graphics.getWidth() - 200, 50);
				font.setColor(Color.GREEN);
				font.draw(DrawUtil.batch, warmup,
						Gdx.graphics.getWidth() / 2 - 100, 50);
				break;
			case THREE:
				// fallthrough
			case TWO:
				// fallthrough
			case ONE:
				font.setColor(Team.RED.color);
				font.draw(DrawUtil.batch, scoreBlue, 50, 50);
				font.setColor(Team.BLUE.color);
				font.draw(DrawUtil.batch, scoreRed,
						Gdx.graphics.getWidth() - 200, 50);
				font.setColor(Color.GREEN);
				font.draw(DrawUtil.batch, time,
						Gdx.graphics.getWidth() / 2 - 100, 50);
				break;
			case GAME:
				font.setColor(Team.RED.color);
				font.draw(DrawUtil.batch, scoreBlue, 50, 50);
				font.setColor(Team.BLUE.color);
				font.draw(DrawUtil.batch, scoreRed,
						Gdx.graphics.getWidth() - 200, 50);
				font.setColor(Color.GREEN);
				font.draw(DrawUtil.batch, time,
						Gdx.graphics.getWidth() / 2 - 100, 50);
				break;
			case GAME_OVER:
				font.setColor(Team.RED.color);
				font.draw(DrawUtil.batch, scoreBlue, 50, 50);
				font.setColor(Team.BLUE.color);
				font.draw(DrawUtil.batch, scoreRed,
						Gdx.graphics.getWidth() - 200, 50);
				font.setColor(Color.GREEN);
				font.draw(DrawUtil.batch, gameover,
						Gdx.graphics.getWidth() / 2 - 100, 50);
				break;
			default:

			}
		} else {
			System.out.println("kein gamestate " + time);
		}
	}

	@Override
	public void onScoreChangedEvent(int scoreBlue, int scoreRed) {
		this.scoreBlue = "RED " + scoreBlue;
		this.scoreRed = "BLUE " + scoreRed;
		if (scoreBlue > scoreRed)
			winner = "TEAM RED";
		else
			winner = "TEAM BLUE";
	}

	@Override
	public void onChangeGameStateEvent(GameState newState, float gameTime) {
		countdown = gameTime;
		this.gamestate = newState;

	}
}
