package de.hochschuletrier.gdw.ss15.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import de.hochschuletrier.gdw.commons.gdx.menu.MenuManager;
import de.hochschuletrier.gdw.ss15.events.DisconnectEvent;

public class MenuPageRoot extends MenuPage {

    public enum Type {

        MAINMENU,
        INGAME
    }

    public MenuPageRoot(Skin skin, MenuManager menuManager, Type type) {
        super(skin, "menu_bg");

//        addActor(new DecoImage(assetManager.getTexture("menu_bg_root_bottom")));
        int x = 50;
        int i = 0;
        int y = 50;
        int yStep = 50;
        if (type == Type.MAINMENU) {
            createLabel(50, 390, 600, 80, "Main Menu", "big");
            addPageEntry1(menuManager, 50, 300, "JOIN SERVER", new MenuPageJoinServer(skin, menuManager));
            addPageEntry2(menuManager, 50, 200, "HOST SERVER", new MenuPageHostServer(skin, menuManager));
            addPageEntry3(menuManager, 50, 100, "CREDITS", new MenuPageCredits(skin, menuManager));
            addLeftAlignedButton(50, 50, 400, 50, "EXIT CLIENT", () -> System.exit(-1));
        } else {
            createLabel(50, 390, 600, 80, "ingame menu", "big");
            addLeftAlignedButton(50, 300, 400, 50, "RESUME GAME !", () -> menuManager.popPage());
            addLeftAlignedButton(50, 200, 400, 50, "LEAVE GAME", this::stopGame);
        }       
    }

    private void stopGame() {
        DisconnectEvent.emit();
    }

    protected final void addPageEntry1(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(50, 300, 300, 50, text, () -> menuManager.pushPage(page));
    }
    protected final void addPageEntry2(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(50, 250, 300, 50, text, () -> menuManager.pushPage(page));
    }
    protected final void addPageEntry3(MenuManager menuManager, int x, int y, String text, MenuPage page) {
        menuManager.addLayer(page);
        addLeftAlignedButton(50, 200, 300, 50, text, () -> menuManager.pushPage(page));
    }
}
