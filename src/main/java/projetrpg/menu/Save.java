package projetrpg.menu;

import projetrpg.map.MainMap;

import java.util.Calendar;

public class Save {

    private final MainMap map;
    private final Calendar created;
    private final Calendar lastSaved;

    Save(MainMap map, Calendar created, Calendar lastSaved) {
        this.map = map;
        this.created = created;
        this.lastSaved = lastSaved;
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

    void updateLastSaved() {

    }

}
