package projetrpg.Quest;

import projetrpg.entities.player.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Quest {
    private int id;
    private boolean isCompleted;
    private boolean isStarted;
    private int expRewarded;
    private String description;
    private ArrayList<Objectiv> objectives;
    private Player player;

    public Quest(int id, int expRewarded, String description, Player player) {
        this.id = id;
        this.expRewarded = expRewarded;
        this.description = description;
        this.isCompleted = false;
        this.isStarted = false;
        this.objectives = new ArrayList<>();
        this.player = player;
        this.player.setQuest(this);
    }

    public String start() {
        this.isStarted = true;
        this.player.setCurrentObjectivs(this.objectives);
        return ("You started " + this.description + ".");
    }

    public String finish() {
        this.player.earnExperience(this.expRewarded);
        this.isCompleted = true;
        this.player.setCurrentObjectivs(null);
        this.player.setQuest(null);
        return (" Congrats! You finished the quest : " + this.description + "! " + "You earned " + this.expRewarded + " xp!");
    }

    public void linkObjectiv(Objectiv o) {
        this.objectives.add(o);
    }

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

    public ArrayList<Objectiv> getObjectives() {
        return objectives;
    }

    public Player getPlayer() {
        return player;
    }

}
