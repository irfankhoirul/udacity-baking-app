package com.irfankhoirul.recipe.modul.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 8/9/2017.
 */

public class RecipeChoiceDialog extends DialogFragment implements RecipeChoiceContract.View {

    @BindView(R.id.rv_recipes)
    RecyclerView rvRecipes;

    private SimpleRecipeAdapter recipeAdapter;
    private RecipeChoiceDialogPresenter presenter;
    private RecipeChoiceDialogListener dialogListener;

    public void setDialogListener(RecipeChoiceDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_recipe_choice, container);
        ButterKnife.bind(this, view);

        presenter = new RecipeChoiceDialogPresenter(getActivity().getApplication(), this);
        presenter.loadRecipes();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvRecipes.setLayoutManager(layoutManager);
        recipeAdapter = new SimpleRecipeAdapter(presenter.getLoadedRecipes(), new SimpleRecipeAdapter.SimpleRecipeClickListener() {
            @Override
            public void onRecipeItemClick(Recipe recipe) {
                Log.v("onRecipeItemClick", "True");
                dialogListener.onDismiss(recipe);
            }
        });
        rvRecipes.setAdapter(recipeAdapter);

        return view;
    }

    @Override
    public void updateRecipeList() {
        recipeAdapter.notifyDataSetChanged();
    }

    public interface RecipeChoiceDialogListener {
        void onDismiss(Recipe recipe);
    }
}
