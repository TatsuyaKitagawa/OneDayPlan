package com.example.tatsuya.onedayplan.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tatsuya.onedayplan.Contract.EditContract;
import com.example.tatsuya.onedayplan.Presenter.EditPresenter;
import com.example.tatsuya.onedayplan.R;

/**
 * Created by tatsuya on 2017/03/23.
 */

public class OneDayPlanEdit extends AppCompatActivity implements EditContract.View{

    EditPresenter editPresenter;
    String titleText;
    String remarkText;
    Boolean testCheckboolean=false;
    Boolean homeworkcheckboolean=false;

    Boolean sendHomeWork;
    Boolean sendTest;

    EditText titleEdit;
    EditText remarkEdit;

    Intent intent;

    Button testcheck;
    Button homeworkcheck;

    int target;

    private static final int RESULTCODE=10;
    private static final int REQUESTEDITCODE=1000;
    private static final int REQUESTDETAILCODE=1001;
    private static  String intentTitle="title";
    private static  String intentCheckTest="test";
    private static  String intentCheckHomeWork="homework";
    private static  String intentRemark="remark";
    private static String intentTarget="target";

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_edit);

        editPresenter=new EditPresenter(this);

        titleEdit=(EditText)findViewById(R.id.title);
        remarkEdit=(EditText)findViewById(R.id.remarks);
        testcheck=(Button) findViewById(R.id.testcheck);
        homeworkcheck=(Button) findViewById(R.id.homeworkcheck);

        intent=getIntent();
        target=intent.getIntExtra(intentTarget,1000);
        if(target!=REQUESTEDITCODE) {
            titleText = intent.getStringExtra(intentTitle);
            remarkText = intent.getStringExtra(intentRemark);
            testCheckboolean = intent.getBooleanExtra(intentCheckTest, false);
            homeworkcheckboolean = intent.getBooleanExtra(intentCheckHomeWork, false);

            showTestCheck(testCheckboolean);
            showHomeWorkCheck(homeworkcheckboolean);
            showTitle(titleText);
            showRemark(remarkText);
        }
        testcheck.setOnClickListener(checkClick);
        homeworkcheck.setOnClickListener(checkClick);
    }

    public View.OnClickListener checkClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.testcheck){
                editPresenter.clickTestCheck(testCheckboolean);
            }
            if (view.getId()==R.id.homeworkcheck){
                editPresenter.clickHomeWorkCheck(homeworkcheckboolean);
            }
        }
    };

    @Override
    public void showTitle(String title){
      titleEdit.setText(title);


    }
    @Override
    public void showRemark(String remark){
        remarkEdit.setText(remark);


    };
    @Override
    public void showTestCheck(boolean testcheck){
        sendTest=testCheckboolean;
        if(testcheck) {
            this.testcheck.setBackgroundColor(Color.RED);
            testCheckboolean=false;
        }else{
            this.testcheck.setBackgroundColor(Color.WHITE);
            testCheckboolean=true;
        }
    }
    @Override
    public void showHomeWorkCheck(boolean homeworkcheck){
        sendHomeWork=homeworkcheckboolean;
        if(homeworkcheck) {
            this.homeworkcheck.setBackgroundColor(Color.RED);
            homeworkcheckboolean=false;
        }else{
            this.homeworkcheck.setBackgroundColor(Color.WHITE);
            homeworkcheckboolean=true;
        }
    }
    @Override
    public void backJump(){
        intent.putExtra(intentTitle,titleEdit.getText().toString());
        intent.putExtra(intentCheckTest,sendTest);
        intent.putExtra(intentCheckHomeWork,sendHomeWork);
        intent.putExtra(intentRemark,remarkEdit.getText().toString());
        setResult(RESULTCODE,intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        backJump();
    }
}

