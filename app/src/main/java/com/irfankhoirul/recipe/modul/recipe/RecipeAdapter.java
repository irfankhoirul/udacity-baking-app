package com.irfankhoirul.recipe.modul.recipe;

import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.florent37.glidepalette.BitmapPalette;
import com.github.florent37.glidepalette.GlidePalette;
import com.irfankhoirul.recipe.R;
import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.util.DisplayMetricUtils;
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
    private int cardHeightInDp;
    private int cardWidthInDp;

    public RecipeAdapter(ArrayList<Recipe> recipes, int cardWidth, int cardHeight, RecipeClickListener clickListener) {
        this.recipes = recipes;
        this.clickListener = clickListener;
        this.cardHeightInDp = cardHeight;
        this.cardWidthInDp = cardWidth;
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
        holder.tvServing.setText(String.valueOf(recipe.getServings()));

        if (recipe.isFavorite()) {
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        setImageThumbnail(holder, recipe.getImage());

        setCardViewSize(holder);

    }

    private void setCardViewSize(RecipeViewHolder holder) {
        ViewGroup.LayoutParams layoutParams =
                holder.cvRecipe.getLayoutParams();
        layoutParams.width = DisplayMetricUtils.convertDpToPixel(cardWidthInDp);
        layoutParams.height = DisplayMetricUtils.convertDpToPixel(cardHeightInDp);
        holder.cvRecipe.setLayoutParams(layoutParams);
    }

    private void setImageThumbnail(final RecipeViewHolder holder, String url) {
        GlideApp.with(holder.ivThumbnail)
                .load(url)
                .placeholder(R.drawable.ic_food_placeholder)
                .listener(GlidePalette.with(url)
                        .use(GlidePalette.Profile.MUTED)
                        .intoBackground(holder.vFooter, GlidePalette.Swatch.RGB)
                        .intoTextColor(holder.tvTitle, GlidePalette.Swatch.BODY_TEXT_COLOR)
                        .intoTextColor(holder.tvServing, GlidePalette.Swatch.BODY_TEXT_COLOR)
                        .intoCallBack(new BitmapPalette.CallBack() {
                            @Override
                            public void onPaletteLoaded(@Nullable Palette palette) {
                                if (palette != null && palette.getMutedSwatch() != null) {
                                    holder.ivFavorite.setColorFilter(palette.getMutedSwatch()
                                            .getBodyTextColor());
                                    holder.ivServing.setColorFilter(palette.getMutedSwatch()
                                            .getBodyTextColor());
                                }
                            }
                        })
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

        @BindView(R.id.cv_recipe)
        CardView cvRecipe;
        @BindView(R.id.rl_recipe_container)
        RelativeLayout rlRecipeContainer;
        @BindView(R.id.iv_thumbnail)
        ImageView ivThumbnail;
        @BindView(R.id.v_footer)
        View vFooter;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv_favorite)
        ImageView ivFavorite;
        @BindView(R.id.iv_serving)
        ImageView ivServing;
        @BindView(R.id.tv_serving)
        TextView tvServing;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ivThumbnail.setOnClickListener(this);

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
