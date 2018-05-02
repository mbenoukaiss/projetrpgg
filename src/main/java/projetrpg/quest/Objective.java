package projetrpg.quest;

import projetrpg.entities.NPC;
import projetrpg.entities.items.Item;
import projetrpg.observer.Observable;

public class Objective extends Observable {

    protected boolean finished;
    protected String description;
    protected ObjectiveType type;
    protected NPC concernedNPC;
    protected Item concernedItem;

    Objective() {

    }

    public Objective(String description, ObjectiveType type) {
        this.description = description;
        this.finished = false;
        this.type = type;
    }

    public void finish() {
        this.finished = true;
        this.notifyObservers();
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setConcernedNPC(NPC concernedNPC) {
        this.concernedNPC = concernedNPC;
    }

    public void setConcernedItem(Item concernedItem) {
        this.concernedItem = concernedItem;
    }

    public boolean isFinished() {
        return finished;
    }

    public String getDescription() {
        return description;
    }

    public ObjectiveType getType() {
        return type;
    }

    public NPC getConcernedNPC() {
        return concernedNPC;
    }

    public Item getConcernedItem() {
        return concernedItem;
    }
}
