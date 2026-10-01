package com.echanneling.e_channeling_system.pattern.factory;

public class CardiologyTest implements DiagnosticTest {
    @Override
    public String getCategoryName() {
        return "12-Lead ECG / Echocardiogram";
    }

    @Override
    public String getDefaultDepartment() {
        return "Cardiology";
    }

    @Override
    public int getTurnaroundHours() {
        return 2;
    }
}