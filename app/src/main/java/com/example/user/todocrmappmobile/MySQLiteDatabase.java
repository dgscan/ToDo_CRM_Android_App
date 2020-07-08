package com.example.user.todocrmappmobile;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class MySQLiteDatabase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "ToDoCRMApp";

    public MySQLiteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERTABLE="CREATE TABLE usertable " +
                "(username TEXT PRIMARY KEY NOT NULL, " +
                "password TEXT NOT NULL," +
                "name TEXT NOT NULL," +
                "surname TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "avatar TEXT)";

        String CREATE_CUSTOMERTABLE = "CREATE TABLE customertable (taxnr TEXT PRIMARY KEY NOT NULL," +
                "companyname TEXT NOT NULL," +
                "customername TEXT NOT NULL," +
                "customersurname TEXT NOT NULL," +
                "address TEXT NOT NULL," +
                "email TEXT," +
                "telephone TEXT NOT NULL)";

        String  CREATE_TASKTABLE = "CREATE TABLE tasktable (taskid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "customerid TEXT NOT NULL," +
                "startdate TEXT," +
                "uptodate TEXT," +
                "description TEXT," +
                "status TEXT," +
                "employeid TEXT," +
                "offerno TEXT," +
                "proformano TEXT)";

        db.execSQL(CREATE_USERTABLE);
        db.execSQL(CREATE_CUSTOMERTABLE);
        db.execSQL(CREATE_TASKTABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE usertable");
        db.execSQL("DROP TABLE customertable");
        db.execSQL("DROP TABLE tasktable");
        this.onCreate(db);
    }

    /*KULLANICI BİLGİLERİNİ KONTROL ET DOĞRUYSA KULLANICI ADINI DÖNDÜR
    * */
    public String UserLogin(String username, String password){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usertable WHERE username=? AND password=?",new String[]{username,password});

        if(cursor != null && cursor.moveToFirst()){

            String result = "";
            result = cursor.getString(0);
            db.close();

            return result;

        }
        else{
            db.close();
            return "notfound!\n";
        }
    }

    /*KULLANICIYI DATABASE'E KAYDET
    * */
    public long UserRegister(String username, String password, String name, String surname, String email){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Log.d("rgstr","username: "+username+" pass: "+password);

        values.put("username",username);
        values.put("password",password);
        values.put("name",name);
        values.put("surname",surname);
        values.put("email",email);

        long result = db.insert("usertable",null,values);
        db.close();

        return result;

    }

    /*TÜM KULLANICILARI LİSTELE
    * */
    public String ListUsers(){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("usertable", new String[]{"username","password","name","surname","email"},null,null,null,null,null,null);


        if(cursor != null && cursor.moveToFirst()){

            String result ="";

            result = cursor.getString(0)+":"+cursor.getString(1)+":"+cursor.getString(2)+"\n";

            while (!cursor.isLast()){

                cursor.moveToNext();
                result+=cursor.getString(0);

            }

            db.close();
            return result;
        }
        else return "Database is empty!";

    }

    public ArrayList<String> GetNameAndEmailByUsername(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT name, surname, email FROM usertable WHERE username=?", new String[]{username});

        ArrayList<String> result = new ArrayList<String>();

        if(cursor!=null && cursor.moveToFirst()){

            result.add(cursor.getString(0)); //name
            result.add(cursor.getString(1)); //surname
            result.add(cursor.getString(2)); //email

            db.close();
            return result;
        }
        else {
            db.close();
            return result;
        }
    }

    public long NewCustomerRegister(ArrayList<String> customerCard){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("taxnr",customerCard.get(0));
        values.put("companyname",customerCard.get(1));
        values.put("customername",customerCard.get(2));
        values.put("customersurname",customerCard.get(3));
        values.put("address",customerCard.get(4));
        values.put("email",customerCard.get(5));
        values.put("telephone",customerCard.get(6));

        long result = db.insert("customertable",null,values);
        db.close();

        return result;

    }

    public ArrayList<ArrayList<String>> ListCustomers(){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("customertable", new String[]{"taxnr","companyname","customername","customersurname","address",
        "email","telephone"},null,null,null,null,null,null);

        ArrayList<ArrayList<String>> result = new ArrayList<>();

        int i =0;

        while (cursor.moveToNext()){

            ArrayList<String> internal = new ArrayList<>();

            internal.add(cursor.getString(0)+"");
            internal.add(cursor.getString(1)+"");
            internal.add(cursor.getString(2)+"");
            internal.add(cursor.getString(3)+"");
            internal.add(cursor.getString(4)+"");
            internal.add(cursor.getString(5)+"");
            internal.add(cursor.getString(6)+"");

            result.add(internal);

            Log.d("Result:",result.get(i).get(1));
            i++;
        }
            db.close();
            return result;
    }

    public long UpdateCustomerCard (ArrayList<String> customerCard){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Cursor cursor = db.rawQuery("UPDATE customertable SET companyname=? AND customername=? AND customersurname=? " +
            //    "AND address=? AND email=? AND telephone=? WHERE taxnr=?",new String[]{customerCard.get(1),customerCard.get(2),
          //      customerCard.get(3),customerCard.get(4),customerCard.get(5),customerCard.get(6),customerCard.get(0)});

        values.put("companyname", customerCard.get(1));
        values.put("customername", customerCard.get(2));
        values.put("customersurname", customerCard.get(3));
        values.put("address", customerCard.get(4));
        values.put("email", customerCard.get(5));
        values.put("telephone", customerCard.get(6));

        long result=db.update("customertable",values,"taxnr=?",new String[]{customerCard.get(0)+""});

        return result;
    }

    public long AddTask (String username, String customerID, String description){

        Log.d("addtask:", username+customerID+description + "");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Date currentTime = Calendar.getInstance().getTime();

        values.put("customerid",customerID);
        values.put("startdate",currentTime.toString());
        values.put("uptodate",currentTime.toString());
        values.put("description",description);
        values.put("status","notstarted");
        values.put("employeid",username);
        values.put("offerno","N/A");
        values.put("proformano","N/A");


        long result = db.insert("tasktable",null,values);
        db.close();

        return result;
    }

    public List<TaskList> GetTasksByEmployee(String employeeID){

        String query = "SELECT * FROM tasktable t, customertable c WHERE t.customerid=c.taxnr AND t.employeid='"+employeeID+"'";

        List<TaskList> tasksList = new LinkedList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        TaskList todolist;

        if(cursor!=null){

            while (cursor.moveToNext()) {

                todolist = new TaskList();
                todolist.setCompanyname(cursor.getString(10));
                todolist.setTaskTxt(cursor.getString(4));
                todolist.setStartDate(cursor.getString(2));
                todolist.setUptoDate(cursor.getString(3));
                todolist.setTaskid(cursor.getString(0));

                Log.d("gettaskdb", cursor.getString(3) + " \n" + "task: " + cursor.getString(4));

                tasksList.add(todolist);
            }
        }

        return tasksList;

    }

    public long UpdateTaskCard(String taskid, String taskTxt, String status, String offerno, String proformano ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Date currentTime = Calendar.getInstance().getTime();

        values.put("description", taskTxt);
        values.put("status", status);
        values.put("offerno", offerno);
        values.put("proformano",proformano);
        values.put("uptodate",currentTime.toString());

        long result = db.update("tasktable",values,"taskid=?",new String[]{taskid+""});

        return result;
    }

    public ArrayList<String> ListTask (String taskid){

        Log.d("dbhata","geldi");

        ArrayList <String> task = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query ="SELECT * FROM tasktable t, customertable c WHERE t.customerid=c.taxnr AND t.taskid='"+Integer.parseInt(taskid)+"'";

        Cursor cursor = db.rawQuery(query, null);

        if(cursor!=null){
            while (cursor.moveToNext()){

                task.add(cursor.getString(10));
                task.add(cursor.getString(2));
                task.add(cursor.getString(3));
                task.add(cursor.getString(7));
                task.add(cursor.getString(8));
                task.add(cursor.getString(4));

            }
        }
        return task;
    }

}