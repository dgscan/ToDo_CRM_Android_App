package com.example.user.todocrmappmobile;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomerUpdateActivity extends AppCompatActivity {

    private ArrayList<ArrayList<String>> customerList;
    private ArrayList<String> customerNameList;
    private ArrayAdapter<String> customerNameListAdapter;
    MySQLiteDatabase db = new MySQLiteDatabase(this);
    private ListView listView;
    private TextView selectedCustomerName;
    private String selectedCustomerId;

    private TextView taxnr;
    private EditText companyname;
    private EditText customername;
    private EditText customersurname;
    private EditText address;
    private EditText email;
    private EditText telephone;

    private AlertDialog.Builder dialogBuilder, confirmBuilder;
    private AlertDialog dialog, confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_update);

        listView = findViewById(R.id.listView1);
        LoadCustomerListtoAdapter();

    }

    public void LoadCustomerListtoAdapter(){

        customerNameList = new ArrayList<>();
        customerList = db.ListCustomers();

        if (customerList.size()>0) {

            Toast.makeText(this,"size:"+customerList.size()+" "+customerList.get(0).get(0),Toast.LENGTH_LONG).show();

            for (int i=0; i<customerList.size();i++){

                customerNameList.add(customerList.get(i).get(1)); //sadece müşteri isimlerini farklı bir array liste ata

                Log.d("customerlist",customerList.get(i).get(1)+"");
            }
            customerNameListAdapter = new ArrayAdapter<>(this, R.layout.customer_update_row,customerNameList);
            listView.setAdapter(customerNameListAdapter);
        }
        else {
            Toast.makeText(this,"Müşteri Listesi Boş!",Toast.LENGTH_LONG).show();
        }
    }

    public void ItemClicked(View v){

        selectedCustomerName = (TextView) v;

        taxnr = new TextView(this);
        companyname = new EditText(this);
        customername = new EditText(this);
        customersurname = new EditText(this);
        address = new EditText(this);
        email = new EditText(this);
        telephone = new EditText(this);

        /*FIND SELECTED CUSTOMER ID
        * */
        for (int i=0; i<customerList.size();i++){
            if(customerList.get(i).get(1).equals(selectedCustomerName.getText().toString())){

                taxnr.setText(customerList.get(i).get(0));
                companyname.setText(customerList.get(i).get(1));
                customername.setText(customerList.get(i).get(2));
                customersurname.setText(customerList.get(i).get(3));
                address.setText(customerList.get(i).get(4));
                email.setText(customerList.get(i).get(5));
                telephone.setText(customerList.get(i).get(6));
            }
        }

        Toast.makeText(this,"Customer ID:"+taxnr.getText().toString(),Toast.LENGTH_LONG).show();

        CreateCustomerUpdateModal();
        dialog.show();
    }

    public void CreateCustomerUpdateModal(){

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Müşteri Kartını Düzenle:");

        final TextView taxnrTag = new TextView(this );
        final TextView companynameTag = new TextView(this );
        final TextView customernameTag = new TextView(this );
        final TextView addressTag = new TextView(this );
        final TextView emailTag = new TextView(this );


        LinearLayout rootLinearLayout = new LinearLayout(this);
        LinearLayout h1Layout = new LinearLayout(this);
        LinearLayout h2Layout = new LinearLayout(this);
        LinearLayout h3Layout = new LinearLayout(this);
        LinearLayout h4Layout = new LinearLayout(this);
        LinearLayout h5Layout = new LinearLayout(this);

        taxnrTag.setText("Vergi No:");
        taxnrTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        taxnr.setSingleLine();
        taxnr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));

        h1Layout.setOrientation(LinearLayout.HORIZONTAL);
        h1Layout.addView(taxnrTag);
        h1Layout.addView(taxnr);

        companynameTag.setText("Firma Adı:");
        companynameTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);

        companyname.setSingleLine();
        companyname.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));

        h2Layout.setOrientation(LinearLayout.HORIZONTAL);
        h2Layout.addView(companynameTag);
        h2Layout.addView(companyname);

        customernameTag.setText("Müşteri Adı & Soyadı:");
        customernameTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);

        customername.setSingleLine();
        customersurname.setSingleLine();
        customername.setLayoutParams(new LinearLayout.LayoutParams(250, 100));
        customersurname.setLayoutParams(new LinearLayout.LayoutParams(250, 100));

        h3Layout.setOrientation(LinearLayout.HORIZONTAL);
        h3Layout.addView(customernameTag);
        h3Layout.addView(customername);
        h3Layout.addView(customersurname);

        addressTag.setText("Adres:");
        addressTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);

        address.setSingleLine();
        address.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));

        h4Layout.setOrientation(LinearLayout.HORIZONTAL);
        h4Layout.addView(addressTag);
        h4Layout.addView(address);

        emailTag.setText("E-mail & Telefon:");
        emailTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);

        email.setSingleLine();
        telephone.setSingleLine();
        email.setLayoutParams(new LinearLayout.LayoutParams(250, 100));
        telephone.setLayoutParams(new LinearLayout.LayoutParams(250, 100));

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

                    long result = db.UpdateCustomerCard(customerCard);

                    if(result>=0){
                        dialog.dismiss();
                        LoadCustomerListtoAdapter();
                        Toast.makeText(CustomerUpdateActivity.this,"Müşteri bilgileri güncellendi!",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(CustomerUpdateActivity.this,"Müşteri güncellenemedi: Database hatası!",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(CustomerUpdateActivity.this,"E-mail dışındaki tüm alanlar girilmelidir!",Toast.LENGTH_LONG).show();
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
