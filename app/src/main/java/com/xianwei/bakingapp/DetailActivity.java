package com.xianwei.bakingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private Recipe recipe;
    Fragment fragment;
    InstructionFragment instructionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpButton();

        if (savedInstanceState == null) {
            recipe = getIntent().getExtras().getParcelable(RECIPE);

            instructionFragment = new InstructionFragment();
            instructionFragment.setRecipe(recipe);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.instruction_container, instructionFragment)
                    .commit();
        } else {
            fragment = getSupportFragmentManager()
                    .getFragment(savedInstanceState, "savedFragment");

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.instruction_container, fragment)
                    .commit();
        }
    }

    private void setUpButton() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                if (manager.getBackStackEntryCount() > 0) {
                    manager.popBackStack(null, manager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragment = getSupportFragmentManager().findFragmentById(R.id.instruction_container);
        getSupportFragmentManager().putFragment(outState, "savedFragment", fragment);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
