package de.hochschuletrier.gdw.ss15.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.commons.jackson.JacksonReader;
import de.hochschuletrier.gdw.ss15.events.CreateServerEvent;
import de.hochschuletrier.gdw.ss15.events.DisconnectEvent;
import de.hochschuletrier.gdw.ss15.events.JoinServerEvent;
import de.hochschuletrier.gdw.ss15.events.TestGameEvent;
import de.hochschuletrier.gdw.ss15.game.GameConstants;
import java.util.HashMap;

public class MenuPageRoot extends MenuPage {
    private HashMap<String, String> maps;

    public enum Type {

        MAINMENU,
        INGAME
    }

    public MenuPageRoot(Skin skin, MenuManager menuManager, Type type) {
        super(skin, "menu_bg");

        try {
            maps = JacksonReader.readMap("data/json/maps.json", String.class);
            for(String name: maps.keySet()) {
                System.out.println(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

//        addActor(new DecoImage(assetManager.getTexture("menu_bg_root_bottom")));
        int x = 100;
        int i = 0;
        int y = 370;
        int yStep = 55;
        if (type == Type.MAINMENU) {
            addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Spiel Starten", this::startGame);
            addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Server Starten", this::startServer);
            addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Server Beitreten", this::joinServer);
        } else {
            addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Fortsetzen", () -> menuManager.popPage());
            addLeftAlignedButton(x, y - yStep * (i++), 400, 50, "Spiel verlassen", this::stopGame);
        }
        addPageEntry(menuManager, x, y - yStep * (i++), "Credits", new MenuPageCredits(skin, menuManager));
        addCenteredButton(menuManager.getWidth() - 80, 54, 100, 40, "Exit", () -> System.exit(-1));
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
