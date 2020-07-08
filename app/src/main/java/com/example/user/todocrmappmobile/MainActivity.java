package com.example.user.todocrmappmobile;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private String usernameStr;
    private String passwordStr;
    private EditText username;
    private EditText password;
    private TextView usernameTag;
    private TextView passwordTag;

    MySQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MySQLiteDatabase(this);

    }


    public void GetInputUserInfo(){

        username = (EditText) findViewById(R.id.usernameTxt);
        password = (EditText) findViewById(R.id.passwordTxt);

        usernameTag = (TextView) findViewById(R.id.usernameTag);
        passwordTag = (TextView) findViewById(R.id.passwordTag);

        usernameStr=username.getText().toString();
        passwordStr=password.getText().toString();

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                usernameTag.setTextColor(Color.BLACK);
                passwordTag.setTextColor(Color.BLACK);
            }
        });
    }

    public void LoginOnClick(View v){

        GetInputUserInfo();

        if (usernameStr.equals("") || passwordStr.equals("")){
            Toast.makeText(this,"Kullanıcı Adı ve Parola boş bırakılamaz! ",Toast.LENGTH_LONG).show();
            usernameTag.setTextColor(Color.RED);
            passwordTag.setTextColor(Color.RED);
        }
        else{
            String result = db.UserLogin(usernameStr,passwordStr);

            if(result.equals(usernameStr)){
                Toast.makeText(this,"Giriş bilgileri doğru!",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, ToDoActivity.class);
                intent.putExtra("username",usernameStr);
                startActivity(intent);

            }
            else{
                Toast.makeText(this,"Hatalı Kullanıcı Adı/Parola!",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void RegisterOnClick(View v){

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);

    }

}
