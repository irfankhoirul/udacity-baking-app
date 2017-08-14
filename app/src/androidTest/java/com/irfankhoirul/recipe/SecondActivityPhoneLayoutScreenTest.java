package com.irfankhoirul.recipe;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.irfankhoirul.recipe.modul.recipe.RecipeActivity;
import com.irfankhoirul.recipe.modul.recipe_detail.RecipeDetailActivity;
import com.irfankhoirul.recipe.modul.step_detail.StepDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Note : Do not run this test before run @{@link FirstStoragePermissionScreenTest}
 */

public class SecondActivityPhoneLayoutScreenTest {

    @Rule
    public IntentsTestRule<RecipeActivity> mActivityTestRule =
            new IntentsTestRule<>(RecipeActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // Deprecated
//        Espresso.registerIdlingResources(mIdlingResource);
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void allInteractionTest() {
        onView(withId(R.id.fl_recipe_container)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_recipes)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(withId(R.id.vp_container)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_ingredient)).check(matches(isDisplayed()));
        onView(withId(R.id.vp_container)).perform(swipeLeft());
        onView(withId(R.id.rv_step)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_step))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.rv_step)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerViewViewAction
                        .clickChildViewWithId(R.id.tv_step_short_description)));
        intended(hasComponent(StepDetailActivity.class.getName()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            // Deprecated
//            Espresso.unregisterIdlingResources(mIdlingResource);
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
