package de.hochschuletrier.gdw.ss15.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ss15.events.CreateServerEvent;
import de.hochschuletrier.gdw.ss15.events.DisconnectEvent;
import de.hochschuletrier.gdw.ss15.events.JoinServerEvent;
import de.hochschuletrier.gdw.ss15.events.TestGameEvent;
import de.hochschuletrier.gdw.ss15.game.GameConstants;

public class MenuPageRoot extends MenuPage {

    public enum Type {

        MAINMENU,
        INGAME
    }

    public MenuPageRoot(Skin skin, MenuManager menuManager, Type type) {
        super(skin, "menu_bg");

//        addActor(new DecoImage(assetManager.getTexture("menu_bg_root_bottom")));
        int x = 100;
        int i = 0;
        int y = 370;
        int yStep = 55;
        if (type == Type.MAINMENU) {
            createLabel(400, 500, 600, 80, "Main Menu");
            addPageEntry(menuManager, 50, 300, "Join Server", new MenuPageJoinServer(skin, menuManager));
            addPageEntry(menuManager, 50, 200, "Host Server", new MenuPageHostServer(skin, menuManager));
            
        } else {
            addLeftAlignedButton(50, 300, 400, 50, "Fortsetzen", () -> menuManager.popPage());
            addLeftAlignedButton(50, 200, 400, 50, "Spiel verlassen", this::stopGame);
        }
        addPageEntry(menuManager, 50, 300, "Credits", new MenuPageCredits(skin, menuManager));
        addCenteredButton(50, 50, 400, 50, "Exit Client", () -> System.exit(-1));
    }

    private void startServer() {
        String userName = "Server";
        String mapName = "data/maps/NiceMap.tmx";
        int port = 9090;
        CreateServerEvent.emit(port, GameConstants.MAX_PLAYERS, mapName, userName);
    }

    private void joinServer() {
        String server = "localhost";
        String userName = "Client";
        int port = 9090;
        JoinServerEvent.emit(server, port, userName);
    }

    private void startGame() {
        TestGameEvent.emit("data/maps/NiceMap.tmx");
    }

    private void stopGame() {
        DisconnectEvent.emit();
    }

    protected final void addPageEntry(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(x, y, 300, 40, text, () -> menuManager.pushPage(page));
    }
}
