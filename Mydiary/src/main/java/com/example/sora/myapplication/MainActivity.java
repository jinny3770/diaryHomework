package com.example.sora.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btn;
    TextView textView;
    EditText editText;

    int year, month, day;
    float bSize, tSize, eSize;

    final String sdpath = Environment.getExternalStorageDirectory().getAbsolutePath();
    final File dir = new File(sdpath + "/mydiary");
    String fileName;

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int y, int m, int d) {
            year = y;
            month = m+1;
            day = d;

            textView.setText(year+"년 " + month +"월 " + day + "일");
            fileName = makeFileName(year, month, day);
            readFile();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("My mini diary");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);

        bSize = btn.getTextSize();
        tSize = textView.getTextSize();
        eSize = editText.getTextSize();


        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        init();
        setTextSize(0.5f);
        fileName = makeFileName(year, month, day);


        if(!dir.exists()){
            dir.mkdir();
        }
        else readFile();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFile();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, dateSetListener, year, month-1, day).show();
            }
        });
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
        switch(id) {
            case R.id.reload:
                readFile();
                break;
            case R.id.delete:
                // dialog
                AlertDialog.Builder dlg = new AlertDialog.Builder(this);
                dlg.setMessage(Integer.toString(year)+"년 "+Integer.toString(month)+"월 "+
                Integer.toString(day)+"일 일기를 삭제하시겠습니까?");
                dlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete();
                    }
                });
                dlg.setNegativeButton("No", null);
                dlg.show();
                break;
            case R.id.small:
                setTextSize(0.5f);
                break;
            case R.id.medium:
                setTextSize(0.9f);
                break;
            case R.id.large:
                setTextSize(1.2f);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    String makeFileName(int y, int m, int d) {
        return dir.getAbsolutePath() + "/" +Integer.toString(y)+"_"+Integer.toString(m)+"_"+Integer.toString(d)+".txt";

    }

    void readFile() {
        String diaryStr = null;
        try {
            FileInputStream input = new FileInputStream(fileName);
            byte[] txt = new byte[input.available()];
            input.read(txt);
            input.close();
            diaryStr = (new String(txt)).trim();
            editText.setText(diaryStr);
            btn.setText("Edit");
        }catch (IOException e) {
            init();
        }
    }

    boolean writeFile(){

        FileOutputStream output;

        //File f = new File(dir+"/"+fileName);
        try{
            output = new FileOutputStream(fileName);
            Byte[] txt = new Byte[500];
            String str = editText.getText().toString();
            output.write(str.getBytes());
            output.close();
            Toast.makeText(this, year+"_"+month+"_"+day+".txt is saved!", Toast.LENGTH_SHORT).show();
            btn.setText("Edit");
            return true;

        }catch (IOException e) {
            Toast.makeText(this, "Write Error", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    void delete() {

        File file = new File(fileName);

        if(file.exists()) {
            file.delete();
            init();
        }
        else
            Toast.makeText(this, "Diary is not exist.", Toast.LENGTH_SHORT).show();
    }

    void init() {
        btn.setText("Save");
        editText.setText(null);
        editText.setHint("no Diary");
        textView.setText(Integer.toString(year) + "년 " + Integer.toString(month) + "월 " +
        Integer.toString(day) + "일");
    }

    void setTextSize(float size) {
        btn.setTextSize(bSize*size);
        editText.setTextSize(eSize*size);
        textView.setTextSize(tSize*size);
    }

}
