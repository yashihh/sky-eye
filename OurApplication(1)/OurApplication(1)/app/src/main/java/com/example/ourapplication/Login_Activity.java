package com.example.ourapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Math;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Login_Activity extends AppCompatActivity {

    public Connection con;
    double radius;  //軸距
    double result=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        final String[] car = {"RAV4", "Corolla Cross", "C-HR", "Prado", "Corolla Altis","Camry",
                "Vios","Yaris","Prius PHV","Corolla Sport","GR Yaris"};
        ArrayAdapter<String> lunchList = new ArrayAdapter<>(Login_Activity.this,android.R.layout.simple_spinner_dropdown_item,car);
        spinner.setAdapter(lunchList);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Login_Activity.this, "您選擇了:" + car[position], Toast.LENGTH_SHORT).show();

                if(parent.getSelectedItemPosition()==0){
                    radius=2.690;
                }
                else if(parent.getSelectedItemPosition()==1||parent.getSelectedItemPosition()==2||parent.getSelectedItemPosition()==9){
                    radius=2.640;
                }
                else if(parent.getSelectedItemPosition()==3){
                    radius=2.790;
                }
                else if(parent.getSelectedItemPosition()==4||parent.getSelectedItemPosition()==8){
                    radius=2.700;
                }
                else if(parent.getSelectedItemPosition()==5){
                    radius=2.850;
                }
                else if(parent.getSelectedItemPosition()==6||parent.getSelectedItemPosition()==7){
                    radius=2.550;
                }
                else if(parent.getSelectedItemPosition()==10){
                    radius=2.560;
                }

                result=radius*Math.tan(30/2);//公式
                result=Math.abs(result);

                Toast.makeText(Login_Activity.this, "你的軸距為:" + result+" m", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button btn_to_B = (Button) findViewById(R.id.button2);

        btn_to_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    con = connectionclass();
                    if(con == null)
                    {
                        Toast.makeText(Login_Activity.this, "Check Your Internet Access!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String query = "UPDATE Car_info SET dif_of_radius =" + radius + "WHERE ID = 1";
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);
                        con.close();
                    }
                }
                catch(Exception e)
                {
                    String z = e.getMessage();
                    Log.d("sql error", z);
                }

                Intent intent = new Intent();
                intent.setClass(Login_Activity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("NewApi")
    public Connection connectionclass()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = "jdbc:jtds:sqlserver://azuretest0814.database.windows.net:1433;databasename=AzureTest0814;user=Justinlin;password=Azuretest0814;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;ssl=reques;";
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch(SQLException se)
        {
            Log.e("error here 1:", se.getMessage());
        }
        catch(ClassNotFoundException e)
        {
            Log.e("error here 2:", e.getMessage());
        }
        catch(Exception e)
        {
            Log.e("error here 3:", e.getMessage());
        }
        return connection;
    }
}