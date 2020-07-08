package com.example.user.todocrmappmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateTask extends AppCompatActivity {

    private TextView taskNo;
    private TextView companyName;
    private TextView startDate;
    private TextView uptoDate;
    private EditText offerNo;
    private EditText proformaNo;
    private RadioGroup status;
    private ArrayList<String> task;
    private String taskid;
    private TextView taskTxt;

    MySQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        db = new MySQLiteDatabase(this);
        taskNo = (TextView) findViewById(R.id.taskNoTxt);
        companyName = (TextView) findViewById(R.id.companyNameTxt);
        startDate = (TextView) findViewById(R.id.startDateTxt);
        status = (RadioGroup) findViewById(R.id.status);
        uptoDate = (TextView) findViewById(R.id.uptoDateTxt);
        offerNo = (EditText) findViewById(R.id.offerNoTxt);
        proformaNo =  (EditText) findViewById(R.id.proformaNoTxt);

        taskTxt = (EditText) findViewById(R.id.task);

        Intent intent = getIntent();
        taskid = intent.getStringExtra("taskid");

        taskNo.setText(taskid);

        LoadTask();
    }
    public void LoadTask(){

        task = db.ListTask(taskid);
        companyName.setText(task.get(0));
        startDate.setText(task.get(1));
        uptoDate.setText(task.get(2));
        offerNo.setText(task.get(3));
        proformaNo.setText(task.get(4));
        taskTxt.setText(task.get(5));
        Toast.makeText(this,task.size()+"",Toast.LENGTH_LONG).show();
    }

    public void UpdateTaskByID(View v){

        int index = status.indexOfChild(findViewById(status.getCheckedRadioButtonId()));
        String statusTxt ="notstarted";

        if (index==0){
            statusTxt.equals("notstarted");
        }
        else {
            statusTxt.equals("started");
        }

        long result = db.UpdateTaskCard(taskid,taskTxt.getText().toString(),statusTxt,offerNo.getText().toString(),proformaNo.getText().toString());

        if (result>=0){
            Toast.makeText(this,"Görev başarıyla güncellendi!",Toast.LENGTH_LONG).show();
            LoadTask();
        }
        else {
            Toast.makeText(this,"Görev güncellenmedi, Hata!",Toast.LENGTH_LONG).show();
        }


    }
}
