package com.xianwei.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.xianwei.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.instruction_container)
    FrameLayout instructionContainer;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    private static final String RECIPE = "recipe";
    private static final int INITIALIZED_STEP_ID = 0;

    public static boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpButton();

        Recipe recipe;
        InstructionFragment instructionFragment;
        if (findViewById(R.id.video_container) != null) {
            twoPane = true;
            if (savedInstanceState == null) {
                recipe = getIntent().getExtras().getParcelable(RECIPE);

                instructionFragment = new InstructionFragment();
                VideoFragment videoFragment = new VideoFragment();

                instructionFragment.setRecipe(recipe);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.instruction_container, instructionFragment)
                        .commit();

                videoFragment.setSteps(recipe.getSteps());
                videoFragment.setStepId(INITIALIZED_STEP_ID);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.video_container, videoFragment)
                        .commit();
            }
        } else {
            twoPane = false;
            if (savedInstanceState == null) {
                recipe = getIntent().getExtras().getParcelable(RECIPE);

                instructionFragment = new InstructionFragment();
                instructionFragment.setRecipe(recipe);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.instruction_container, instructionFragment)
                        .commit();
            }
        }
    }
    //override Up button, back to instructionFragment when videoFragmented inflated
    private void setUpButton() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                if (manager.getBackStackEntryCount() > 0) {
                    manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    finish();
                }
            }
        });
    }
    // back to previous fragment when more than one video fragment inflated
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
