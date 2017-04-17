package com.example.tatsuya.onedayplan.Presenter;

import com.example.tatsuya.onedayplan.Contract.EditContract;

/**
 * Created by tatsuya on 2017/03/23.
 */

public class EditPresenter implements EditContract.Action {

    EditContract.View view;
    public EditPresenter(EditContract.View view) {
        this.view=view;
    }
    @Override
    public void clickTestCheck(boolean testcheck){
        view.showTestCheck(testcheck);
    };
    @Override
    public void clickHomeWorkCheck(boolean homeworkcheck){
        view.showHomeWorkCheck(homeworkcheck);
    };
    @Override
    public void clickBackButton(){};
    @Override
    public void clickSaveButton(){};
}
