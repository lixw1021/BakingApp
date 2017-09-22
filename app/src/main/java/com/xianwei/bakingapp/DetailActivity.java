package com.xianwei.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.xianwei.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.instruction_container)
    FrameLayout instructionContainer;

    private static final String RECIPE = "recipe";
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        recipe = getIntent().getExtras().getParcelable(RECIPE);

        InstructionFragment instructionFragment = new InstructionFragment();
        instructionFragment.setRecipe(recipe);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.instruction_container, instructionFragment)
                .commit();
    }
}
