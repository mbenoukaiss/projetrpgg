package projetrpg.observer;

import projetrpg.SerializationIgnore;

import java.util.HashSet;
import java.util.Set;

public abstract class Observable implements IObservable{
    /**
     * The set of observers.
     */
    @SerializationIgnore
    private Set<IObserver> observers;

    public Observable() {
        observers = new HashSet<>();
    }

    /**
     * Add an observer to the set of observers
     *
     * @param o The observer to add
     */
    @Override
    public void addObserver(IObserver o) {
        observers.add(o);
    }

    /**
     * Remove an observer from the set of observers
     *
     * @param o The observer to add
     */
    @Override
    public void removeObserver(IObserver o) {
        observers.remove(o);
    }


    /**
     * The method that has to be called if this object
     * has changed.
     */
    @Override
    public void notifyObservers() {
        notifyObservers(null);
    }

    /**
     * The method that has to be called if this object
     * has changed.
     *
     * @param arg An argument.
     */
    @Override
    public void notifyObservers(Object arg) {
        for (IObserver observer : observers) {
            observer.update(this);
        }
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    @Override
    public void clearObservers() {
        observers.clear();
    }
}
