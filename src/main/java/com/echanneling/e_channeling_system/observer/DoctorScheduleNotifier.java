package com.echanneling.e_channeling_system.observer;

import java.util.ArrayList;
import java.util.List;

public class DoctorScheduleNotifier implements ScheduleSubject {

    private final List<ScheduleObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(ScheduleObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ScheduleObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {

        for (ScheduleObserver observer : observers) {
            observer.update(message);
        }
    }
}