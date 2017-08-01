package com.irfankhoirul.recipe.modul.recipe;


import android.Manifest;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private static final int WRITE_STORAGE_PERMISSION_REQUEST_CODE = 100;

    @BindView(R.id.rv_recipes)
    RecyclerView rvRecipes;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.tv_loading_message)
    TextView tvLoadingMessage;

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

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showPermissionDialog();

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_STORAGE_PERMISSION_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

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
//        rvRecipes.getItemAnimator().setChangeDuration(0);
    }

    @Override
    public void updateRecipeList(ArrayList<Recipe> recipes) {

    }

    private void showPermissionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_permission, null);
        dialogBuilder.setView(dialogView);
//        dialogBuilder.setTitle(R.string.dialog_title_sort);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog permissionDialog = dialogBuilder.create();
        permissionDialog.setCancelable(false);
        permissionDialog.show();
    }

    @Override
    public void setLoading(boolean status, @Nullable String message) {
        if (status) {
            rvRecipes.setVisibility(View.GONE);
            tvLoadingMessage.setText(message);
            llLoading.setVisibility(View.VISIBLE);
        } else {
            llLoading.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void updateRecipeList() {
        recipeAdapter.notifyDataSetChanged();
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_STORAGE_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mViewModel.loadRecipes(0);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showError("Permission not granted");
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
