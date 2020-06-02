package com.student.dashboard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Date;
import androidx.annotation.NonNull;
import com.student.dashboard.entity.ContactUs;

public class ContactUsDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private EditText heading, body, contact;
    private Button submit;

    public ContactUsDialog(@NonNull Context context) {
        super(context);
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);

        View view = LayoutInflater.from(context).inflate(R.layout.contact_us, null, false);
        heading = view.findViewById(R.id.heading);
        body = view.findViewById(R.id.body);
        contact = view.findViewById(R.id.contact);
        submit = view.findViewById(R.id.submit);

        submit.setOnClickListener(this);

        setContentView(view);

        Window window = getWindow();
        if (window != null)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View view) {
        if (contact.getText().toString().isEmpty()) {
            contact.setError("Required");
            return;
        }
        if (heading.getText().toString().isEmpty()) {
            heading.setError("Required");
            return;
        }
        if (body.getText().toString().isEmpty()) {
            body.setError("Required");
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Posting Your Query Please Wait...");
        progressDialog.show();
        progressDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Toast.makeText(context, "Query Submitted Successfully", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "We'll get back to you within 24 Hrs.", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        ContactUs contactUs = new ContactUs(heading.getText().toString(),
                body.getText().toString(), contact.getText().toString(),
                new Date().getTime());

        contactUs.save(progressDialog);
    }
}
