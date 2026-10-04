package com.echanneling.e_channeling_system.observer;

public interface ScheduleSubject {

    void addObserver(ScheduleObserver observer);

    void removeObserver(ScheduleObserver observer);

    void notifyObservers(String message);

}