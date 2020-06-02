package com.student.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.student.dashboard.entity.Student;
import com.student.dashboard.entity.TwoAttributes;
import com.student.dashboard.holder.StudentHolder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements ValueEventListener {

    private ProgressDialog dialog;
    private static final String PRESENT = "present";
    private int total_attendance = 0;
    private float total_marks = 0f;

    private TextView attendance, marks, name, roll, address, gender, contact, standard;
    private RecyclerView attendanceRecyclerView, marksRecyclerView;
    private ImageView profileImage;

    private ArrayList<TwoAttributes> marksAttributes, attendanceAttributes;
    private TableAdapter marksAdapter, attendanceAdapter;

    private Student student = null;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        attendance = findViewById(R.id.total_attendance);
        marks = findViewById(R.id.resultText);
        name = findViewById(R.id.name);
        roll = findViewById(R.id.roll);
        address = findViewById(R.id.address);
        standard = findViewById(R.id.standard);
        gender = findViewById(R.id.gender);
        contact = findViewById(R.id.contact);
        profileImage = findViewById(R.id.studentImage);

        attendanceRecyclerView = findViewById(R.id.attendanceRecycler);
        marksRecyclerView = findViewById(R.id.marksRecycler);

        marksAdapter = new TableAdapter(this);
        attendanceAdapter = new TableAdapter(this);

        marksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        marksRecyclerView.setAdapter(marksAdapter);
        attendanceRecyclerView.setAdapter(attendanceAdapter);

        marksAttributes = new ArrayList<>();
        attendanceAttributes = new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please Wait...");
        dialog.show();

       reference = FirebaseDatabase.getInstance().getReference(LoginActivity.STUDENTS_NODE)
                .child(StudentHolder.student.roll);
        reference.addValueEventListener(this);

    }

    private void convertAttendance() {
        attendanceAttributes.add(0,new TwoAttributes("Date","Status (P/A)"));
        if(student.getAttendance()==null) {
            attendanceAdapter.setAttributes(attendanceAttributes);
            attendance.setText("Total Attendance : N A");
            return;
        }
        for (String s : student.getAttendance().keySet()) {
            attendanceAttributes.add(new TwoAttributes(s, student.getAttendance().get(s)));
            if (student.getAttendance().get(s).toLowerCase().equals(PRESENT))
                total_attendance++;
        }
        attendanceAdapter.setAttributes(attendanceAttributes);
        attendance.setText("Total Attendance : " + total_attendance + "/" + student.getAttendance().size());
    }

    private void convertMarks() {
        marksAttributes.add(0,new TwoAttributes("Subject","Score"));
        if(student.getMarks()==null) {
            marksAdapter.setAttributes(marksAttributes);
            marks.setText("Total Marks : N A");
            return;
        }
        for (String s : student.getMarks().keySet()) {
            marksAttributes.add(new TwoAttributes(s, "" + student.getMarks().get(s)));
            total_marks += student.marks.get(s);
        }
        marksAdapter.setAttributes(marksAttributes);
        marks.setText("Total Marks : " + total_marks);

    }

    public void changeProfileImage(View view) {

        if (!checkPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {

            loadImage();
        }

    }

    private void loadImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data.getData() == null)
                return;

            Glide.with(this).load(data.getData()).into(profileImage);

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setMessage("Uploading Image...");
            dialog.show();

            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(student.roll);
            storageReference.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> taskUri = taskSnapshot.getStorage().getDownloadUrl();
                    while (!taskUri.isSuccessful()) {
                    }
                    String url = taskUri.getResult().toString();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(LoginActivity.STUDENTS_NODE)
                            .child(student.roll);
                    student.setImageurl(url);
                    ref.setValue(student);
                    dialog.dismiss();
                }
            });

        }

    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (!checkPermission()) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            } else {
                loadImage();
            }
        }

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        student = dataSnapshot.getValue(Student.class);
        if (student != null) {
            initialize();
            dialog.dismiss();
        } else {
            Toast.makeText(this, "Some Error Occured", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Please Contact System Admistrator", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            finish();
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(this, "Some Error Occured", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Please Contact System Admistrator", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        finish();
    }

    private void initialize() {

        if (getSupportActionBar().isShowing())
            getSupportActionBar().setTitle(student.name);

        name.setText(student.name);
        roll.setText(student.roll);
        standard.setText(student.standard);
        gender.setText(student.gender);
        contact.setText(student.contact);
        address.setText(student.address);

        name.setSelected(true);
        roll.setSelected(true);
        standard.setSelected(true);
        gender.setSelected(true);
        contact.setSelected(true);

        try {
            URL url = new URL(student.imageurl);
            Glide.with(this).load(student.imageurl).into(profileImage);
        } catch (Exception e) {
            profileImage.setImageResource(R.drawable.student);
        }

        marksAttributes.clear();
        attendanceAttributes.clear();
        convertMarks();
        convertAttendance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.contactUs:
                ContactUsDialog dialog = new ContactUsDialog(this);
                dialog.show();
                break;
            case R.id.notices:
                startActivity(new Intent(this,NoticeActivity.class));
                break;
            case R.id.signOut:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to Sign Out ?");
                builder.setTitle("Confirm");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.SHARED_DB,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(LoginActivity.ROLL_NUMBER);
                        editor.remove(LoginActivity.AADHAR_NUMBER);
                        editor.apply();
                        dialog.dismiss();
                        startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
                        finish();
                    }
                });
                builder.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        reference.removeEventListener(this);
    }
}
