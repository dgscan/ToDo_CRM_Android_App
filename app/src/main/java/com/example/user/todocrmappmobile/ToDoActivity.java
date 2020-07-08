package com.example.user.todocrmappmobile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ToDoActivity extends AppCompatActivity {

    private String username;

    public TextView employeeName;
    public TextView employeeEmail;

    private ListView todoList;
    private DrawerLayout rootLayout;

    private AlertDialog.Builder dialogBuilder, confirmBuilder;
    private AlertDialog dialog, confirmDialog;

    private ArrayList<ArrayList<String>> customerAry;
    ArrayList<String> customerNameList;
    Spinner customerList;

    MySQLiteDatabase db;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private ItemAdapter adapter;
    private List<TaskList> tasksList;

    @Override
    protected void onResume() {
        super.onResume();

        /*POPULATE TODO TASK LIST HERE
         * */
        PopulateTodoTasks();
        //Toast.makeText(this,"güncel",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        final Intent intent = getIntent();
        username = intent.getStringExtra("username");

        todoList = (ListView) findViewById(R.id.listView1);
        rootLayout = findViewById(R.id.drawer_layout);

        /*GET NAVIGATION VIEW ELEMENTS BY ID
        * */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        employeeName = (TextView) headerView.findViewById(R.id.employeeName);
        employeeEmail = (TextView) headerView.findViewById(R.id.email);

        /*GET USER NAME & SURNAME AND EMAIL
        * */
        db = new MySQLiteDatabase(this);

        /*SHOW USER NAME & SURNAME AND EMAIL
        * */
        ArrayList<String> result;
        result = db.GetNameAndEmailByUsername(username);
        employeeName.setText(result.get(0)+" "+result.get(1));
        employeeEmail.setText(result.get(2));


        /*POPULATE TODO TASK LIST HERE
        * */
        PopulateTodoTasks();


        /*NAVIGATION VIEW*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getTitle().toString().startsWith("Müşteri Ekle")){
                    CreateCustomerModal();
                    dialog.show();
                }else if(menuItem.getTitle().toString().startsWith("Müşteri Bilgi")){
                    Intent updateCustomerIntent = new Intent(ToDoActivity.this,CustomerUpdateActivity.class);
                    startActivity(updateCustomerIntent);
                }
                else if(menuItem.getTitle().toString().startsWith("Yeni Görev")){
                    CreateAddTaskModal();
                    dialog.show();
                }
                else if(menuItem.getTitle().toString().startsWith("Çıkış")){
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else
                    return false;
                return true;
            }
        });

    }

    public void PopulateTodoTasks(){

        recyclerView = (RecyclerView)findViewById(R.id.taskListView);
        recyclerView.setHasFixedSize(true);

        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        adapter = new ItemAdapter(db.GetTasksByEmployee(username),this, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    public Spinner PopulateCustomerDropdownList(){

        Spinner s = new Spinner(this);

        customerNameList = new ArrayList<>();

        customerAry = db.ListCustomers();

        Log.d("customerary:", customerAry.size() + "");

        /*ADD CUSTOMER NAME BY ID
        * */
        if (customerAry.size()>0) {
            for (int i = 0; i < customerAry.size(); i++) {

                Log.d("customerary:", customerAry.get(i).get(1) + "");
                customerNameList.add(customerAry.get(i).get(1));
            }
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.simple_spinner_layout,customerNameList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_layout);
        s.setAdapter(spinnerArrayAdapter);

        return s;
    }

    public void CreateAddTaskModal(){

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Yeni Görev Ekle:");

        final TextView customerTag = new TextView(this);
        final TextView descriptionTag = new TextView(this);
        final TextView offerNoTag = new TextView(this);
        final TextView proformaNoTag = new TextView(this);

        customerList = PopulateCustomerDropdownList();



        final EditText description = new EditText(this);
        final EditText offerNo = new EditText(this);
        final EditText proformaNo = new EditText(this);
        LinearLayout rootLinearLayout = new LinearLayout(this);


        customerTag.setText("Lütfen ilgili müşteriyi seçiniz:");
        customerTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        customerList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));


        descriptionTag.setText("Görevi giriniz:");
        descriptionTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        description.setSingleLine();
        description.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));

        rootLinearLayout.setOrientation(LinearLayout.VERTICAL);
        rootLinearLayout.addView(customerTag);
        rootLinearLayout.addView(customerList);
        rootLinearLayout.addView(descriptionTag);
        rootLinearLayout.addView(description);

        dialogBuilder.setView(rootLinearLayout);


        /*OK BUTONUNA BASINCA NE YAPACAK
        * */
        dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                long result=0;

                Log.d("cust id",customerList.getSelectedItem().toString()+" desc:"+description.getText().toString());

                if (customerAry.size()>0) {
                    for (int i = 0; i < customerAry.size(); i++) {

                        if (customerList.getSelectedItem().toString().equals(customerAry.get(i).get(1))){

                            Log.d("selectedCustomerId:", customerAry.get(i).get(0) + "");
                            result = db.AddTask(username,customerAry.get(i).get(0),description.getText().toString());
                        }
                    }
                }

                if (result>=0){
                    //kayıt eklendi
                    Toast.makeText(ToDoActivity.this,"Yeni görev eklendi!",Toast.LENGTH_LONG).show();
                }
                else {
                    //kayıt eklenmedi
                    Toast.makeText(ToDoActivity.this,"Yeni görev eklenemedi. Hata!",Toast.LENGTH_LONG).show();
                }

                /*TODO TASK LIST'İ YENİLE*/
                PopulateTodoTasks();
            }
        });

        /*CANCEL BUTONUNA BASINCA NE YAPACAK
         * */
        dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = dialogBuilder.create();
    }

    public void CreateCustomerModal(){
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Yeni Müşteri Ekle:");

        final TextView taxnrTag = new TextView(this );
        final TextView companynameTag = new TextView(this );
        final TextView customernameTag = new TextView(this );
        final TextView addressTag = new TextView(this );
        final TextView emailTag = new TextView(this );

        final EditText taxnr = new EditText(this);
        final EditText companyname = new EditText(this);
        final EditText customername = new EditText(this);
        final EditText customersurname = new EditText(this);
        final EditText address = new EditText(this);
        final EditText email = new EditText(this);
        final EditText telephone = new EditText(this);

        LinearLayout rootLinearLayout = new LinearLayout(this);
        LinearLayout h1Layout = new LinearLayout(this);
        LinearLayout h2Layout = new LinearLayout(this);
        LinearLayout h3Layout = new LinearLayout(this);
        LinearLayout h4Layout = new LinearLayout(this);
        LinearLayout h5Layout = new LinearLayout(this);

        taxnrTag.setText("Vergi No:");
        taxnrTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        taxnr.setSingleLine();
        taxnr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));

        h1Layout.setOrientation(LinearLayout.HORIZONTAL);
        h1Layout.addView(taxnrTag);
        h1Layout.addView(taxnr);

        companynameTag.setText("Firma Adı:");
        companynameTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);

        companyname.setSingleLine();
        companyname.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));

        h2Layout.setOrientation(LinearLayout.HORIZONTAL);
        h2Layout.addView(companynameTag);
        h2Layout.addView(companyname);

        customernameTag.setText("Müşteri Adı & Soyadı:");
        customernameTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);

        customername.setSingleLine();
        customersurname.setSingleLine();
        customername.setLayoutParams(new LinearLayout.LayoutParams(250, 150));
        customersurname.setLayoutParams(new LinearLayout.LayoutParams(250, 150));

        h3Layout.setOrientation(LinearLayout.HORIZONTAL);
        h3Layout.addView(customernameTag);
        h3Layout.addView(customername);
        h3Layout.addView(customersurname);

        addressTag.setText("Adres:");
        addressTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);

        address.setSingleLine();
        address.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));

        h4Layout.setOrientation(LinearLayout.HORIZONTAL);
        h4Layout.addView(addressTag);
        h4Layout.addView(address);

        emailTag.setText("E-mail & Telefon:");
        emailTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);

        email.setSingleLine();
        telephone.setSingleLine();
        email.setLayoutParams(new LinearLayout.LayoutParams(250, 150));
        telephone.setLayoutParams(new LinearLayout.LayoutParams(250, 150));

        h5Layout.setOrientation(LinearLayout.HORIZONTAL);
        h5Layout.addView(emailTag);
        h5Layout.addView(email);
        h5Layout.addView(telephone);


        /*TÜM YATAY LAYOUTLARI ROOT LAYOUTA EKLE
        * */
        rootLinearLayout.setOrientation(LinearLayout.VERTICAL);
        rootLinearLayout.addView(h1Layout);
        rootLinearLayout.addView(h2Layout);
        rootLinearLayout.addView(h3Layout);
        rootLinearLayout.addView(h4Layout);
        rootLinearLayout.addView(h5Layout);

        dialogBuilder.setView(rootLinearLayout);

        //OK BUTONUNU YARAT VE BASILINCA YAPILACAKLARI SEÇ
        dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //OK BUTONUNA BASILINCA YAPILACAKLAR
                ArrayList<String> customerCard = new ArrayList<String>();
                customerCard.add(taxnr.getText().toString());
                customerCard.add(companyname.getText().toString());
                customerCard.add(customername.getText().toString());
                customerCard.add(customersurname.getText().toString());
                customerCard.add(address.getText().toString());
                customerCard.add(email.getText().toString());
                customerCard.add(telephone.getText().toString());

                if(!customerCard.get(0).equals("") && !customerCard.get(1).equals("") && !customerCard.get(2).equals("") &&
                        !customerCard.get(3).equals("") && !customerCard.get(4).equals("") && !customerCard.get(6).equals("")){

                    long result = db.NewCustomerRegister(customerCard);

                    if(result>=0){
                        dialog.dismiss();
                        Toast.makeText(ToDoActivity.this,"Yeni müşteri kaydedildi!",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(ToDoActivity.this,"Müşteri kaydedilemedi: Database hatası!",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(ToDoActivity.this,"E-mail dışındaki tüm alanlar girilmelidir!",Toast.LENGTH_LONG).show();
                }

            }
        });

        //CANCEL BUTONUNU YARAT VE BASILINCA YAPILACAKLARI SEÇ
        dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        dialog = dialogBuilder.create();
    }
}
