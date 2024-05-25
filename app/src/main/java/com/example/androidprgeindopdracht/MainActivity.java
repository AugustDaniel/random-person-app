package com.example.androidprgeindopdracht;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Adapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PersonAdapter.OnItemClickListener, ApiListener {

    private final List<Person> personList = new ArrayList<>();
    private PersonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adapter = new PersonAdapter(this.getApplicationContext(), this.personList, this);
        RecyclerView rv = findViewById(R.id.main_rv);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns(this, 200)));
        new ApiManager(this.getApplicationContext(), this).getPersons();
    }

    @Override
    public void onAvailable(Person person) {
        this.personList.add(person);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Error error) {
    }

    @Override
    public void onItemClick(int clickedPosition) {
        Person selectedPhoto = personList.get(clickedPosition);
        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(Person.TAG, selectedPhoto);
        startActivity(detailIntent);
    }

    // Method to convert dp to pixels
    public int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    // Method to calculate the number of columns
    public int calculateNoOfColumns(Context context, int columnWidthDp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthPx = displayMetrics.widthPixels;
        float columnWidthPx = dpToPx(columnWidthDp);
        return Math.round(screenWidthPx / columnWidthPx);
    }
}