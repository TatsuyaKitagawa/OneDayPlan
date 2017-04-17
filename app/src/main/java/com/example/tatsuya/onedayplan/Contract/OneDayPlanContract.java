package com.example.tatsuya.onedayplan.Contract;

import com.example.tatsuya.onedayplan.View.ListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tatsuya on 2017/03/11.
 */

public interface OneDayPlanContract {

    interface View{
        void showOneDayList(List<ListItem> onedayplanlist);
        void startDetail(int position);
        void startEdit();

    }

    interface Action{
        void clickOneDayClick(int position);
        void clickAddButton();

    }


}
