package com.student.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.student.dashboard.entity.Notice;
import com.student.dashboard.entity.Student;
import com.student.dashboard.holder.StudentHolder;

import java.util.ArrayList;

public class NoticeActivity extends AppCompatActivity implements ValueEventListener {

    private static final String NOTICE_NODE_NAME = "notice";
    private ProgressDialog dialog;
    private ArrayList<Notice> noticeArrayList;

    private RecyclerView recyclerView;
    private NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        recyclerView = findViewById(R.id.noticeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoticeAdapter(this);
        recyclerView.setAdapter(adapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Notices...");
        dialog.setCancelable(false);
        dialog.show();

        noticeArrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(NOTICE_NODE_NAME);
        reference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        noticeArrayList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Notice notice = snapshot.getValue(Notice.class);
            if (notice != null) {
                switch (notice.type) {
                    case "ALL":
                        noticeArrayList.add(notice);
                        break;
                    case "CW":
                        for (String cls : notice.filter.split(";")){
                            if (StudentHolder.student.standard.toLowerCase().equals(cls.toLowerCase().trim()))
                                noticeArrayList.add(notice);
                        }
                        break;
                    case "SS":
                        for (String roll : notice.filter.split(";")){
                            if (StudentHolder.student.roll.equals(roll.trim()))
                                noticeArrayList.add(notice);
                        }
                        break;
                }
            }
        }
        adapter.setList(noticeArrayList);
        dialog.dismiss();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        dialog.dismiss();
    }

}
