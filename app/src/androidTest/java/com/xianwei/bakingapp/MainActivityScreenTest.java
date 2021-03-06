package com.xianwei.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by xianwei li on 9/25/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecyclerViewItem_OpenDetailActivity() {
        onView(withId(R.id.rv_recipe)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void RecyclerViewNameCheck() {
        onView(withRecyclerView(R.id.rv_recipe).atPosition(0))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
    }

    @Test
    public void RecyclerViewNumberCheck() {
        onView(withRecyclerView(R.id.rv_recipe).atPosition(0))
                .check(matches(hasDescendant(withText("8"))));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerView) {
        return new RecyclerViewMatcher(recyclerView);
    }
}
