package de.hochschuletrier.gdw.ss15.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.input.InputForwarder;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.utils.FpsCalculator;
import de.hochschuletrier.gdw.ss15.Main;
import de.hochschuletrier.gdw.ss15.game.AbstractGame;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import de.hochschuletrier.gdw.ss15.game.input.InputKeyboard;
import de.hochschuletrier.gdw.ss15.game.input.InputManager;
import de.hochschuletrier.gdw.ss15.menu.MenuPageRoot;

/**
 * Gameplay state
 * 
 * @author Santo Pfingsten
 */
public class GameplayState extends BaseGameState {

    private static final Color OVERLAY_COLOR = new Color(0f, 0f, 0f, 0.5f);
    private final FpsCalculator fpsCalc = new FpsCalculator(200, 100, 16);
    private final BitmapFont font;

    private final AbstractGame game;
    private final Music music;

    private final MenuManager menuManager = new MenuManager(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, this::onMenuEmptyPop);
    private final InputForwarder inputForwarder;
    private final InputProcessor menuInputProcessor;

    public GameplayState(AssetManagerX assetManager, AbstractGame game) {
        this.game = game;
        font = assetManager.getFont("verdana_32");

        music = assetManager.getMusic("gameplay");

        Skin skin = ((MainMenuState)Main.getInstance().getPersistentState(MainMenuState.class)).getSkin();
        final MenuPageRoot menuPageRoot = new MenuPageRoot(skin, menuManager, MenuPageRoot.Type.INGAME);
        menuManager.addLayer(menuPageRoot);
        menuInputProcessor = menuManager.getInputProcessor();

        menuManager.addLayer(new DecoImage(assetManager.getTexture("menu_fg")));
        menuManager.pushPage(menuPageRoot);
//        menuManager.getStage().setDebugAll(true);

        Main.getInstance().addScreenListener(menuManager);

        inputForwarder = new InputForwarder() {

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    if (mainProcessor == InputManager.getInputProcessor()) {
                        mainProcessor = menuInputProcessor;
                    } else {
                        menuManager.popPage();
                    }
                    return true;
                }
                return super.keyUp(keycode);
            }
        };
        game.start();
    }

    private void onMenuEmptyPop() {
        inputForwarder.set(InputManager.getInputProcessor());
    }

    @Override
    public void update(float delta) {
        fpsCalc.addFrame();
        game.update(delta);
        if (inputForwarder.get() == menuInputProcessor) {
            menuManager.update(delta);
            Main.getInstance().screenCamera.bind();
            DrawUtil.fillRect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), OVERLAY_COLOR);
            menuManager.render();
        }
        Main.getInstance().screenCamera.bind();
        font.setColor(Color.WHITE);
        font.draw(DrawUtil.batch, String.format("%.2f FPS", fpsCalc.getFps()), 0, 0);
    }

    @Override
    public void onEnter(BaseGameState previousState) {
        MusicManager.play(music, GameConstants.MUSIC_FADE_TIME);
    }

    @Override
    public void onEnterComplete() {
        Main.inputMultiplexer.addProcessor(inputForwarder);
        inputForwarder.set(InputManager.getInputProcessor());
    }

    @Override
    public void onLeave(BaseGameState nextState) {
        Main.inputMultiplexer.removeProcessor(inputForwarder);
    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
