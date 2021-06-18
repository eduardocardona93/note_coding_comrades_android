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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.madt.note_coding_comrades_android.R;
import com.madt.note_coding_comrades_android.model.Category;
import com.madt.note_coding_comrades_android.model.NoteAppViewModel;
import com.madt.note_coding_comrades_android.utilities.NoteUtils;

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {
    RecyclerView rcCategories;
    ImageView createCategory;
    private NoteAppViewModel noteAppViewModel;
    ArrayList<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        noteAppViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(NoteAppViewModel.class);

        noteAppViewModel.getAllCategories().observe(this, categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
            NoteUtils.showLog("list size", categoryList.size() + "");
            NoteUtils.showLog("db list size", categories.size() + "");
            rcCategories = findViewById(R.id.rcCategories);
            rcCategories.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
            rcCategories.setAdapter(new CategoryAdapter(this, categoryList));
        });


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
                        if (categoryName.isEmpty()) {
                            alertBox("Please enter value for Category name");
                            return;
                        }
                        if (categoryList.contains(categoryName)) {
                            alertBox("Category name already exist!");
                            return;
                        }
                        // categoryList.add(new Category(categoryName));
                        noteAppViewModel.insertCategory(new Category(categoryName));
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    // method that will display the alert dialog
    public void alertBox(String message) {
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
        private ArrayList<Category> categoryList;

        CategoryAdapter(Activity activity, ArrayList<Category> categoryList) {
            this.activity = activity;
            this.categoryList = categoryList;
            NoteUtils.showLog("adpter list size", categoryList.size() + "");
        }

        @NonNull
        @Override
        public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_category, parent, false);

            return new CategoryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {

            holder.categoryNameTV.setText(categoryList.get(position).getCatName());
            holder.categoryNameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), NoteListActivity.class);
                    intent.putExtra(NoteListActivity.CATEGORY_ID, categoryList.get(position).getCatId());
                    intent.putExtra(NoteListActivity.CATEGORY_NAME, categoryList.get(position).getCatName());
                    startActivity(intent);
                }
            });

            /*holder.categoryNameTV.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    renameCategoryDialog(categoryList.get(position).getCatId());
                    return false;
                }
            });*/


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


        void renameCategoryDialog(int catId) {

            AlertDialog.Builder builder = new AlertDialog.Builder(CategoryListActivity.this);
            LayoutInflater layoutInflater = LayoutInflater.from(CategoryListActivity.this);
            View view = layoutInflater.inflate(R.layout.dialog_create_category, null);
            builder.setView(view);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            EditText categoryNameET = view.findViewById(R.id.categoryNameET);
            TextView txtLavel = view.findViewById(R.id.txtLabel);
            Button btnCreate = view.findViewById(R.id.btnCreateCategory);

            txtLavel.setText("Rename Category");
            btnCreate.setText("Rename");

            btnCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String categoryName = categoryNameET.getText().toString().trim();
                    if (categoryName.isEmpty()) {
                        alertBox("Please enter value for Category name");
                        return;
                    }
                    if (categoryList.contains(categoryName)) {
                        alertBox("Category name already exist!");
                        return;
                    }

                    noteAppViewModel.getCategoryById(catId).observe(CategoryListActivity.this, category -> {
                        category.setCatName(categoryName);
                        noteAppViewModel.updateCategory(category);
                        alertDialog.dismiss();

                    });

                    //rcCategories.setAdapter(new CategoryAdapter(CategoryHavingNotes.this, categoryList));

                }
            });
        }

    }
}