package com.example.sora.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    myDBHelper helper;
    EditText name, number, editName, editNumber;
    Button b1, b2, b3, b4, b5;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("가수 그룹 관리 DB");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        helper = new myDBHelper(this);

        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);
        editName = (EditText) findViewById(R.id.editName);
        editNumber = (EditText) findViewById(R.id.editNumber);

        b1 = (Button) findViewById(R.id.init);
        b2 = (Button) findViewById(R.id.input);
        b3 = (Button) findViewById(R.id.edit);
        b4 = (Button) findViewById(R.id.delete);
        b5 = (Button) findViewById(R.id.query);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);

        b5.performClick();
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.init :
                sqlDB = helper.getWritableDatabase();
                helper.onUpgrade(sqlDB, 1, 2);
                sqlDB.close();
                break;

            case R.id.input:
                sqlDB = helper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO groupTBL VALUES ('" + name.getText().toString() + "' , " +
                        number.getText().toString() + ");");
                sqlDB.close();

                Toast.makeText(getApplicationContext(), number.getText().toString() + " 입력 완료", Toast.LENGTH_SHORT).show();
                b5.performClick();
                break;

            case R.id.edit:
                sqlDB = helper.getWritableDatabase();
                sqlDB.execSQL("UPDATE groupTBL SET gNumber = " + number.getText().toString() +
                        " WHERE gName = '" + name.getText().toString() + "';");
                b5.performClick();
                break;

            case R.id.delete:
                sqlDB = helper.getWritableDatabase();
                sqlDB.execSQL("DELETE FROM groupTBL WHERE gName = '" + name.getText().toString() + "';");
                b5.performClick();
                break;

            case R.id.query:
                sqlDB = helper.getReadableDatabase();

                Cursor cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);

                String strNames = "그룹 이름" + "\r\n" + "----------" + "\r\n";
                String strNumbers = "인원" + "\r\n" + "------" + "\r\n";

                while(cursor.moveToNext()) {
                    strNames += cursor.getString(0) + "\r\n";
                    strNumbers += cursor.getString(1) + "\r\n";
                }

                editName.setText(strNames);
                editNumber.setText(strNumbers);

                cursor.close();
                sqlDB.close();
                break;

        }
    }

    public class myDBHelper extends SQLiteOpenHelper {

        public myDBHelper(Context context) {
            // 문자열 = 생성될 DB의 파일명
            super(context, "groupDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE groupTBL ( gName CHAR(20) PRIMARY KEY, gNumber INTEGER);");
        }

        public void onUpgrade(SQLiteDatabase db, int oldvVertsion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
