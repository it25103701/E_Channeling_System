package com.echanneling.e_channeling_system.NotificationAndAnnouncementMS;

public class Notification {


        private Long id;
        private String title;
        private String message;
        private String type; // e.g., Doctor Delay, Rule Change, Alert Template
        private String preferenceRules; // Slide Update requirement

        public Notification() {}

        public Notification(Long id, String title, String message, String type, String preferenceRules) {
            this.id = id;
            this.title = title;
            this.message = message;
            this.type = type;
            this.preferenceRules = preferenceRules;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getPreferenceRules() { return preferenceRules; }
        public void setPreferenceRules(String preferenceRules) { this.preferenceRules = preferenceRules; }
    }

