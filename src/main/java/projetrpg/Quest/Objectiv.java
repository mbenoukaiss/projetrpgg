package projetrpg.Quest;

public class Objectiv {
    private boolean isFinished;
    private String description;
    private Quest associatedQuest;

    public Objectiv(Boolean isFinished, String description, Quest quest) {
        this.description = description;
        this.isFinished = isFinished;
        this.associatedQuest = quest;
        this.getAssociatedQuest().linkObjectiv(this);
    }


    public String finish() {
        boolean over = true;
        this.isFinished = true;
        for(Objectiv o : this.getAssociatedQuest().getObjectives()) {
            if (!o.isFinished) {
                over = false;
            }
        }
        if (over) {
            return this.getAssociatedQuest().finish();
        } else {
            this.getAssociatedQuest().getPlayer().removeObjectiv(this);
            return ("You finished this objectiv : " + this.description + ".");
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    public String getDescription() {
        return description;
    }

    public Quest getAssociatedQuest() {
        return associatedQuest;
    }
}
