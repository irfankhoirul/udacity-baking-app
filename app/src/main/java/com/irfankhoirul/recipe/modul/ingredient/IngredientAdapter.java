package com.irfankhoirul.recipe.modul.ingredient;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 8/3/2017.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private Context context;

    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        if (context == null) {
            context = itemView.getContext();
        }

        return new IngredientAdapter.IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        Ingredient item = ingredients.get(position);
        holder.tvIngredient.setText(item.getIngredient());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        String unit;
        switch (item.getMeasure()) {
            case "G":
                unit = context.getResources().getQuantityString(R.plurals.plural_unit_gram,
                        (int) item.getQuantity());
                break;
            case "CUP":
                unit = context.getResources().getQuantityString(R.plurals.plural_unit_cup,
                        (int) item.getQuantity());
                break;
            case "TBLSP":
                unit = context.getResources().getQuantityString(R.plurals.plural_unit_tablespoon,
                        (int) item.getQuantity());
                break;
            case "TSP":
                unit = context.getResources().getQuantityString(R.plurals.plural_unit_teaspoon,
                        (int) item.getQuantity());
                break;
            case "K":
                unit = context.getResources().getQuantityString(R.plurals.plural_unit_kilo_grams,
                        (int) item.getQuantity());
                break;
            case "OZ":
                unit = context.getResources().getQuantityString(R.plurals.plural_unit_ounce,
                        (int) item.getQuantity());
                break;
            default:
                unit = "";
                break;
        }
        holder.tvUnit.setText(unit);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient)
        TextView tvIngredient;
        @BindView(R.id.tv_quantity)
        TextView tvQuantity;
        @BindView(R.id.tv_unit)
        TextView tvUnit;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
