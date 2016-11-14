package com.example.jacobevans.mymobilepopquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private TextView tv;
    private Button adminLoginButton;
    private EditText enteredAdmminNameEditTextobj, enteredAdminPasswordEditTextobj;
    private SQLiteDatabase db=null;
    private String enteredAdminNameString, enteredAdminPasswordString,namestr;
    private Intent i,in;
    private Login_Preference l;
    private static final String dbname="MyMobilePopQuiz.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        l=new Login_Preference(getApplicationContext());
        enteredAdmminNameEditTextobj =(EditText)findViewById(R.id.editText);
        enteredAdminPasswordEditTextobj =(EditText)findViewById(R.id.editText2);
        tv=(TextView)findViewById(R.id.textView3);

        /*
                    activity_login.xml
                    android:text="New register?Sign up."
                    android:id="@+id/textView3"
        */
        adminLoginButton =(Button)findViewById(R.id.button);
        checkSavedPreferences();
        db=openOrCreateDatabase(dbname, Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS user_table(USERNAME VARCHAR PRIMARY KEY,NAME VARCHAR," +
                "PASSWORD VARCHAR,PREVBEST INTEGER,AVG REAL,COUNT INTEGER);");

        /*

        PRIMARY KEY
            |
            |
            |
            V                                       user_table
         __________________________________________________________
        |USERNAME | NAME     | PASSWORD | PREVBEST | AVG  | COUNT  |
        |VARCHAR  | VARCHAR  | VARCHAR  | INTEGER  | REAL | INTEGER|
        |----------------------------------------------------------|
        |userName | realName | ******** | score    | avg  | count  |
        |----------------------------------------------------------|
        |         |          |          |          |      |        |
        |----------------------------------------------------------|
        |         |          |          |          |      |        |
        |----------------------------------------------------------|
        |         |          |          |          |      |        |
        |----------------------------------------------------------|
        |         |          |          |          |      |        |
        |----------------------------------------------------------|
        |         |          |          |          |      |        |
        |----------------------------------------------------------|
        |         |          |          |          |      |        |
        |----------------------------------------------------------|
        |         |          |          |          |      |        |
        |----------------------------------------------------------|
        |         |          |          |          |      |        |
        |----------------------------------------------------------|

        */
        db.execSQL("CREATE TABLE IF NOT EXISTS admin_table(ADMINNAME VARCHAR PRIMARY KEY,NAME VARCHAR,PASSWORD VARCHAR);");

        /*
                            admin_table
         _______________________________
        |ADMINNAME | NAME    | PASSWORD |
        |VARCHAR   | VARCHAR | VARCHAR  |
        |-------------------------------|
        |email    | name     | *******  |
        |-------------------------------|
        |          |         |          |
        |-------------------------------|
        |          |         |          |
        |-------------------------------|
        |          |         |          |
        |-------------------------------|

        */

        try {
            Cursor c = db.rawQuery("SELECT * FROM admin_table",null);
            if ( c==null || c.getCount() == 0) {
                db.execSQL("INSERT INTO admin_table VALUES('"+"admin"+"','"+"enteredAdmminNameEditTextobj"+"','"+"password"+"');");
                db.execSQL("INSERT INTO admin_table VALUES('"+"jakefromatl@gmail.com"+"','"+"Jacob Evans"+"','"+"12345"+"');");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        adminLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredAdminNameString = enteredAdmminNameEditTextobj.getText().toString();
                enteredAdminPasswordString = enteredAdminPasswordEditTextobj.getText().toString();
                int f=0;
                if (!enteredAdminNameString.equals("") && !enteredAdminPasswordString.equals("")) {
                    try {
                        Cursor c = db.rawQuery("SELECT * FROM admin_table WHERE ADMINNAME='"
                                + enteredAdminNameString + "' AND PASSWORD='" + enteredAdminPasswordString + "'", null);
                        if (c != null && c.getCount() != 0) {
                            if (c.moveToFirst()){
                                do {
                                    namestr = c.getString(c.getColumnIndex("NAME"));
                                } while (c.moveToNext());
                            }
                            Toast.makeText(getApplicationContext(),"Welcome! "+namestr,Toast.LENGTH_LONG).show();
                            Intent in = new Intent(getApplicationContext(), AdminActivity.class);
                            f=1;
                            l.setString("NAME",namestr);
                            l.setbool("Loggedin",true);
                            l.setbool("Admin",true);
                            l.setString("Username", enteredAdminNameString);
                            startActivity(in);
                        }
                        c = db.rawQuery("SELECT * FROM user_table WHERE USERNAME='" +
                                enteredAdminNameString + "' AND PASSWORD='" + enteredAdminPasswordString + "'", null);
                        if (c != null && c.getCount() != 0) {
                            if (c.moveToFirst()){
                                do {
                                    namestr = c.getString(c.getColumnIndex("USERNAME"));

                                } while (c.moveToNext());
                            }
                            Intent in = new Intent(getApplicationContext(), QuizDetails.class);
                            f=1;
                            l.setString("NAME",namestr);
                            l.setbool("Loggedin",true);
                            l.setbool("Admin",false);
                            l.setString("Username", enteredAdminNameString);
                            startActivity(in);
                        }
                        if(f==0)
                            Toast.makeText(getApplicationContext(),"Invalid Username or password",Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Username or Password cannot be blank!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void text_Select(View v){
        in=new Intent(getApplicationContext(),SignUp.class);
        startActivity(in);
    }
    public void checkSavedPreferences(){
        if(l.getbool("Loggedin")){
            if(l.getbool("Admin")){
                i=new Intent(getApplicationContext(),AdminActivity.class);
                startActivity(i);
            }
            else{
                i=new Intent(getApplicationContext(),QuizDetails.class);
                startActivity(i);
            }
        }
    }
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit!")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }
}

