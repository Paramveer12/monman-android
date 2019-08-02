package com.example.expenses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class editactiviry extends AppCompatActivity {

    RadioButton incbtn,expbtn;
    RadioGroup radioGroup;
    EditText itembox,amountbox,paybox,notes;
    TextView pbox;
    Spinner catbox;
    ArrayList<String> categories;
    String srno;
    Button update;
    Integer a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editactiviry);
        srno = getIntent().getExtras().getString("srno");
        incbtn = findViewById(R.id.incbtn);
        expbtn = findViewById(R.id.expbtn);
        itembox = findViewById(R.id.itembox);
        amountbox = findViewById(R.id.amountbox);
        radioGroup = findViewById(R.id.radiogroup);
        setTitle("Edit Transaction");
        radioGroup.check(R.id.expbtn);
        paybox = findViewById(R.id.paybox);
        pbox = findViewById(R.id.pbox);
        notes=findViewById(R.id.notes);
        catbox = findViewById(R.id.catbox);
        categories = new ArrayList<>();
        final ArrayAdapter<String> arrayadp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,categories);
        catbox.setAdapter(arrayadp);
        incbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pbox.setText("Payer");
                categories.clear();
                categories.add("Select a category");
                categories.add("Salary");
                categories.add("Bonus");
                categories.add("Savings");
                catbox.setAdapter(arrayadp);
            }
        });
        expbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbox.setText("Payee");
                categories.clear();
                categories.add("Select a category");
                categories.add("Automobile");
                categories.add("Entertainment");
                categories.add("Food");
                categories.add("Health Care");
                categories.add("Household");
                categories.add("Personal");
                categories.add("Travel");
                categories.add("Others");
                catbox.setAdapter(arrayadp);
            }
        });
        Databasehelper obj = new Databasehelper(this);
        obj.mydbopen();
        ArrayList<String> profilerecord = obj.fetchrecords(srno);

        a = Integer.parseInt(profilerecord.get(2));
        if(profilerecord.get(0).equals("Expense")){
            a = -a;
            expbtn.setChecked(true);
            pbox.setText("Payee");
            categories.clear();
            categories.add("Select a category");
            categories.add("Automobile");
            categories.add("Entertainment");
            categories.add("Food");
            categories.add("Health Care");
            categories.add("Household");
            categories.add("Personal");
            categories.add("Travel");
            categories.add("Others");
            catbox.setAdapter(arrayadp);
        }
        else if(profilerecord.get(0).equals("Income")){
            pbox.setText("Payer");
            incbtn.setChecked(true);
            categories.clear();
            categories.add("Select a category");
            categories.add("Salary");
            categories.add("Bonus");
            categories.add("Savings");
            catbox.setAdapter(arrayadp);
        }
        itembox.setText(profilerecord.get(1));
        amountbox.setText(a.toString());
        int position = categories.indexOf(profilerecord.get(3));
        catbox.setSelection(position);
        paybox.setText(profilerecord.get(4));
        notes.setText(profilerecord.get(5));
        obj.mydbclose();

        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean catcheck = catbox.getSelectedItem().toString().equals("Select a category");
                if(itembox.getText().toString().trim().length()>0 && notes.getText().toString().trim().length()>0
                        && amountbox.getText().toString().trim().length()>0 && paybox.getText().toString().trim().length()>0
                        && (incbtn.isChecked() || expbtn.isChecked()) &&  catcheck == false) {
                    Databasehelper obj = new Databasehelper(editactiviry.this);
                    obj.mydbopen();
                    long rowid = (long) obj.updatevalues(srno ,(expbtn.isChecked()) ? "Expense" : "Income" , itembox.getText().toString(),
                            Integer.parseInt(amountbox.getText().toString()),catbox.getSelectedItem().toString(),
                            paybox.getText().toString(), notes.getText().toString());
                    obj.mydbclose();
                    if (rowid != -1) {
                        Toast.makeText(editactiviry.this, "Record Saved", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(editactiviry.this, listview.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(editactiviry.this, "Unable to Create Account", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(editactiviry.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                }
        });
    }
}
