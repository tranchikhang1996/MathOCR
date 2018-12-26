package com.example.lap12427.mathocr.ui.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.lap12427.mathocr.R;
import com.example.lap12427.mathocr.ui.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    public abstract BaseFragment getHostFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResLayoutId());
        if(savedInstanceState == null) {
            hostFragment(getHostFragment());
        }
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(this);
    }

    protected int getResLayoutId() {
        return R.layout.common_layout;
    }

    public void hostFragment(BaseFragment fragment, int id) {
        if(fragment == null || getSupportFragmentManager().findFragmentByTag(fragment.getTag()) != null) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, fragment, fragment.TAG);
        transaction.commit();
    }

    public void hostFragment(BaseFragment fragment) {
        hostFragment(fragment, R.id.fragment_container);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
