package com.echanneling.e_channeling_system.pattern.factory;

public class BloodTest implements DiagnosticTest {
    @Override
    public String getCategoryName() {
        return "Full Blood Count / Lipid Profile";
    }

    @Override
    public String getDefaultDepartment() {
        return "Pathology / Hematology";
    }

    @Override
    public int getTurnaroundHours() {
        return 6;
    }
}