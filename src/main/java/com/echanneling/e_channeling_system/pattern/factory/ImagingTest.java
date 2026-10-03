package com.echanneling.e_channeling_system.pattern.factory;

public class ImagingTest implements DiagnosticTest {
    @Override
    public String getCategoryName() {
        return "Chest X-Ray / Ultrasound";
    }

    @Override
    public String getDefaultDepartment() {
        return "Radiology";
    }

    @Override
    public int getTurnaroundHours() {
        return 12;
    }
}