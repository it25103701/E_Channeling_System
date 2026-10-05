package com.echanneling.e_channeling_system.pattern.factory;

import org.springframework.stereotype.Component;

/**
 * Concrete product implementation for Cardiology diagnostics (UC-06).
 * Encapsulates domain specifications, department routing, and expected turnaround
 * times for ECG and Echocardiogram diagnostic requisitions.
 */
@Component
public class CardiologyTest implements DiagnosticTest {

    /**
     * Retrieves the standard clinical category description.
     *
     * @return test category identifier
     */
    @Override
    public String getCategoryName() {
        return "12-Lead ECG / Echocardiogram";
    }

    /**
     * Resolves the default hospital department designated for cardiology testing.
     *
     * @return primary clinical department
     */
    @Override
    public String getDefaultDepartment() {
        return "Cardiology";
    }

    /**
     * Standard turnaround time required to process and interpret cardiology diagnostics.
     *
     * @return processing window in hours
     */
    @Override
    public int getTurnaroundHours() {
        return 2;
    }
}