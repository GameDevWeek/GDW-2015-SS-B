package de.hochschuletrier.gdw.ss14.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.cameras.orthogonal.LimitedSmoothCamera;
import de.hochschuletrier.gdw.commons.gdx.sound.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.state.GameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ss14.Main;
import de.hochschuletrier.gdw.ss14.game.Game;
import de.hochschuletrier.gdw.ss14.input.InputDevice.DeviceType;
import de.hochschuletrier.gdw.ss14.input.InputManager;
import de.hochschuletrier.gdw.ss14.input.infos.InputInfo;
import de.hochschuletrier.gdw.ss14.input.infos.InputSettings;
import de.hochschuletrier.gdw.ss14.input.infos.KeyboardInfo;
import de.hochschuletrier.gdw.ss14.input.infos.MouseInfo;

/**
 * Gameplay state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends GameState implements InputProcessor {

    private Game game;
    private Sound helicopter;
    private final Vector2 cursor = new Vector2();
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);

    private final SoundEmitter emitter = new SoundEmitter();
    private final LimitedSmoothCamera camera = new LimitedSmoothCamera();
    private final Vector2 position = new Vector2(100, 100);
    private float totalMapWidth, totalMapHeight;

    public GameplayState() {
    }

    @Override
    public void init(AssetManagerX assetManager) {
        super.init(assetManager);
        helicopter = assetManager.getSound("helicopter");
        game = new Game();
        game.init(assetManager);
        Main.inputMultiplexer.addProcessor(this);
        
        // Setup camera
        TiledMap map = game.getMap();
        camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        totalMapWidth = map.getWidth() * map.getTileWidth();
        totalMapHeight = map.getHeight() * map.getTileHeight();
        camera.setBounds(0, 0, totalMapWidth, totalMapHeight);
        camera.updateForced();
        Main.getInstance().addScreenListener(camera);
        InputManager.init();
    }

    @Override
    public void render() {
        camera.bind();

        DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Color.BLACK);

        game.render();
        DrawUtil.fillRect(position.x - 10, position.y -10, 20, 20, Color.RED);
    }

    @Override
    public void update(float delta) {
        InputManager.getInstance().update();
        emitter.update();
        emitter.setPosition(cursor.x, cursor.y, 0);
        game.update(delta);
        camera.update(delta);
        
        float speed = 1000.0f;
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            position.x -= delta * speed;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            position.x += delta * speed;
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
            position.y -= delta * speed;
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
            position.y += delta * speed;

        position.x = Math.max(10, Math.min(totalMapWidth-10, position.x));
        position.y = Math.max(10, Math.min(totalMapHeight-10, position.y));
        camera.setDestination(position);
        fpsCalc.addFrame();
    }

    @Override
    public void onEnter() {
        emitter.dispose();
        emitter.play(helicopter, true);
    }

    @Override
    public void onLeave() {
        emitter.dispose();
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        cursor.set(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
