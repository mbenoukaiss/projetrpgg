package projetrpg.observer;

public interface IObservable {
    /**
     * Add an observer to the set of observers
     *
     * @param o The observer to add
     */
    void addObserver(IObserver o);

    /**
     * Remove an observer from the set of observers
     *
     * @param o The observer to add
     */
    void removeObserver(IObserver o);

    /**
     * The method that has to be called if this object
     * has changed.
     */
    void notifyObservers();

    /**
     * The method that has to be called if this object
     * has changed.
     *
     * @param arg An argument.
     */
    void notifyObservers(Object arg);

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    void clearObservers();
}
