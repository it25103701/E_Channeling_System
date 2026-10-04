package com.echanneling.e_channeling_system.observer;

public class PatientNotificationObserver implements ScheduleObserver {

    @Override
    public void update(String message) {

        System.out.println("Patient Notification: " + message);

    }
}