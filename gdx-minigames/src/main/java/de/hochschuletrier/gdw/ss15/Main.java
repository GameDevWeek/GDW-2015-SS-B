package de.hochschuletrier.gdw.ss15;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVar;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarEnum;
import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.assets.loaders.AnimationExtendedLoader;
import de.hochschuletrier.gdw.commons.gdx.audio.MusicManager;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundDistanceModel;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundInstance;
import de.hochschuletrier.gdw.commons.gdx.devcon.DevConsoleView;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.Hotkey;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyManager;
import de.hochschuletrier.gdw.commons.gdx.input.hotkey.HotkeyModifier;
import de.hochschuletrier.gdw.commons.gdx.state.BaseGameState;
import de.hochschuletrier.gdw.commons.gdx.state.StateBasedGame;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.GdxResourceLocator;
import de.hochschuletrier.gdw.commons.gdx.utils.KeyUtil;
import de.hochschuletrier.gdw.commons.gdx.utils.ScreenUtil;
import de.hochschuletrier.gdw.commons.netcode.simple.NetServerSimple;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.utils.ClassUtils;
import de.hochschuletrier.gdw.ss15.datagrams.DatagramFactory;
import de.hochschuletrier.gdw.ss15.events.CreateServerEvent;
import de.hochschuletrier.gdw.ss15.events.DisconnectEvent;
import de.hochschuletrier.gdw.ss15.events.JoinServerEvent;
import de.hochschuletrier.gdw.ss15.events.TestGameEvent;
import de.hochschuletrier.gdw.ss15.game.TestGame;
import de.hochschuletrier.gdw.ss15.sandbox.SandboxCommand;
import de.hochschuletrier.gdw.ss15.states.ConnectingState;
import de.hochschuletrier.gdw.ss15.states.GameplayState;
import de.hochschuletrier.gdw.ss15.states.LoadGameState;
import de.hochschuletrier.gdw.ss15.states.MainMenuState;

/**
 *
 * @author Santo Pfingsten
 */
