package projetrpg.quest;

import projetrpg.observer.Observable;

public class Objective<T> extends Observable {

    /**
     * If the objective is finished.
     */
    protected boolean finished;

    /**
     * The description of this objective.
     */
    protected String description;

    /**
     * The type of the objective.
     */
    protected ObjectiveType type;

    /**
     * The concerned object.
     */
    protected T concernedObject;

    Objective() {

    }

    public Objective(String description, ObjectiveType type) {
        this.description = description;
        this.finished = false;
        this.type = type;
    }

    /**
     * Tells if this objective is finished.
     *
     * @return True if the objective is finished.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Consider this objective as finished.
     */
    public void finish() {
        this.finished = true;
        this.notifyObservers();
    }

    /**
     * Getter for the concerned object.
     *
     * @return The concerned object.
     */
    public T getConcernedObject() {
        return concernedObject;
    }

    /**
     * Setter for the concerned object.
     *
     * @param concernedObject The new object.
     */
    public void setConcernedObject(T concernedObject) {
        this.concernedObject = concernedObject;
    }

    /**
     * Getter for the description.
     *
     * @return The description of this objective.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the type of objective.
     *
     * @return The type of this objective.
     */
    public ObjectiveType getType() {
        return type;
    }

}
