package com.example.expenses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Expeses extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout viewall;
    ArrayList<HashMap<String,String>> list ;
    HashMap<String,String> map1;
    ImageView image;
    TextView currebtbalance;
    MyAdapter myAdapter;
    ListView listview;
    ArrayList<String> itemid1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview = findViewById(R.id.listview);
        setTitle("Money Manager");
        image = findViewById(R.id.imageView);
        image.setVisibility(View.GONE);
        currebtbalance = findViewById(R.id.currentbalance);
        Databasehelper obj = new Databasehelper(this);
        obj.mydbopen();
        list = obj.sortdatedesc2();
        itemid1 = obj.fetchids2();
        Integer a = obj.fetchbalance();
        obj.mydbclose();
        if(list.isEmpty()){
            listview.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);
        }
        else {
            image.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            myAdapter = new MyAdapter(this, android.R.layout.simple_list_item_1, itemid1);
            listview.setAdapter(myAdapter);
        }
        currebtbalance.setText(String.valueOf(a));



        viewall = findViewById(R.id.viewall);
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Expeses.this, listview.class));
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.plus32);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Expeses.this , newtrandiag.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    class MyAdapter extends ArrayAdapter<String> {
        MyAdapter(Context mycontext, int layout1, ArrayList<String> a ) {
            super(mycontext, layout1, a );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            map1 = list.get(position);

            LayoutInflater myinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myview = myinflater.inflate(R.layout.custom, parent, false);

            TextView itemid, amount, date;
            ImageView imageView;
            itemid = (TextView) myview.findViewById(R.id.itemid);
            amount = (TextView) myview.findViewById(R.id.amount);
            date = (TextView) myview.findViewById(R.id.date);
            imageView =  myview.findViewById(R.id.image);

            itemid.setText(map1.get("itemid"));
            String amt = map1.get("amount");
            Integer amtint = Integer.parseInt(amt);
            if (amtint < 0) {
                amtint = -amtint;
            }
            String a = map1.get("type");
            if (a.equals("Expense")) {
                amount.setTextColor(getResources().getColor(R.color.red,null));
            } else {
                amount.setTextColor(getResources().getColor(R.color.green,null));
            }
            amount.setText(String.valueOf(amtint));

            String category1 =  map1.get("category");
            if (category1.equals("Automobile")){
                imageView.setImageResource(R.drawable.automobile);
            }
            else if (category1.equals("Entertainment")){
                imageView.setImageResource(R.drawable.popcorn);
            }
            else if (category1.equals("Food")){
                imageView.setImageResource(R.drawable.burger);
            }
            else if (category1.equals("Travel")){
                imageView.setImageResource(R.drawable.mapsandflags);
            }
            else if (category1.equals("Health Care")){
                imageView.setImageResource(R.drawable.heartbeat);
            }
            else if (category1.equals("Household")){
                imageView.setImageResource(R.drawable.house);
            }
            else if (category1.equals("Personal")){
                imageView.setImageResource(R.drawable.richman);
            }
            else if (category1.equals("Others")){
                imageView.setImageResource(R.drawable.expensive);
            }
            else {
                imageView.setImageResource(R.drawable.profit);
            }


            date.setText(map1.get("date"));
            return myview;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder mybuilder = new AlertDialog.Builder(Expeses.this);
            mybuilder.setMessage("Do you want to Exit?");
            mybuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Expeses.super.onBackPressed();
                }
            });
            mybuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            mybuilder.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
          if (id == R.id.vlist) {
            startActivity(new Intent(Expeses.this, listview.class));

        } else if (id == R.id.newexp) {
            startActivity(new Intent(Expeses.this,newtrandiag.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