public class Main extends StateBasedGame
        implements DisconnectEvent.Listener, TestGameEvent.Listener,
        CreateServerEvent.Listener, JoinServerEvent.Listener {

    public static CommandLine cmdLine;

    public static final boolean IS_RELEASE = ClassUtils.getClassUrl(Main.class).getProtocol().equals("jar");

    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 1024;

    private final AssetManagerX assetManager = new AssetManagerX();
    private static Main instance;

    public final DevConsole console = new DevConsole(16);
    private final DevConsoleView consoleView = new DevConsoleView(console);
    private Skin consoleSkin;
    public static final InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private final CVarEnum<SoundDistanceModel> distanceModel = new CVarEnum("snd_distanceModel", SoundDistanceModel.INVERSE, SoundDistanceModel.class, 0, "sound distance model");
    private final CVarEnum<SoundEmitter.Mode> emitterMode = new CVarEnum("snd_mode", SoundEmitter.Mode.STEREO, SoundEmitter.Mode.class, 0, "sound mode");
    private final Hotkey toggleFullscreen = new Hotkey(() -> ScreenUtil.toggleFullscreen(), Input.Keys.ENTER, HotkeyModifier.ALT);

    public Main() {
        super(new BaseGameState());
    }

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    /**
     * @return the assetManager
     */
    public AssetManagerX getAssetManager() {
        return assetManager;
    }

    private void setupDummyLoader() {
    }

    private void loadAssetLists() {
        TextureParameter param = new TextureParameter();
        param.minFilter = param.magFilter = Texture.TextureFilter.Linear;

        assetManager.loadAssetList("data/json/images.json", Texture.class, param);
        assetManager.loadAssetList("data/json/sounds.json", Sound.class, null);
        assetManager.loadAssetList("data/json/music.json", Music.class, null);
        assetManager.loadAssetList("data/json/particleEffects.json", ParticleEffect.class, null);
        assetManager.loadAssetListWithParam("data/json/animations.json", AnimationExtended.class,
                AnimationExtendedLoader.AnimationExtendedParameter.class);
        BitmapFontParameter fontParam = new BitmapFontParameter();
        fontParam.flip = true;
        assetManager.loadAssetList("data/json/fonts.json", BitmapFont.class, fontParam);
    }

    private void setupGdx() {
        KeyUtil.init();
        Gdx.graphics.setContinuousRendering(true);

        Gdx.input.setCatchMenuKey(true);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void create() {
        CurrentResourceLocator.set(new GdxResourceLocator(Files.FileType.Local));
        DrawUtil.init();
        setupDummyLoader();
        loadAssetLists();
        setupGdx();
        SoundInstance.init();

        consoleSkin = new Skin(Gdx.files.internal("data/skins/basic.json"));
        consoleView.init(consoleSkin);
        addScreenListener(consoleView);
        inputMultiplexer.addProcessor(consoleView.getInputProcessor());
        inputMultiplexer.addProcessor(HotkeyManager.getInputProcessor());

        changeState(new LoadGameState(assetManager, this::onLoadComplete), null, null);

        this.console.register(distanceModel);
        distanceModel.addListener((CVar) -> distanceModel.get().activate());

        this.console.register(emitterMode);
        emitterMode.addListener(this::onEmitterModeChanged);

        TestGameEvent.register(this);
        DisconnectEvent.register(this);
        CreateServerEvent.register(this);
        JoinServerEvent.register(this);

        toggleFullscreen.register();
        Pixmap pm = new Pixmap(Gdx.files.internal("data/ui/cursor.png"));
        Gdx.input.setCursorImage(pm, 0, 0);
    }

    private void onLoadComplete() {
        final MainMenuState mainMenuState = new MainMenuState(assetManager);
        addPersistentState(mainMenuState);
        changeState(mainMenuState, null, null);
        SandboxCommand.init(assetManager);

        if (cmdLine.hasOption("sandbox")) {
            SandboxCommand.runSandbox(cmdLine.getOptionValue("sandbox"));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        DrawUtil.batch.dispose();
        consoleView.dispose();
        consoleSkin.dispose();
        SoundEmitter.disposeGlobal();
    }

    protected void preRender() {
        DrawUtil.clearColor(Color.BLACK);
        DrawUtil.clear();
        DrawUtil.resetColor();

        DrawUtil.batch.begin();
    }

    protected void postRender() {
        DrawUtil.batch.end();
        if (consoleView.isVisible()) {
            consoleView.render();
        }
    }

    @Override
    protected void preUpdate(float delta) {
        if (consoleView.isVisible()) {
            consoleView.update(delta);
        }
        console.executeCmdQueue();
        SoundEmitter.updateGlobal();
        MusicManager.update(delta);

        preRender();
    }

    @Override
    protected void postUpdate(float delta) {
        postRender();
    }

    public void onEmitterModeChanged(CVar cvar) {
        int x = Gdx.graphics.getWidth() / 2;
        int y = Gdx.graphics.getHeight() / 2;
        SoundEmitter.setListenerPosition(x, y, 10, emitterMode.get());
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void onTestGameEvent(String mapName) {
        if (!isTransitioning()) {
            TestGame game = new TestGame();
            game.init(assetManager, mapName);
            changeState(new GameplayState(assetManager, game), new SplitHorizontalTransition(500), null);
        }
    }

    @Override
    public void onDisconnectEvent() {
        if (!isTransitioning()) {
            changeState(getPersistentState(MainMenuState.class));
        }
    }

    @Override
    public void onCreateServerEvent(int port, int maxPlayers, String mapName, String userName) {
        if (!isTransitioning()) {
            NetServerSimple netServer = new NetServerSimple(DatagramFactory.POOL);
            if (netServer.start(port, maxPlayers)) {
                TestGame game = new TestGame(netServer, null);
                game.init(assetManager, mapName);
                changeState(new GameplayState(assetManager, game), new SplitHorizontalTransition(500), null);
            }
        }
    }

    @Override
    public void onJoinServerEvent(String server, int port, String userName) {
        if (!isTransitioning()) {
            try {
                ConnectingState connectingState = new ConnectingState(assetManager, server, port, userName);
                if (connectingState.isSuccess()) {
                    changeState(connectingState);
                } else {
                    connectingState.dispose();
                    DisconnectEvent.emit();
                }
            } catch (IOException e) {
                DisconnectEvent.emit();
            }
        }
    }

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Balls of Steel";
        cfg.width = WINDOW_WIDTH;
        cfg.height = WINDOW_HEIGHT;
        cfg.useGL30 = false;
        cfg.vSyncEnabled = true;
        cfg.foregroundFPS = 60;
        cfg.backgroundFPS = 60;
        cfg.addIcon("data/icon128.png", Files.FileType.Internal);
        cfg.addIcon("data/icon64.png", Files.FileType.Internal);
        cfg.addIcon("data/icon32.png", Files.FileType.Internal);
        cfg.addIcon("data/icon16.png", Files.FileType.Internal);

        parseOptions(args);
        new LwjglApplication(getInstance(), cfg);
    }

    private static void parseOptions(String[] args) throws IllegalArgumentException {
        CommandLineParser cmdLineParser = new PosixParser();

        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("sandbox")
                .withDescription("Start a Sandbox Game")
                .withType(String.class)
                .hasArg()
                .withArgName("Sandbox Classname")
                .create());

        try {
            cmdLine = cmdLineParser.parse(options, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
