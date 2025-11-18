package com.example.student_firebase_7b_f25;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

public class StudentAdapter extends FirebaseRecyclerAdapter<Student, StudentAdapter.StudentViewHolder> {
    Context context;

    public StudentAdapter(@NonNull FirebaseRecyclerOptions<Student> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentViewHolder holder, int position, @NonNull Student model) {
        holder.tvName.setText(model.getName());
        holder.tvGpa.setText(String.valueOf(model.getCgpa()));
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRef(position).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Record Deleted", Toast.LENGTH_SHORT).show();
                        notifyItemRemoved(position);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent(model, position);
            }
        });
    }

    private void updateStudent(Student model, int position)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.add_update_student_dialog_design, null);
        EditText etName, etID, etGpa;
        etName = view.findViewById(R.id.etName);
        etID = view.findViewById(R.id.etId);
        etGpa = view.findViewById(R.id.etGPA);
        etName.setText(model.getName());
        etID.setText(String.valueOf(model.getId()));
        etGpa.setText(String.valueOf(model.getCgpa()));

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Update Student")
                .setView(view)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Student student = new Student(Integer.parseInt(etID.getText().toString()), etName.getText().toString(), Float.parseFloat(etGpa.getText().toString()));
                        getRef(position).setValue(student)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Student updated", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();

    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.single_student_design, parent, false);
       return new StudentViewHolder(view);
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName, tvGpa;
        ImageView ivDel, ivEdit;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGpa = itemView.findViewById(R.id.tvGPA);
            tvName = itemView.findViewById(R.id.tvName);
            ivDel = itemView.findViewById(R.id.ivDel);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
    }
}
