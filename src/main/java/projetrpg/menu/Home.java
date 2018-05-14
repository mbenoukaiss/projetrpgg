package projetrpg.menu;

public class Home {

    private final SavesServices savesServices;

    public Home(String savePath) {
        this.savesServices = new SavesServices(savePath);
    }

    public SavesServices getSavesServices() {
        return savesServices;
    }

}
