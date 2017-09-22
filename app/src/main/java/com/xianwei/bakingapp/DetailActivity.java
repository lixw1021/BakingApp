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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(RECIPE)) {
            recipe = bundle.getParcelable(RECIPE);

            InstructionFragment instructionFragment = new InstructionFragment();
            instructionFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.instruction_container, instructionFragment)
                    .commit();
        }



    }
}
