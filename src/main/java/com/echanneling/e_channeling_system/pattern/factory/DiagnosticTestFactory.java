package com.echanneling.e_channeling_system.pattern.factory;

public class DiagnosticTestFactory {
    public static DiagnosticTest createTest(String testCategory) {
        if (testCategory == null) {
            return new BloodTest();
        }
        String lower = testCategory.toLowerCase();
        if (lower.contains("ecg") || lower.contains("cardio")) {
            return new CardiologyTest();
        } else if (lower.contains("x-ray") || lower.contains("imaging") || lower.contains("ultrasound")) {
            return new ImagingTest();
        } else {
            return new BloodTest();
        }
    }
}