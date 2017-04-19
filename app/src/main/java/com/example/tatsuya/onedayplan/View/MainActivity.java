package com.example.tatsuya.onedayplan.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

import com.example.tatsuya.onedayplan.Contract.OneDayPlanContract;
import com.example.tatsuya.onedayplan.Presenter.OneDayPlanPresenter;
import com.example.tatsuya.onedayplan.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements OneDayPlanContract.View {

   private OneDayPlanPresenter oneDayPlanPresenter=new OneDayPlanPresenter(this);
   private List<ListItem> onedayData =new ArrayList<>();
   private RecyclerView onedayList;
   private ListAdapter adapter;
   private boolean checkTest;
   private boolean checkHomeWork;
   private String title;
   private String remark;
    private String list;
    private FloatingActionButton addButton;
//    SharedPreferences oneDayPlanLoad;
//    SharedPreferences.Editor saveEditor;
//    JSONArray saveListData;
    private Realm oneDaySaveData;
    private int position;

   private static final int RESULTCODE=10;
   private static final int REQUESTEDITCODE=1000;
    private static final int REQUESTDETAILCODE=1001;
   private static  String intentTitle="title";
   private static  String intentCheckTest="test";
   private static  String intentCheckHomeWork="homework";
   private static  String intentRemark="remark";
    private static String intentTarget="target";
//    private static  String saveTitle="SaveTitle";
//   private static  String saveRemark="SaveRemark";
//   private static  String saveCheckTest="SaveCheckTest";
//   private static  String saveCheckHomeWork="SaveCheckHomeWork";
//    private static  String saveList="SaveList";


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewSetUp();
        getDataLoad();

        addButton.setOnClickListener(clickAddButton);

        showOneDayList(onedayData);

        adapter.setItemListClickListener(clickList);
        new ItemTouchHelper(listAction).attachToRecyclerView(onedayList);

    }

    public void viewSetUp(){
        oneDaySaveData.init(this);
        oneDaySaveData=Realm.getDefaultInstance();
        onedayData=getDataLoad();
        onedayList = (RecyclerView) findViewById(R.id.oneday_list);
        showOneDayList(onedayData);

        addButton = (FloatingActionButton) findViewById(R.id.addbutton);
        //oneDayPlanLoad=getSharedPreferences("Save", Context.MODE_PRIVATE);

    }

    public List<ListItem> getDataLoad(){
        RealmResults<ListItem> saveresult=oneDaySaveData.where(ListItem.class).findAll();
        return saveresult;
    }

    public ItemTouchHelper.SimpleCallback listAction = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            ListItem fromlistItem=onedayData.get(viewHolder.getAdapterPosition());

            title=fromlistItem.getTitle();
            checkTest=fromlistItem.getTestCheck();//a
            checkHomeWork=fromlistItem.getHomeworkCheck();
            remark=fromlistItem.getRemark();
            oneDaySaveData.beginTransaction();
            ListItem tolistItem=onedayData.get(target.getAdapterPosition());

            fromlistItem.setTitle(tolistItem.getTitle());
            fromlistItem.setTestCheck(tolistItem.getTestCheck());
            fromlistItem.setHomeworkCheck(tolistItem.getHomeworkCheck());
            fromlistItem.setRemark(tolistItem.getRemark());

            tolistItem.setTitle(title);
            tolistItem.setTestCheck(checkTest);
            tolistItem.setHomeworkCheck(checkHomeWork);
            tolistItem.setRemark(remark);

            Log.d("hoge",String.valueOf(target.getAdapterPosition()));
            oneDaySaveData.commitTransaction();

            adapter.move(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            adapter.refreshItem(getDataLoad());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            oneDaySaveData.beginTransaction();
            onedayData.get(viewHolder.getAdapterPosition()).deleteFromRealm();
            oneDaySaveData.commitTransaction();
            adapter.removeAtPosition(viewHolder.getAdapterPosition());
            adapter.refreshItem(getDataLoad());


        }
    };

    public View.OnClickListener clickList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            position = onedayList.getChildAdapterPosition(v);
            oneDayPlanPresenter.clickOneDayClick(position);
        }
    };

    public View.OnClickListener clickAddButton=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            oneDayPlanPresenter.clickAddButton();
        }
    };


    @Override
    public void showOneDayList(List<ListItem> oneDayPlanList) {

        adapter = new ListAdapter(this, onedayData);
        onedayList.setAdapter(adapter);
        onedayList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    public void startDetail(int position) {
        ListItem listItem=adapter.datalist.get(position);
        Intent detailIntent=new Intent(getApplicationContext(),OneDayPlanDetail.class);
        detailIntent.putExtra(intentTitle,listItem.getTitle());
        detailIntent.putExtra(intentCheckTest,listItem.getTestCheck());
        detailIntent.putExtra(intentCheckHomeWork,listItem.getHomeworkCheck());
        detailIntent.putExtra(intentRemark,listItem.getRemark());
        startActivityForResult(detailIntent,REQUESTDETAILCODE);

    }


    @Override
    public void startEdit(){
        Intent editIntent=new Intent(getApplicationContext(),OneDayPlanEdit.class);
        editIntent.putExtra(intentCheckHomeWork,false);
        editIntent.putExtra(intentCheckTest,false);
        editIntent.putExtra(intentTarget,REQUESTEDITCODE);
        startActivityForResult(editIntent,REQUESTEDITCODE);

    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);

        //saveEditor=oneDayPlanLoad.edit();

        switch (requestCode){
            case REQUESTDETAILCODE:
                if (resultCode==RESULTCODE){
                    title=intent.getStringExtra(intentTitle);
                    checkTest=intent.getBooleanExtra(intentCheckTest,false);
                    checkHomeWork=intent.getBooleanExtra(intentCheckHomeWork,false);
                    remark=intent.getStringExtra(intentRemark);
                    SaveData(position);
                }
                break;


            case REQUESTEDITCODE:
                if (resultCode==RESULTCODE){
                    title=intent.getStringExtra(intentTitle);
                    checkTest=intent.getBooleanExtra(intentCheckTest,false);
                    checkHomeWork=intent.getBooleanExtra(intentCheckHomeWork,false);
                    remark=intent.getStringExtra(intentRemark);
                    newSaveData();

                }
                break;
    }
}


    public void newSaveData(){
        oneDaySaveData.beginTransaction();
        ListItem listItem=oneDaySaveData.createObject(ListItem.class);
        listItem.setTitle(title);
        listItem.setTestCheck(checkTest);
        listItem.setHomeworkCheck(checkHomeWork);
        listItem.setRemark(remark);
        oneDaySaveData.commitTransaction();
        adapter.refreshItem(getDataLoad());
    }
    public void SaveData(int position){
        oneDaySaveData.beginTransaction();
        ListItem listItem=adapter.datalist.get(position);
        listItem.setTitle(title);
        listItem.setTestCheck(checkTest);
        listItem.setHomeworkCheck(checkHomeWork);
        listItem.setRemark(remark);
        oneDaySaveData.commitTransaction();
        adapter.refreshItem(getDataLoad());
    }

}
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
