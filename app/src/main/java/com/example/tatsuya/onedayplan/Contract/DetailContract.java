package com.example.tatsuya.onedayplan.Contract;

/**
 * Created by tatsuya on 2017/03/23.
 */

public interface DetailContract {

    interface View{
        void showTitle(String title);
        void showTestCheck(boolean testcheck);
        void showHomeWorkCheck(boolean homeworkcheck);
        void showRemark(String remark);
        void startEdit();
        void backMain();
    }
    interface Action{
        void clickEditButton();
    }
}
