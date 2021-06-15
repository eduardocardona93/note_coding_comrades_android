package com.madt.note_coding_comrades_android.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.madt.note_coding_comrades_android.R;

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {
    RecyclerView rcCategories;
    ImageView createCategory;

    ArrayList<String> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        categoryList.add("Ranajna");
        categoryList.add("Eduardo");
        categoryList.add("Sumit");
        categoryList.add("Lino");
        categoryList.add("Dinamol");

        rcCategories = findViewById(R.id.rcCategories);
        rcCategories.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        rcCategories.setAdapter(new CategoryAdapter(this, categoryList));

        createCategory = findViewById(R.id.createCategory);
        createCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryListActivity.this);
                LayoutInflater layoutInflater = LayoutInflater.from(CategoryListActivity.this);
                View view = layoutInflater.inflate(R.layout.dialog_create_category, null);
                builder.setView(view);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                EditText categoryNameET = view.findViewById(R.id.categoryNameET);
                Button btnCreate = view.findViewById(R.id.btnCreateCategory);

                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String categoryName = categoryNameET.getText().toString().trim();
                        if(categoryName.isEmpty()){
                            alertBox("Please enter value for Category name");
                            return;
                        }
                        if(categoryList.contains(categoryName)){
                            alertBox("Category name already exist!");
                            return;
                        }
                        categoryList.add(categoryName);
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    // method that will display the alert dialog
    public void alertBox(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(CategoryListActivity.this);
        builder.setTitle("Alert");
        builder.setMessage(message);

        builder.setCancelable(false);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    class CategoryAdapter extends
            RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        private Activity activity;
        private ArrayList<String> categoryList;

        CategoryAdapter(Activity activity, ArrayList<String> categoryList) {
            this.activity = activity;
            this.categoryList = categoryList;
        }

        @NonNull
        @Override
        public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_category, parent, false);

            return new CategoryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {

            holder.categoryNameTV.setText(categoryList.get(position));
            holder.categoryNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intentEdit = new Intent(getBaseContext(), NoteListActivity.class);
                    startActivity(intentEdit);
                }
            });


        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView categoryNameTV;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                categoryNameTV = itemView.findViewById(R.id.categoryNameTV);

            }
        }

    }
}