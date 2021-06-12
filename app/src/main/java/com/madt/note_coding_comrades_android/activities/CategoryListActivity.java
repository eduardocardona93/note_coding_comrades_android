package com.madt.note_coding_comrades_android.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.madt.note_coding_comrades_android.R;

public class CategoryListActivity extends AppCompatActivity {
    RecyclerView rcCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        rcCategories = findViewById(R.id.rcCategories);
        rcCategories.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        rcCategories.setAdapter(new CategoryAdapter(this));

    }

    class CategoryAdapter extends
            RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        private Activity activity;


        CategoryAdapter(Activity activity) {
            this.activity = activity;

        }

        @NonNull
        @Override
        public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_category, parent, false);

            return new CategoryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {

            holder.listRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intentEdit = new Intent(getBaseContext(), NoteListActivity.class);
                    startActivity(intentEdit);
                }
            });


        }

        @Override
        public int getItemCount() {
            return 8;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ConstraintLayout listRow;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                listRow = itemView.findViewById(R.id.list_item);

            }
        }

    }
}