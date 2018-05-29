package projetrpg.menu.save;

import projetrpg.map.MainMap;

import java.io.File;
import java.util.Calendar;

public class Save {

    private final File file;
    private final MainMap map;
    private final Calendar created;
    private Calendar lastSaved;

    public Save(File file, MainMap map, Calendar created, Calendar lastSaved) {
        this.file = file;
        this.map = map;
        this.created = created;
        this.lastSaved = lastSaved;
    }

    public File getFile() {
        return file;
    }

    public MainMap getMap() {
        return map;
    }

    public Calendar getCreated() {
        return (Calendar) created.clone();
    }

    public Calendar getLastSaved() {
        return (Calendar) lastSaved.clone();
    }

    public void updateLastSaved() {
        lastSaved = Calendar.getInstance();
    }

}
