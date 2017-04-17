package com.example.tatsuya.onedayplan.Presenter;

import com.example.tatsuya.onedayplan.Contract.OneDayPlanContract;

/**
 * Created by tatsuya on 2017/03/11.
 */

public class OneDayPlanPresenter implements OneDayPlanContract.Action {

    private final OneDayPlanContract.View view;

    public OneDayPlanPresenter(OneDayPlanContract.View view) {
        this.view=view;

    }

    @Override
    public void clickOneDayClick(int position){
        view.startDetail(position);
    }

    @Override
    public void clickAddButton(){
        view.startEdit();
    }


}


