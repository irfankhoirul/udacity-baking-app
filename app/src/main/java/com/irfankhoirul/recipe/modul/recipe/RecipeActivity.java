package com.irfankhoirul.recipe.modul.recipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.irfankhoirul.recipe.R;

import butterknife.ButterKnife;

public class RecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, RecipeFragment.newInstance(getIntent().getLongExtra("recipeId", 0L)))
                    .commit();
        }
    }

}
