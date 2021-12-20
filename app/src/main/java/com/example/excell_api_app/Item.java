package com.example.excell_api_app;

import android.widget.TextView;

public class Item {

    String first_label;
    String second_label;
    String third_label;

    public Item() {
    }

    public Item(String first_label, String second_label, String third_label) {
        this.first_label = first_label;
        this.second_label = second_label;
        this.third_label = third_label;
    }

    public String getFirst_label() {
        return first_label;
    }

    public void setFirst_label(String first_label) {
        this.first_label = first_label;
    }

    public String getSecond_label() {
        return second_label;
    }

    public void setSecond_label(String second_label) {
        this.second_label = second_label;
    }

    public String getThird_label() {
        return third_label;
    }

    public void setThird_label(String third_label) {
        this.third_label = third_label;
    }


}
