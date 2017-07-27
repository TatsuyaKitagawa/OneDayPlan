package com.example.tatsuya.onedayplan.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.tatsuya.onedayplan.Contract.OneDayPlanContract;
import com.example.tatsuya.onedayplan.Presenter.OneDayPlanPresenter;
import com.example.tatsuya.onedayplan.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class OnedayFragmentView extends Fragment implements OneDayPlanContract.View{

    private OneDayPlanPresenter oneDayPlanPresenter=new OneDayPlanPresenter(this);
    private List<ListItem> onedayData =new ArrayList<>();
    private ListAdapter adapter;
    private boolean checkTest;
    private boolean checkHomeWork;
    private String title;
    private String remark;
    private String list;
    private FloatingActionButton addButton;
    private Realm oneDaySaveData;
    private int position;
    private String correntday;

    private static final int RESULTCODE=10;
    private static final int REQUESTEDITCODE=1000;
    private static final int REQUESTDETAILCODE=1001;
    private static  String intentTitle="title";
    private static  String intentCheckTest="test";
    private static  String intentCheckHomeWork="homework";
    private static  String intentRemark="remark";
    private static String intentTarget="target";
    private RecyclerView onedayList;
    private int m_mode;
    private static final  String KEY="key";
    private View view;
    private FrameLayout frameLayout;

    public static OnedayFragmentView newInstance(int mode) {
        OnedayFragmentView frag = new OnedayFragmentView();
        Bundle b = new Bundle();
        b.putInt(KEY, mode);
        frag.setArguments(b);
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b_in=getArguments();
        m_mode=b_in.getInt(KEY);
        view=inflater.inflate(R.layout.fragment_view,container,false);
        frameLayout=(FrameLayout)view.findViewById(R.id.flagmentlistlayout);
        setView();
        return frameLayout;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState){
        super.onViewCreated(view,saveInstanceState);

        viewSetUp();
    }

    public void viewSetUp(){
        oneDaySaveData.init(getActivity());
        oneDaySaveData=Realm.getDefaultInstance();
        onedayData=getDataLoad();
        onedayList = (RecyclerView)view.findViewById(R.id.oneday_list);
        showOneDayList(onedayData);

        adapter.setItemListClickListener(clickList);
        new ItemTouchHelper(listAction).attachToRecyclerView(onedayList);

        addButton = (FloatingActionButton)view.findViewById(R.id.addbutton);
        addButton.setOnClickListener(clickAddButton);

    }

    public List<ListItem> getDataLoad(){

        RealmResults<ListItem> saveresult=oneDaySaveData.where(ListItem.class).equalTo("day",correntday).findAll();
        return saveresult;
    }

    public ItemTouchHelper.SimpleCallback listAction = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            ListItem fromlistItem=onedayData.get(viewHolder.getAdapterPosition());

            title=fromlistItem.getTitle();
            checkTest=fromlistItem.getTestCheck();
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
            oneDaySaveData.commitTransaction();
            adapter.move(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            oneDaySaveData.beginTransaction();
            onedayData.get(viewHolder.getAdapterPosition()).deleteFromRealm();
            oneDaySaveData.commitTransaction();
            adapter.removeAtPosition(viewHolder.getAdapterPosition());
        }
    };

    public View.OnClickListener clickAddButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            oneDayPlanPresenter.clickAddButton();
        }
    };

    public View.OnClickListener clickList =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            position = onedayList.getChildAdapterPosition(v);
            oneDayPlanPresenter.clickOneDayClick(position);
        }
    };



    @Override
    public void showOneDayList(List<ListItem> onedayplanlist){

        adapter = new ListAdapter(getActivity(), onedayData);
        onedayList.setAdapter(adapter);
        Log.d("aaaa",String.valueOf(adapter.getItemCount()));
        onedayList.setLayoutManager(new LinearLayoutManager(getContext()));
    };

    @Override
    public void startDetail(int position) {
        ListItem listItem=adapter.datalist.get(position);
        Intent detailIntent=new Intent(getActivity().getApplicationContext(),OneDayPlanDetail.class);
        detailIntent.putExtra(intentTitle,listItem.getTitle());
        detailIntent.putExtra(intentCheckTest,listItem.getTestCheck());
        detailIntent.putExtra(intentCheckHomeWork,listItem.getHomeworkCheck());
        detailIntent.putExtra(intentRemark,listItem.getRemark());
        startActivityForResult(detailIntent,REQUESTDETAILCODE);
    }

    @Override
    public void startEdit(){
            Intent editIntent=new Intent(getActivity().getApplicationContext(),OneDayPlanEdit.class);
            editIntent.putExtra(intentCheckHomeWork,false);
            editIntent.putExtra(intentCheckTest,false);
            editIntent.putExtra(intentTarget,REQUESTEDITCODE);
            startActivityForResult(editIntent,REQUESTEDITCODE);
        }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case REQUESTDETAILCODE:
                if (resultCode == RESULTCODE) {
                    title = intent.getStringExtra(intentTitle);
                    checkTest = intent.getBooleanExtra(intentCheckTest, false);
                    checkHomeWork = intent.getBooleanExtra(intentCheckHomeWork, false);
                    remark = intent.getStringExtra(intentRemark);
                    SaveData(position);
                }
                break;


            case REQUESTEDITCODE:
                if (resultCode == RESULTCODE) {
                    title = intent.getStringExtra(intentTitle);
                    checkTest = intent.getBooleanExtra(intentCheckTest, false);
                    checkHomeWork = intent.getBooleanExtra(intentCheckHomeWork, false);
                    remark = intent.getStringExtra(intentRemark);
                    newSaveData();

                }
                break;
        }
    }

    public void newSaveData(){

        oneDaySaveData.beginTransaction();
        ListItem listItem=oneDaySaveData.createObject(ListItem.class);
        listItem.setDay(correntday);
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

    public void setView(){

        switch (m_mode){
            case 0:
                correntday="月曜日";
                frameLayout.setBackgroundResource(android.R.color.holo_blue_bright);
                break;
            case 1:
                correntday="火曜日";
                frameLayout.setBackgroundResource(android.R.color.holo_green_light);
                break;
            case 2:
                correntday="水曜日";
                frameLayout.setBackgroundResource(android.R.color.holo_red_dark);
                break;
            case 3:
                correntday="木曜日";
                frameLayout.setBackgroundResource(android.R.color.holo_purple);
                break;
            case 4:
                correntday="金曜日";
                frameLayout.setBackgroundResource(android.R.color.holo_orange_light);
                break;
        }
    }

}
