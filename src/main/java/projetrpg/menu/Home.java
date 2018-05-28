package projetrpg.menu;

import projetrpg.menu.save.SavesServices;

public class Home {

    private final SavesServices savesServices;

    public Home(SavesServices savesServices) {
        this.savesServices = savesServices;
    }

    public SavesServices getSavesServices() {
        return savesServices;
    }

}
