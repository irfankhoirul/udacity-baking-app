package com.irfankhoirul.recipe.modul.recipe;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.util.DisplayMetricUtils;
import com.irfankhoirul.recipe.util.RecyclerViewMarginDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends LifecycleFragment
        implements RecipeListContract.View, RecipeAdapter.RecipeClickListener {

    @BindView(R.id.rv_recipes)
    RecyclerView rvRecipes;

    private RecipeListViewModel mViewModel;
    private RecipeAdapter recipeAdapter;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecipeListViewModel.Factory factory = new RecipeListViewModel.Factory(
                getActivity().getApplication(), this);

        mViewModel = ViewModelProviders.of(this, factory)
                .get(RecipeListViewModel.class);
        Log.v("ViewModelHash", mViewModel.toString());

        Log.v("RecipeCount", String.valueOf(mViewModel.getLoadedRecipes().size()));
        mViewModel.loadRecipes(0);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        int marginInPixel = DisplayMetricUtils.convertDpToPixel(8);
        int deviceWidthInDp = DisplayMetricUtils.convertPixelsToDp(
                DisplayMetricUtils.getDeviceWidth(getActivity()));

        int column = deviceWidthInDp / 300;

        RecyclerViewMarginDecoration decoration =
                new RecyclerViewMarginDecoration(RecyclerViewMarginDecoration.ORIENTATION_VERTICAL,
                        marginInPixel, column);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), column);
        rvRecipes.setLayoutManager(layoutManager);
        rvRecipes.addItemDecoration(decoration);
        recipeAdapter = new RecipeAdapter(mViewModel.getLoadedRecipes(), this);
        rvRecipes.setAdapter(recipeAdapter);
    }

    @Override
    public void updateRecipeList(ArrayList<Recipe> recipes) {

    }

    @Override
    public void setLoading(boolean status, @Nullable String message) {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void updateRecipeList() {
        recipeAdapter.notifyDataSetChanged();
        Log.v("ViewNotified", "True");
    }

    @Override
    public void onRecipeItemClick(Recipe review) {

    }

    @Override
    public void onFavoriteClick(Recipe recipe, int position) {
        if (recipe.isFavorite()) {
            recipe.setFavorite(false);
        } else {
            recipe.setFavorite(true);
        }
        recipeAdapter.notifyItemChanged(position);
    }
}
