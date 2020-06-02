package com.student.dashboard.entity;

public class TwoAttributes {

    public String column1,column2;

    public TwoAttributes() {
    }

    public TwoAttributes(String column1, String column2) {
        this.column1 = column1;
        this.column2 = column2;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }
}
