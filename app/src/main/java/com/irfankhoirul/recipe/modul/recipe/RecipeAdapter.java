package com.irfankhoirul.recipe.modul.recipe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.glidepalette.GlidePalette;
import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.util.GlideApp;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private ArrayList<Recipe> recipes = new ArrayList<>();
    private RecipeClickListener clickListener;

    public RecipeAdapter(ArrayList<Recipe> recipes, RecipeClickListener clickListener) {
        this.recipes = recipes;
        this.clickListener = clickListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);

        holder.tvTitle.setText(recipe.getName());

        if (recipe.isFavorite()) {
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        setImageThumbnail(holder, recipe.getImage());

    }

    private void setImageThumbnail(RecipeViewHolder holder, String url) {
        GlideApp.with(holder.ivThumbnail)
                .load(url)
                .placeholder(R.drawable.ic_food_placeholder)
                .listener(GlidePalette.with(url)
                        .use(GlidePalette.Profile.MUTED)
                        .intoBackground(holder.vFooter, GlidePalette.Swatch.RGB)
                        .intoTextColor(holder.tvTitle, GlidePalette.Swatch.BODY_TEXT_COLOR)
                        .crossfade(true)
                )
                .into(holder.ivThumbnail);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    interface RecipeClickListener {
        void onRecipeItemClick(Recipe review);

        void onFavoriteClick(Recipe recipe, int position);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_thumbnail)
        ImageView ivThumbnail;
        @BindView(R.id.v_footer)
        View vFooter;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv_favorite)
        ImageView ivFavorite;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

            ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onFavoriteClick(recipes.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            clickListener.onRecipeItemClick(recipes.get(getAdapterPosition()));
        }
    }

}
