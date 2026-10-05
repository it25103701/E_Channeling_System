package com.echanneling.e_channeling_system.pattern.factory;

/**
 * Product interface for the Factory Pattern implementation in Lab Orders (UC-06).
 * Defines contract specifications for diagnostic test instances across multiple
 * laboratory and clinical departments.
 */
public interface DiagnosticTest {

    /**
     * Retrieves the standard clinical category description and nomenclature.
     *
     * @return test category descriptor
     */
    String getCategoryName();

    /**
     * Identifies the primary hospital department responsible for processing this test.
     *
     * @return default clinical department designation
     */
    String getDefaultDepartment();

    /**
     * Retrieves standard turnaround processing time in hours.
     *
     * @return baseline turnaround hours
     */
    int getTurnaroundHours();
}