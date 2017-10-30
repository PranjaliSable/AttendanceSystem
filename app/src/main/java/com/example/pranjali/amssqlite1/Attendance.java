package com.example.pranjali.amssqlite1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Attendance extends AppCompatActivity {
    private ArrayAdapter<Record> listAdapter ;
    TextView mdate;
    Button dateChange,toggleA,saveA;
    ListView att_list;
    boolean toggleEmulator=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mdate=(TextView)findViewById(R.id.date);
        dateChange=(Button)findViewById(R.id.cDate);
        toggleA=(Button) findViewById(R.id.att_toggle);
        saveA=(Button)findViewById(R.id.att_save);
        att_list=(ListView) findViewById(R.id.attendance);

        dateChange.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.N)
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

              //
                DatePickerDialog datePickerDialog = new DatePickerDialog(Attendance.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                mdate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        Record[] record=new Record[10];
        for(int i=0;i<10;i++)
            record[i]=new Record(""+(i+1));
        final ArrayList<Record> recordList = new ArrayList<Record>();
        recordList.addAll( Arrays.asList(record) );

        listAdapter=new RecordArrayAdapter(getApplicationContext(), recordList);

        att_list.setAdapter(listAdapter);


        toggleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleEmulator) {
                    for (int i = 0; i < listAdapter.getCount(); i++) {
                        listAdapter.getItem(i).setChecked(false);
                    }
                    att_list.setAdapter(listAdapter);
                    toggleA.setText("CHECK ALL");
                    Snackbar.make(v, "All student marked absent", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                else{
                    for (int i = 0; i < listAdapter.getCount(); i++) {
                        listAdapter.getItem(i).setChecked(true);
                    }
                    att_list.setAdapter(listAdapter);
                    toggleA.setText("UNCHECK ALL");
                    Snackbar.make(v, "All student marked present", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                toggleEmulator=!toggleEmulator;
            }
        });

        saveA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBhelper databaseHelper=new DBhelper(Attendance.this);

                for (int i = 0; i < listAdapter.getCount(); i++) {
                    String roll=listAdapter.getItem(i).getRollNo();
                    Boolean att=listAdapter.getItem(i).isChecked();

                    String status;
                    if(att)
                        status="P";
                    else
                        status="A";
                    databaseHelper.insertAttendance(roll,mdate.getText().toString(),status);
                }

                Cursor c=databaseHelper.getAttendance();
                for(int i=0;i<c.getCount();i++){
                    Log.i("Attendance","id "+c.getString(0)+" roll "+c.getString(1)+" date "+c.getString(2)+" attendance "+c.getString(3));
                    c.moveToNext();
                }

            }
        });

    }

    /** Holds child views for one row. */
    private static class RecordViewHolder {
        private CheckBox mCheckBox ;
        private EditText mRollNo;
        public RecordViewHolder() {}
        public RecordViewHolder( EditText mRollNo, CheckBox mCheckBox ) {
            this.mCheckBox = mCheckBox ;

            this.mRollNo = mRollNo ;
        }
        public CheckBox getmCheckBox() {
            return mCheckBox;
        }
        public void setmCheckBox(CheckBox checkBox) {
            this.mCheckBox = checkBox;
        }

        public void setmRollNo(EditText mRollNo){
            this.mRollNo=mRollNo;
        }
        public EditText getmRollNo(){
            return mRollNo;
        }
    }

    /** Holds student record */
    private static class Record {
        // private String name = "" ;

        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }

        private String rollNo="";
        private boolean checked = false ;
        public Record() {}
        public Record( String rollNo ) {
            // this.name = name ;
            this.rollNo=rollNo;
        }
        public Record( String rollNo, boolean checked ) {
            //  this.name = name ;
            this.rollNo=rollNo;
            this.checked = checked ;
        }
        /*  public String getName() {
              return name;
          }
          public void setName(String name) {
              this.name = name;
          }*/
        public boolean isChecked() {
            return checked;
        }
        public void setChecked(boolean checked) {
            this.checked = checked;
        }
        /*public String toString() {
            return name ;
        }*/
        public void toggleChecked() {
            checked = !checked ;
        }
    }

    public void textUpdate(View view){

        // Is the view now checked?
        CheckBox checkBox=((CheckBox) view);
        boolean checked = checkBox.isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {

            case R.id.checkView:
                if (checked) {
                    checkBox.setText("Present");
                    //attendanceAdapter.modelItems[holder.pos].setValue('y');

                }
                else{
                    checkBox.setText("Absent");
                    //attendanceAdapter.modelItems[holder.pos].setValue('n');
                }
                // attendanceList.setAdapter(attendanceAdapter);

        }

    }
    /** Custom adapter for displaying an array of Planet objects. */
    private static class RecordArrayAdapter extends ArrayAdapter<Record> {

        private LayoutInflater inflater;

        public RecordArrayAdapter(Context context, List<Record> recordList ) {
            super( context, R.layout.row, R.id.attendance, recordList );
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context) ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Planet to display
            Record record = this.getItem( position );

            // The child views in each row.
            CheckBox checkBoxView ;
            EditText rollNoView ;
            //  TextView nameView;

            // Create a new row view
            if ( convertView == null ) {
                convertView = inflater.inflate(R.layout.row, null);

                // Find the child views.
                rollNoView = (EditText) convertView.findViewById( R.id.rollN );
                // nameView = (TextView) convertView.findViewById(R.id.nameView);
                checkBoxView = (CheckBox) convertView.findViewById( R.id.checkView );
                View item=convertView.findViewById(R.id.item_row2);
                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                convertView.setTag( new RecordViewHolder(rollNoView,checkBoxView) );
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v.findViewById(R.id.checkView) ;
                        cb.toggle();
                        Record record1 = (Record) cb.getTag();
                        record1.setChecked( cb.isChecked() );

                        if (cb.isChecked()) {
                            cb.setText("Present");
                        }
                        else {
                            cb.setText("Absent");
                        }
                    }
                });
                // If CheckBox is toggled, update the planet it is tagged with.
                checkBoxView.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Record record1 = (Record) cb.getTag();
                        record1.setChecked( cb.isChecked() );

                        if (cb.isChecked()) {
                            cb.setText("Present");
                        }
                        else {
                            cb.setText("Absent");
                        }
                    }
                });

            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                RecordViewHolder viewHolder = (RecordViewHolder) convertView.getTag();
                checkBoxView = viewHolder.getmCheckBox() ;
                rollNoView = viewHolder.getmRollNo() ;
                //nameView=viewHolder.getmNameView();
            }

            // Tag the CheckBox with the Planet it is displaying, so that we can
            // access the planet in onClick() when the CheckBox is toggled.
            checkBoxView.setTag( record );

            // Display planet data
            checkBoxView.setChecked( record.isChecked() );
            if(record.isChecked())
                checkBoxView.setText("Present");
            else
                checkBoxView.setText("Absent");
            // nameView.setText( record.getName() );
            rollNoView.setText(record.getRollNo());

            return convertView;
        }
    }


}


