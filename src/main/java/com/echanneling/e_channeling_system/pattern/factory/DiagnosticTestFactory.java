package com.echanneling.e_channeling_system.pattern.factory;

/**
 * Factory class for instantiating concrete {@link DiagnosticTest} products (UC-06).
 * Encapsulates object creation and routes test categories to their appropriate
 * diagnostic specializations based on input descriptors.
 */
public final class DiagnosticTestFactory {

    private DiagnosticTestFactory() {
        // Prevent direct instantiation of factory utility class
    }

    /**
     * Resolves and creates a concrete {@link DiagnosticTest} instance based on category keywords.
     * Defaults to {@link BloodTest} when the category is unspecified or unmapped.
     *
     * @param testCategory the requested test category descriptor
     * @return a corresponding {@link DiagnosticTest} implementation
     */
    public static DiagnosticTest createTest(String testCategory) {
        if (testCategory == null || testCategory.trim().isEmpty()) {
            return new BloodTest();
        }

        String normalizedCategory = testCategory.toLowerCase();

        if (normalizedCategory.contains("ecg") || normalizedCategory.contains("cardio")) {
            return new CardiologyTest();
        }

        if (normalizedCategory.contains("x-ray")
                || normalizedCategory.contains("imaging")
                || normalizedCategory.contains("ultrasound")) {
            return new ImagingTest();
        }

        return new BloodTest();
    }
}