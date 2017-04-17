package com.example.tatsuya.onedayplan.Presenter;

import com.example.tatsuya.onedayplan.Contract.DetailContract;

/**
 * Created by tatsuya on 2017/03/23.
 */

public class DetailPresenter implements DetailContract.Action {
    DetailContract.View view;

    public DetailPresenter(DetailContract.View view) {
        this.view=view;
    }

    @Override
    public void clickEditButton(){
        view.startEdit();
    };
}
