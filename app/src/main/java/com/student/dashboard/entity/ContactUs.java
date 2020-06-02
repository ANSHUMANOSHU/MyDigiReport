package com.student.dashboard.entity;

import android.app.ProgressDialog;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContactUs {

    private static final String CONTACTUS_NODE = "contactus";

    public String heading,body,contact;
    public long stamp;

    public ContactUs() {
    }

    public ContactUs(String heading, String body, String contact, long stamp) {
        this.heading = heading;
        this.body = body;
        this.contact = contact;
        this.stamp = stamp;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    public void save(final ProgressDialog progressDialog){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(CONTACTUS_NODE);
        ref.child(contact).setValue(this, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                progressDialog.dismiss();
            }
        });
    }

}
