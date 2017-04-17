package com.example.tatsuya.onedayplan.Contract;

/**
 * Created by tatsuya on 2017/03/23.
 */

public interface EditContract {
    interface View{
        void showTitle(String title);
        void showTestCheck(boolean testcheck);
        void showHomeWorkCheck(boolean homeworkcheck);
        void showRemark(String remark);
        void backJump();
    }
    interface Action{
        void clickTestCheck(boolean testcheck);
        void clickHomeWorkCheck(boolean testcheck);
        void clickBackButton();
        void clickSaveButton();
    }
}
