package com.example.expenses;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

class Databasehelper extends SQLiteOpenHelper {
    SQLiteDatabase mydb;

    public Databasehelper(Context context) {
        super(context, "expenseinfo", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table if not exists expenseinfo(srno integer primary key AUTOINCREMENT  ,"+"type varchar,"+"itemid varchar," +"amount integer,"+"category varchar,"
                + "pay varchar,"+"date date,"+"notes varchar)";
        try {
            db.execSQL(query);
        }
        catch (Exception e){
            Log.e("databaseproject", "Error creating table due to :" +e.getMessage(),e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public long insertvalues(String type , String itemid, Integer amount, String category , String pay, String notes){
        ContentValues myvalues = new ContentValues();
        if(type == "Expense"){
            amount = -amount;
        }
        myvalues.put("type" , type );
        myvalues.put("itemid",itemid);
        myvalues.put("amount",amount);
        myvalues.put("category",category);
        myvalues.put("pay",pay);
        myvalues.put("notes",notes);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        myvalues.put("date",dateFormat.format(date));
        return mydb.insert("expenseinfo",null,myvalues);
    }
//    public ArrayList<HashMap<String,String>> getdata(){
//
//        ArrayList<HashMap<String,String>> itemlist = new ArrayList<>();
//        String query = "Select * from expenseinfo";
//        Cursor cursor = mydb.rawQuery(query,null);
//        while (cursor.moveToNext()){
//            HashMap<String,String> item = new HashMap<>();
//            item.put("type",cursor.getString(cursor.getColumnIndex("type")));
//            item.put("itemid",cursor.getString(cursor.getColumnIndex("itemid")));
//            item.put("amount",cursor.getString(cursor.getColumnIndex("amount")));
//            item.put("category",cursor.getString(cursor.getColumnIndex("category")));
//            item.put("pay",cursor.getString(cursor.getColumnIndex("pay")));
//            item.put("notes",cursor.getString(cursor.getColumnIndex("notes")));
//            item.put("date",cursor.getString(cursor.getColumnIndex("date")));
//            itemlist.add(item);
//        }
//        return itemlist;
//    }

    public Integer fetchbalance(){

        Integer s = null;
        Cursor cursor = mydb.rawQuery("SELECT SUM(amount) as Total FROM expenseinfo", null);

        if(cursor.moveToFirst()) {
            s = cursor.getInt(0);
        }
        return s;
    }

    public ArrayList<String> fetchsrdatedesc(){

        ArrayList<String> string = new ArrayList<>();
        Cursor cursor = mydb.query("expenseinfo",null,null,null,null,null,"date desc");
        while (cursor.moveToNext()){
            string.add(cursor.getString(cursor.getColumnIndex("srno")));
        }

        return string;
    }
    public ArrayList<String> fetchsramountdesc(){

        ArrayList<String> string = new ArrayList<>();
        Cursor cursor = mydb.query("expenseinfo",null,null,null,null,null,"amount desc");
        while (cursor.moveToNext()){
            string.add(cursor.getString(cursor.getColumnIndex("srno")));
        }

        return string;
    }
    public ArrayList<String> fetchsramountasc(){

        ArrayList<String> string = new ArrayList<>();
        Cursor cursor = mydb.query("expenseinfo",null,null,null,null,null,"amount asc");
        while (cursor.moveToNext()){
            string.add(cursor.getString(cursor.getColumnIndex("srno")));
        }

        return string;
    }

    public ArrayList<String> fetchsrdateasc(){

        ArrayList<String> string = new ArrayList<>();
        Cursor cursor = mydb.query("expenseinfo",null,null,null,null,null,"date asc");
        while (cursor.moveToNext()){
            string.add(cursor.getString(cursor.getColumnIndex("srno")));
        }

        return string;
    }


    public ArrayList<String> fetchids2(){

        ArrayList<String> string = new ArrayList<>();
        Integer a = 0;
        Cursor cursor = mydb.rawQuery("SELECT * FROM expenseinfo", null);
        while (cursor.moveToNext() && a<3){
            a++;
            string.add(cursor.getString(cursor.getColumnIndex("itemid")));
        }

        return string;
    }

    public boolean deleteitem(String srno1){

        return mydb.delete("expenseinfo","srno = ?" , new String[]{srno1})>0;
    }

    public ArrayList<String> fetchrecords(String srno){
        Cursor myresult = mydb.rawQuery("select * from expenseinfo where srno=?", new String[]{srno});
        ArrayList<String> expenserecords = new ArrayList<>();
        if(myresult.moveToNext()){
            expenserecords.add(myresult.getString(myresult.getColumnIndex("type")));
            expenserecords.add(myresult.getString(myresult.getColumnIndex("itemid")));
            expenserecords.add(myresult.getString(myresult.getColumnIndex("amount")));
            expenserecords.add(myresult.getString(myresult.getColumnIndex("category")));
            expenserecords.add(myresult.getString(myresult.getColumnIndex("pay")));
            expenserecords.add(myresult.getString(myresult.getColumnIndex("notes")));
        }
        else{
            expenserecords.clear();
        }
        return expenserecords;
    }

    public long updatevalues(String srno , String type ,String itemid, Integer amount, String category, String pay, String notes){

        ContentValues myvalues1 = new ContentValues();
        if(type == "Expense"){
            amount = -amount;
        }
        myvalues1.put("type" , type );
        myvalues1.put("itemid",itemid);
        myvalues1.put("amount",amount);
        myvalues1.put("category",category);
        myvalues1.put("pay",pay);
        myvalues1.put("notes",notes);
        return mydb.update("expenseinfo",myvalues1,"srno=?",new String[]{srno});

    }

    public ArrayList<HashMap<String,String>> sortdateasc(){

        ArrayList<HashMap<String,String>> itemlist = new ArrayList<>();
        Cursor cursor = mydb.query("expenseinfo",null,null,null,null,null,"datetime(date) asc");
        while (cursor.moveToNext()){
            HashMap<String,String> item = new HashMap<>();
            item.put("type",cursor.getString(cursor.getColumnIndex("type")));
            item.put("itemid",cursor.getString(cursor.getColumnIndex("itemid")));
            item.put("amount",cursor.getString(cursor.getColumnIndex("amount")));
            item.put("category",cursor.getString(cursor.getColumnIndex("category")));
            item.put("pay",cursor.getString(cursor.getColumnIndex("pay")));
            item.put("notes",cursor.getString(cursor.getColumnIndex("notes")));
            item.put("date",cursor.getString(cursor.getColumnIndex("date")));
            itemlist.add(item);
        }
        return itemlist;
    }

    public ArrayList<HashMap<String,String>> sortdatedesc2(){

        ArrayList<HashMap<String,String>> itemlist = new ArrayList<>();
        Integer a = 0;
        Cursor cursor = mydb.query("expenseinfo",null,null,null,null,null,"date desc");
        while (cursor.moveToNext() && a<3){
            a++;
            HashMap<String,String> item = new HashMap<>();
            item.put("type",cursor.getString(cursor.getColumnIndex("type")));
            item.put("itemid",cursor.getString(cursor.getColumnIndex("itemid")));
            item.put("amount",cursor.getString(cursor.getColumnIndex("amount")));
            item.put("category",cursor.getString(cursor.getColumnIndex("category")));
            item.put("pay",cursor.getString(cursor.getColumnIndex("pay")));
            item.put("notes",cursor.getString(cursor.getColumnIndex("notes")));
            item.put("date",cursor.getString(cursor.getColumnIndex("date")));
            itemlist.add(item);
        }
        return itemlist;
    }

    public ArrayList<HashMap<String,String>> sortdatedesc(){

        ArrayList<HashMap<String,String>> itemlist = new ArrayList<>();
        Cursor cursor = mydb.query("expenseinfo",null,null,null,null,null,"date desc");
        while (cursor.moveToNext()){
            HashMap<String,String> item = new HashMap<>();
            item.put("type",cursor.getString(cursor.getColumnIndex("type")));
            item.put("itemid",cursor.getString(cursor.getColumnIndex("itemid")));
            item.put("amount",cursor.getString(cursor.getColumnIndex("amount")));
            item.put("category",cursor.getString(cursor.getColumnIndex("category")));
            item.put("pay",cursor.getString(cursor.getColumnIndex("pay")));
            item.put("notes",cursor.getString(cursor.getColumnIndex("notes")));
            item.put("date",cursor.getString(cursor.getColumnIndex("date")));
            itemlist.add(item);
        }
        return itemlist;
    }

    public ArrayList<HashMap<String,String>> sortamtdesc(){

        ArrayList<HashMap<String,String>> itemlist = new ArrayList<>();
        Cursor cursor = mydb.query("expenseinfo",null,null,null,null,null,"amount desc");
        while (cursor.moveToNext()){
            HashMap<String,String> item = new HashMap<>();
            item.put("type",cursor.getString(cursor.getColumnIndex("type")));
            item.put("itemid",cursor.getString(cursor.getColumnIndex("itemid")));
            item.put("amount",cursor.getString(cursor.getColumnIndex("amount")));
            item.put("category",cursor.getString(cursor.getColumnIndex("category")));
            item.put("pay",cursor.getString(cursor.getColumnIndex("pay")));
            item.put("notes",cursor.getString(cursor.getColumnIndex("notes")));
            item.put("date",cursor.getString(cursor.getColumnIndex("date")));
            itemlist.add(item);
        }
        return itemlist;
    }

    public ArrayList<HashMap<String,String>> sortamtasc(){

        ArrayList<HashMap<String,String>> itemlist = new ArrayList<>();
        Cursor cursor = mydb.query("expenseinfo",null,null,null,null,null,"amount asc");
        while (cursor.moveToNext()){
            HashMap<String,String> item = new HashMap<>();
            item.put("type",cursor.getString(cursor.getColumnIndex("type")));
            item.put("itemid",cursor.getString(cursor.getColumnIndex("itemid")));
            item.put("amount",cursor.getString(cursor.getColumnIndex("amount")));
            item.put("category",cursor.getString(cursor.getColumnIndex("category")));
            item.put("pay",cursor.getString(cursor.getColumnIndex("pay")));
            item.put("notes",cursor.getString(cursor.getColumnIndex("notes")));
            item.put("date",cursor.getString(cursor.getColumnIndex("date")));
            itemlist.add(item);
        }
        return itemlist;
    }

    public void mydbopen(){
        mydb = this.getReadableDatabase();
    }
    public void mydbclose(){
        mydb.close();
    }

}
