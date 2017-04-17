package com.example.tatsuya.onedayplan.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tatsuya.onedayplan.Contract.DetailContract;
import com.example.tatsuya.onedayplan.Presenter.DetailPresenter;
import com.example.tatsuya.onedayplan.R;

/**
 * Created by tatsuya on 2017/03/23.
 */

public class OneDayPlanDetail extends AppCompatActivity implements DetailContract.View {
    DetailPresenter detailPresenter;
    TextView title;
    TextView remark;
    TextView testcheck;
    TextView homeworkcheck;
    Intent mainIntent;

    String titleText;
    String remarkText;
    Boolean testCheckboolean;
    Boolean homeworkcheckboolean;

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
        setContentView(R.layout.activity_detail);

        detailPresenter=new DetailPresenter(this);

        Button editButton=(Button)findViewById(R.id.editbutton);
        title=(TextView)findViewById(R.id.title);
        remark=(TextView)findViewById(R.id.remark);
        testcheck=(Button)findViewById(R.id.testcheck);
        homeworkcheck=(Button)findViewById(R.id.homeworkcheck);

        mainIntent=getIntent();
        titleText=mainIntent.getStringExtra(intentTitle);
        remarkText=mainIntent.getStringExtra(intentRemark);
        testCheckboolean=mainIntent.getBooleanExtra(intentCheckTest,false);
        homeworkcheckboolean =mainIntent.getBooleanExtra(intentCheckHomeWork,false);

        viewShow();
        editButton.setOnClickListener(editButtonClick);
    }

    public void viewShow(){
        showTestCheck(testCheckboolean);
        showHomeWorkCheck(homeworkcheckboolean);
        showTitle(titleText);
        showRemark(remarkText);
    }

    public View.OnClickListener editButtonClick=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            detailPresenter.clickEditButton();
        }
    };

    @Override
    public void showTitle(String title){
        this.title.setText(title);

    }
    @Override
    public void showTestCheck(boolean testcheck){
        if(testcheck) {
            this.testcheck.setBackgroundColor(Color.RED);
        }else{
            this.testcheck.setBackgroundColor(Color.WHITE);
        }
    }
    @Override
    public void showHomeWorkCheck(boolean homework){
        if(homework) {
            this.homeworkcheck.setBackgroundColor(Color.RED);
        }else{
            this.homeworkcheck.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void showRemark(String remark){
        this.remark.setText(remark);
    }

    @Override
    public void startEdit(){
        Intent editIntent=new Intent(getApplicationContext(),OneDayPlanEdit.class);

        editIntent.putExtra(intentTarget,REQUESTDETAILCODE);
        editIntent.putExtra(intentTitle,titleText);
        editIntent.putExtra(intentCheckTest,testCheckboolean);
        editIntent.putExtra(intentCheckHomeWork, homeworkcheckboolean);
        editIntent.putExtra(intentRemark,remarkText);
        startActivityForResult(editIntent,REQUESTEDITCODE);
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);


        switch (requestCode){
            case REQUESTEDITCODE:
                if (resultCode==RESULTCODE){
//                    Toast.makeText(OneDayPlanDetail.this,String.valueOf(resultCode),Toast.LENGTH_LONG).show();
                    titleText=intent.getStringExtra(intentTitle);
                    testCheckboolean=intent.getBooleanExtra(intentCheckTest,false);
                    homeworkcheckboolean=intent.getBooleanExtra(intentCheckHomeWork,false);
                    remarkText=intent.getStringExtra(intentRemark);
                    viewShow();

                }
        }

    }

    @Override
    public void backMain(){
        mainIntent.putExtra(intentTitle,titleText);
        mainIntent.putExtra(intentCheckTest,testCheckboolean);
        mainIntent.putExtra(intentCheckHomeWork, homeworkcheckboolean);
        mainIntent.putExtra(intentRemark,remarkText);
        setResult(RESULTCODE,mainIntent);
        finish();
    }

    @Override
    public void onBackPressed(){
        backMain();
    }

}
