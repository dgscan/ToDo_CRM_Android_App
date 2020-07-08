package com.example.user.todocrmappmobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private String usernameStr;
    private String passwordStr;
    private String nameStr;
    private String lastnameStr;
    private String emailStr;

    MySQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new MySQLiteDatabase(this);
    }

    public void RegisterOnClick(View v){
        EditText username = (EditText) findViewById(R.id.usernameTxt);
        EditText password = (EditText) findViewById(R.id.passwordTxt);
        EditText name = (EditText) findViewById(R.id.nameTxt);
        EditText lastname = (EditText) findViewById(R.id.lastnameTxt);
        EditText email = (EditText) findViewById(R.id.emailTxt);

        usernameStr = username.getText().toString();
        passwordStr = password.getText().toString();
        nameStr = name.getText().toString();
        lastnameStr = lastname.getText().toString();
        emailStr = email.getText().toString();

        if (!usernameStr.equals("") && !passwordStr.equals("") && !nameStr.equals("") && !lastnameStr.equals("") && !emailStr.equals("")){

            Log.d("getuserinfo","uname: "+usernameStr+" pass: "+passwordStr);

            try {

                long result = db.UserRegister(usernameStr,passwordStr,nameStr,lastnameStr,emailStr);

                /*CHECK IF USER ALREADY REGISTERED WITH SAME ID
                * */
                if (result>=0){
                    Toast.makeText(this,"Kayıt yapıldı!",Toast.LENGTH_LONG).show();

                    username.setText("");
                    password.setText("");
                    name.setText("");
                    lastname.setText("");
                    email.setText("");
                }
                else {
                    Toast.makeText(this,"Kayıt yapılamadı!: Database hatası",Toast.LENGTH_LONG).show();
                }

            }catch (Exception ex){
                Toast.makeText(this,"Kayıt yapılamadı! Hata Kodu: "+ex.toString(),Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this,"Hiç bir alan boş bırakılamaz!",Toast.LENGTH_LONG).show();
        }

        //String res = db.ListUsers();
        //Toast.makeText(this,"result: "+res,Toast.LENGTH_LONG).show();

    }


}
