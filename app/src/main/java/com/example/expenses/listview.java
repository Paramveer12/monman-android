package com.example.expenses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class listview extends AppCompatActivity {


    ListView listView;
    MyAdapter myAdapter;
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> map1;
    ArrayList<String> srno ;
    String notes1;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        listView = findViewById(R.id.listview);
        Databasehelper obj = new Databasehelper(this);
        obj.mydbopen();
        srno = obj.fetchsrdatedesc();
        image = findViewById(R.id.imageView2);
        list = obj.sortdatedesc();
        obj.mydbclose();
        if(list.isEmpty()){
            listView.setVisibility(View.INVISIBLE);
            image.setVisibility(View.VISIBLE);
        }
        else {
            image.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            myAdapter = new MyAdapter(this, android.R.layout.simple_list_item_1, srno);
            listView.setAdapter(myAdapter);
        }
        registerForContextMenu(listView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Databasehelper obj = new Databasehelper(this);

        if(id == R.id.dateasc){
            obj.mydbopen();
            list = obj.sortdateasc();
            srno = obj.fetchsrdateasc();
            obj.mydbclose();
            myAdapter.notifyDataSetChanged();

        }
        else if(id == R.id.datedesc){
            obj.mydbopen();
            list = obj.sortdatedesc();
            srno = obj.fetchsrdatedesc();
            obj.mydbclose();
            myAdapter.notifyDataSetChanged();

        }

        else if(id == R.id.amtasc){
            obj.mydbopen();
            list =obj.sortamtasc();
            srno = obj.fetchsramountasc();
            obj.mydbclose();
            myAdapter.notifyDataSetChanged();

        }
        else if(id == R.id.amtdesc){
            obj.mydbopen();
            list =obj.sortamtdesc();
            srno = obj.fetchsramountdesc();
            obj.mydbclose();
            myAdapter.notifyDataSetChanged();
        }
        else if (id == android.R.id.home){
            startActivity(new Intent(listview.this,Expeses.class));
        }


        return super.onOptionsItemSelected(item);
    }


    class MyAdapter extends ArrayAdapter<String> {
        MyAdapter(Context mycontext, int layout1, ArrayList<String> a ) {
            super(mycontext, layout1, a );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater myinflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View myview = myinflater.inflate(R.layout.custom, parent, false);

            TextView itemid, amount, category, pay, date;
            ImageView imageView;
            itemid = (TextView) myview.findViewById(R.id.itemid);
            amount = (TextView) myview.findViewById(R.id.amount);
            date = (TextView) myview.findViewById(R.id.date);
            imageView =  myview.findViewById(R.id.image);

            Log.d("Stage 5",String.valueOf(srno.size()));
            Log.d("Stage 5 list" , String.valueOf(list.size()));

            map1 = list.get(position);

            //itemid.setText(map1.get("itemid"));
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
            if (category1.contentEquals("Automobile")){
                imageView.setImageResource(R.drawable.automobile);
            }
            else if (category1.contentEquals("Entertainment")){
                imageView.setImageResource(R.drawable.popcorn);
            }
            else if (category1.contentEquals("Food")){
                imageView.setImageResource(R.drawable.burger);
            }
            else if (category1.contentEquals("Travel")){
                imageView.setImageResource(R.drawable.mapsandflags);
            }
            else if (category1.contentEquals("Health Care")){
                imageView.setImageResource(R.drawable.heartbeat);
            }
            else if (category1.contentEquals("Household")){
                imageView.setImageResource(R.drawable.house);
            }
            else if (category1.contentEquals("Personal")){
                imageView.setImageResource(R.drawable.richman);
            }
            else if (category1.contentEquals("Others")){
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }


    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final int index = info.position;


        if (item.getItemId() == R.id.edit) {
            Intent i = new Intent(listview.this,editactiviry.class);
            i.putExtra("srno",srno.get(index));
            startActivity(i);
            return true;
        }
        else if (item.getItemId() == R.id.delete) {


            AlertDialog.Builder mybuilder = new AlertDialog.Builder(listview.this);
            mybuilder.setMessage("Do you want to delete?");
            mybuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Databasehelper obj = new Databasehelper(listview.this);
                    obj.mydbopen();
                    Log.d("Stage 1",String.valueOf(srno.size()));
                    Log.d("Stage 1 list" , String.valueOf(list.size()));
                    String s = srno.get(index);
                    boolean rows = obj.deleteitem(s);
                    Log.d("Stage 2",String.valueOf(srno.size()));
                    Log.d("Stage 2 list" , String.valueOf(list.size()));
                    if (rows ) {
                        Toast.makeText(listview.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("Stage 3 list" , String.valueOf(list.size()));
                    Log.d("Stage 3",String.valueOf(srno.size()));
                    list = obj.sortdatedesc();
                    srno.remove(index);
                    srno = obj.fetchsrdatedesc();
                    Log.d("Stage 4",String.valueOf(srno.size()));
                    Log.d("Stage 4 list" , String.valueOf(list.size()));
                    obj.mydbclose();
                    if(list.isEmpty()){
                        listView.setVisibility(View.INVISIBLE);
                        image.setVisibility(View.VISIBLE);
                    }
                    else {
                        image.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        myAdapter.notifyDataSetChanged();
                    }


                }

                });
            mybuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            mybuilder.show();
            return true;
            }
        else if(item.getItemId()==R.id.notes){
            AlertDialog.Builder mybuilder = new AlertDialog.Builder(listview.this);
           map1=  list.get(index);
           notes1 = map1.get("notes");
            mybuilder.setMessage(notes1);
            mybuilder.show();
            return true;
        }
        else if(item.getItemId()==R.id.pay){
            AlertDialog.Builder mybuilder = new AlertDialog.Builder(listview.this);
            map1=  list.get(index);
            notes1 = map1.get("pay");
            mybuilder.setMessage(notes1);
            mybuilder.show();
            return true;
        }
        return true;
            }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        startActivity(new Intent(listview.this, Expeses.class));
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(listview.this, Expeses.class));
    }
    }
