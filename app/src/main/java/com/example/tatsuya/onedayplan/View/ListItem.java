package com.example.tatsuya.onedayplan.View;

import io.realm.RealmObject;

/**
 * Created by tatsuya on 2017/03/14.
 */

public class ListItem extends RealmObject {
    String title;
    Boolean testCheck;
    Boolean homeworkCheck;
    String remark;
    String day;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }



    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getHomeworkCheck() {

        return homeworkCheck;
    }

    public void setHomeworkCheck(Boolean homeworkCheck) {
        this.homeworkCheck = homeworkCheck;
    }

    public Boolean getTestCheck() {

        return testCheck;
    }

    public void setTestCheck(Boolean testCheck) {
        this.testCheck = testCheck;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String text) {
        this.title = text;
    }
}
