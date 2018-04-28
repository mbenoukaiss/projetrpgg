package projetrpg.quest;

import projetrpg.entities.items.Item;
import projetrpg.observer.IObservable;
import projetrpg.observer.Observable;
import projetrpg.observer.IObserver;

import java.util.ArrayList;

public class Quest extends Observable implements IObserver {
    private int id;
    private boolean isCompleted;
    private boolean isStarted;
    private int expRewarded;
    private String description;
    private ArrayList<Objective> objectives;
    private ArrayList<Item> rewardedItems;

    public Quest(int id, int expRewarded, String description) {
        this.id = id;
        this.expRewarded = expRewarded;
        this.description = description;
        this.isCompleted = false;
        this.isStarted = false;
        this.objectives = new ArrayList<>();
        this.rewardedItems = new ArrayList<>();
    }

    public String start() {
        this.isStarted = true;
        return ("You started this quest : " + this.description + ".");
    }

    public String finish() {
        this.isCompleted = true;
        return ("You finished this quest : " + this.description + ". You earned " + this.expRewarded + ".");
    }

    public void linkObjectiv(Objective o) {
        this.objectives.add(o);
    }

    public void linkRewardedItem(Item i) { this.rewardedItems.add(i); }

    public int getId() {
        return id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public int getExpRewarded() {
        return expRewarded;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Objective> getObjectives() {
        return objectives;
    }

    public ArrayList<Item> getRewardedItems() {
        return rewardedItems;
    }

    @Override
    public void update(IObservable a) {
        boolean over = true;
        for(Objective o : this.objectives) {
            if (!o.isFinished()) {
                over = false;
            }
        }
        if (over) {
            this.finish();
            notifyObservers();
        } else {
            ((Objective) a).setFinished(true);
        }
    }
}
