package com.student.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.student.dashboard.entity.Student;
import com.student.dashboard.holder.StudentHolder;

public class LoginActivity extends AppCompatActivity implements ValueEventListener{

    public static final String STUDENTS_NODE = "students";
    public static final String SHARED_DB = "database";
    public static final String ROLL_NUMBER = "roll";
    public static final String AADHAR_NUMBER = "aadhar";
    private EditText rollNumber, password;
    private CheckBox checkBox;

    private DatabaseReference reference;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar().isShowing())
            getSupportActionBar().hide();

        rollNumber = findViewById(R.id.roll_number);
        password = findViewById(R.id.password);
        checkBox = findViewById(R.id.rememberMe);

        rollNumber.setText(getSharedPreferences(SHARED_DB, MODE_PRIVATE).getString(ROLL_NUMBER, ""));
        password.setText(getSharedPreferences(SHARED_DB, MODE_PRIVATE).getString(AADHAR_NUMBER, ""));

        loginClicked(null);

    }

    public void aboutClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Developers");
        builder.setMessage("Anshuman Pandey \n\nHardik Goel");
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void contactUsClicked(View view) {
        ContactUsDialog contactUsDialog = new ContactUsDialog(this);
        contactUsDialog.show();
    }

    public void loginClicked(View view) {
        if (rollNumber.getText().toString().isEmpty() || password.getText().toString().isEmpty())
            return;

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Checking Credentials...");
        dialog.show();

        reference = FirebaseDatabase.getInstance().getReference(STUDENTS_NODE);
        reference.addValueEventListener(this);

    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChild(rollNumber.getText().toString())) {
            Student student = dataSnapshot.child(rollNumber.getText().toString()).getValue(Student.class);
            if (student.aadhar.equals(password.getText().toString())) {
                if (checkBox.isChecked()) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_DB, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ROLL_NUMBER, rollNumber.getText().toString());
                    editor.putString(AADHAR_NUMBER, password.getText().toString());
                    editor.apply();
                }

                StudentHolder.student = student;
                startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                finish();

            }
        }
        dialog.dismiss();

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        dialog.dismiss();
    }

    @Override
    public void finish() {
        super.finish();
        if(reference!=null)
        reference.removeEventListener(this);
    }
}
