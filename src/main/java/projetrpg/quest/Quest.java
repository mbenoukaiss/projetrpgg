package projetrpg.quest;

import projetrpg.entities.items.Item;
import projetrpg.observer.IObservable;
import projetrpg.observer.Observable;
import projetrpg.observer.IObserver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Quest extends Observable implements IObserver, Comparable<Quest> {

    /**
     * The name of the quest.
     */
    protected String name;

    /**
     * The description of the quest.
     */
    protected String description;

    /**
     * The required level to start this quest.
     */
    protected int levelRequired;

    /**
     * The experience reward.
     */
    protected int expRewarded;

    /**
     * The items rewarded
     */
    protected ArrayList<Item> reward;

    /**
     * The objectives.
     */
    protected Set<Objective> objectives;

    Quest() {

    }

    public Quest(int expRewarded, String name, String description, int levelRequired) {
        this.expRewarded = expRewarded;
        this.description = description;
        this.name = name;
        this.levelRequired = levelRequired;
        this.objectives = new HashSet<>();
        this.reward = new ArrayList<>();
    }

    /**
     * Adds an objective to this quest.
     *
     * @param o The objective.
     */
    public void linkObjective(Objective o) {
        this.objectives.add(o);
        o.addObserver(this);
    }

    /**
     * Adds an item to the list of items rewarded.
     *
     * @param i The item.
     */
    public void linkRewardedItem(Item i) { this.reward.add(i); }

    /**
     * The getter for the name of this quest.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the description of the quest.
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the experience reward.
     *
     * @return The experience reward.
     */
    public int getExpRewarded() {
        return expRewarded;
    }

    /**
     * Getter for the objectives.
     *
     * @return The objectives.
     */
    public Set<Objective> getObjectives() {
        return new HashSet<>(objectives);
    }

    /**
     * Getter for the items reward.
     *
     * @return The items.
     */
    public ArrayList<Item> getReward() {
        return (ArrayList<Item>) reward.clone();
    }

    /**
     * Getter for the level required to start
     * this quest.
     *
     * @return The level required.
     */
    public int getLevelRequired() {
        return levelRequired;
    }

    /**
     * Give up this quest.
     */
    public void giveUp() {
        for (Objective o : this.getObjectives()) {
            o.finished = false;
        }
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
            notifyObservers();
        }
    }

    @Override
    public int compareTo(Quest quest) {
        return Integer.compare(levelRequired, quest.levelRequired);
    }
}
