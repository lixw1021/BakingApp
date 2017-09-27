package com.xianwei.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by xianwei li on 9/26/2017.
 */

@RunWith(AndroidJUnit4.class)
public class VideoFragmentButtonTest {

    @Rule
    public ActivityTestRule<DetailActivity> activityTestRule
            = new ActivityTestRule<>(DetailActivity.class);

    @Before
    public void init() {

    }

    @Test
    public void clickNextStepButton_LaunchNewFragment() {
        onView(withId(R.id.rv_steps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
}
