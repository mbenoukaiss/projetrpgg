package projetrpg.quest;

import projetrpg.entities.NPC;
import projetrpg.entities.items.Item;
import projetrpg.observer.AbstractObservable;

public class Objective extends AbstractObservable {
    private boolean isFinished;
    private String description;
    private ObjectiveType type;
    private NPC concernedNPC;
    private Item concernedItem;

    public Objective(String description, ObjectiveType type) {
        this.description = description;
        this.isFinished = false;
        this.type = type;
    }

    public void finish() {
        this.isFinished = true;
        this.notifyObservers();
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void setConcernedNPC(NPC concernedNPC) {
        this.concernedNPC = concernedNPC;
    }

    public void setConcernedItem(Item concernedItem) {
        this.concernedItem = concernedItem;
    }

    public boolean isFinished() {
        return isFinished;
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
