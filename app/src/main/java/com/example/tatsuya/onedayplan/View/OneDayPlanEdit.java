package com.example.tatsuya.onedayplan.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tatsuya.onedayplan.Contract.EditContract;
import com.example.tatsuya.onedayplan.Presenter.EditPresenter;
import com.example.tatsuya.onedayplan.R;

/**
 * Created by tatsuya on 2017/03/23.
 */

public class OneDayPlanEdit extends AppCompatActivity implements EditContract.View{

    public EditPresenter editPresenter;
    private String titleText;
    private String remarkText;
    private Boolean testCheckboolean=false;
    private Boolean homeworkcheckboolean=false;

    private Boolean sendHomeWork;
    private Boolean sendTest;

    private EditText titleEdit;
    private EditText remarkEdit;

    private Intent intent;

    private Button testcheck;
    private Button homeworkcheck;

    private int target;

    private AlertDialog.Builder saveDialog;

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

        saveDialog =new AlertDialog.Builder(this);

        intent=getIntent();
        target=intent.getIntExtra(intentTarget,1000);
        if(target!=REQUESTEDITCODE) {
            titleText = intent.getStringExtra(intentTitle);
            remarkText = intent.getStringExtra(intentRemark);
            testCheckboolean = intent.getBooleanExtra(intentCheckTest, false);
            homeworkcheckboolean = intent.getBooleanExtra(intentCheckHomeWork, false);


            showTitle(titleText);
            showRemark(remarkText);
        }
        showDialog();
        showTestCheck(testCheckboolean);
        showHomeWorkCheck(homeworkcheckboolean);
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
            this.testcheck.setBackgroundColor(Color.LTGRAY);
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
            this.homeworkcheck.setBackgroundColor(Color.LTGRAY);
            homeworkcheckboolean=true;
        }
    }
    @Override
    public void backJump(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("保存しますか");
        alertDialogBuilder.setPositiveButton("保存",
        new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int id){
                intent.putExtra(intentTitle,titleEdit.getText().toString());
                intent.putExtra(intentCheckTest,sendTest);
                intent.putExtra(intentCheckHomeWork,sendHomeWork);
                intent.putExtra(intentRemark,remarkEdit.getText().toString());
                setResult(RESULTCODE,intent);
                finish();
            }
        });


        // アラートダイアログの中立ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setNeutralButton("保存せず閉じる",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int id){
                        setResult(-100,intent);
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("キャンセル",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int id){}
                });
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        // アラートダイアログを表示します
        alertDialog.show();

    }



    @Override
    public void onBackPressed(){
        saveDialog.show();
    }
    public void showDialog(){
        saveDialog.setTitle("保存しますか?");
        saveDialog.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backJump();
            }
        }).
                setNegativeButton("終了",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).
                setNeutralButton("キャンセル",null);
    }
}

