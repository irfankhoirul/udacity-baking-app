package com.irfankhoirul.recipe.modul.recipe;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.irfankhoirul.recipe.data.pojo.Recipe;
import com.irfankhoirul.recipe.data.pojo.Thumbnail;
import com.irfankhoirul.recipe.data.source.RequestResponseListener;
import com.irfankhoirul.recipe.data.source.remote.RemoteRecipeRecipeDataSource;
import com.irfankhoirul.recipe.data.source.remote.RemoteRecipeRecipeDataSourceImpl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by Irfan Khoirul on 7/25/2017.
 */

public class RecipeListViewModel extends AndroidViewModel implements RecipeListContract.ViewModel {

    private final RecipeListContract.View mView;
    private final RemoteRecipeRecipeDataSource remoteRecipeRecipeDataSource;
    private Call<Recipe> recipeRequest;
    private ArrayList<Recipe> recipes = new ArrayList<>();

    public RecipeListViewModel(Application application, RecipeListContract.View mView) {
        super(application);
        this.mView = mView;
        remoteRecipeRecipeDataSource = new RemoteRecipeRecipeDataSourceImpl();
    }

    @Override
    public void loadRecipes(int source) {
        mView.setLoading(true, "Preparing Recipes...");
        if (recipes.size() == 0) {
            remoteRecipeRecipeDataSource.getRecipes(new RequestResponseListener<Recipe>() {
                @Override
                public void onRequestStart(Call<Recipe> call) {
                    recipeRequest = call;
                }

                @Override
                public void onSuccess(ArrayList<Recipe> result) {
                    recipes.clear();
                    recipes.addAll(result);
                    for (int i = 0; i < result.size(); i++) {
                        Recipe recipe = recipes.get(i);
                        if (recipe.getImage() == null || recipe.getImage().isEmpty()) {
                            int stepCount = recipe.getSteps().size();
                            for (int j = stepCount - 1; j >= 0; j--) {
                                if (recipe.getSteps().get(j).getThumbnailURL() != null &&
                                        !recipe.getSteps().get(j).getThumbnailURL().isEmpty()) {
                                    Log.v("Thumbnail:" + i, "FromVideoThumbnail");
                                    recipe.setImage(recipe.getSteps().get(j).getThumbnailURL());
                                    break;
                                } else if (recipe.getSteps().get(j).getVideoURL() != null &&
                                        !recipe.getSteps().get(j).getVideoURL().isEmpty()) {
                                    Log.v("Thumbnail:" + i, "FromVideoFrame");
                                    String[] params = new String[2];
                                    params[0] = recipe.getSteps().get(j).getVideoURL(); // url
                                    params[1] = String.valueOf(i); // position
                                    new GetVideoThumbnailTask().execute(params);
                                    break;
                                }
                            }
                        }
                    }
                    mView.setLoading(false, null);
                    mView.updateRecipeList();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    mView.setLoading(false, null);
                }
            });
        } else {
            mView.setLoading(false, null);
            mView.updateRecipeList();
        }
    }

    @Override
    public ArrayList<Recipe> getLoadedRecipes() {
        return recipes;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final RecipeListContract.View mView;

        public Factory(@NonNull Application application, RecipeListContract.View view) {
            mApplication = application;
            mView = view;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RecipeListViewModel(mApplication, mView);
        }
    }

    private class GetVideoThumbnailTask extends AsyncTask<String, Void, Thumbnail> {
        @Override
        protected Thumbnail doInBackground(String... strings) {
            Bitmap bitmap = null;
            MediaMetadataRetriever mediaMetadataRetriever = null;
            try {
                mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(strings[0], new HashMap<String, String>());
                bitmap = mediaMetadataRetriever.getFrameAtTime();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mediaMetadataRetriever != null) {
                    mediaMetadataRetriever.release();
                }
            }

            String path = "";
            if (bitmap != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
                path = MediaStore.Images.Media.insertImage(
                        getApplication().getApplicationContext().getContentResolver(),
                        bitmap, "tmp_recipe_thumb" + strings[1], null);
            }

            Thumbnail thumbnail = new Thumbnail();
            thumbnail.setPath(path);
            thumbnail.setPosition(Integer.parseInt(strings[1]));

            return thumbnail;
        }

        @Override
        protected void onPostExecute(Thumbnail result) {
            recipes.get(result.getPosition()).setImage(result.getPath());
            mView.updateRecipeList();
        }
    }
}
