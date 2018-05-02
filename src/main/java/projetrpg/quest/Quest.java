package projetrpg.quest;

import projetrpg.entities.items.Item;
import projetrpg.observer.IObservable;
import projetrpg.observer.Observable;
import projetrpg.observer.IObserver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Quest extends Observable implements IObserver {

    protected boolean started;
    protected boolean completed;
    protected int expRewarded;
    protected String description;
    protected Set<Objective> objectives;
    protected ArrayList<Item> reward;

    Quest() {

    }

    public Quest(int expRewarded, String description) {
        this.expRewarded = expRewarded;
        this.description = description;
        this.completed = false;
        this.started = false;
        this.objectives = new HashSet<>();
        this.reward = new ArrayList<>();
    }

    public String start() {
        this.started = true;
        return ("You started this quest : " + this.description + ".");
    }

    public String finish() {
        this.completed = true;
        return ("You finished this quest : " + this.description + ". You earned " + this.expRewarded + ".");
    }

    public void linkObjective(Objective o) {
        this.objectives.add(o);
        o.addObserver(this);
    }

    public void linkRewardedItem(Item i) { this.reward.add(i); }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isStarted() {
        return started;
    }

    public int getExpRewarded() {
        return expRewarded;
    }

    public String getDescription() {
        return description;
    }

    public Set<Objective> getObjectives() {
        return new HashSet<>(objectives);
    }

    public ArrayList<Item> getReward() {
        return (ArrayList<Item>) reward.clone();
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
