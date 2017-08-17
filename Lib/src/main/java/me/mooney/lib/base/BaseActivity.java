package me.mooney.lib.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import me.mooney.lib.R;

public class BaseActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_base);

        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        View view= LayoutInflater.from(this)
                .inflate(layoutResID,null);

        FrameLayout frameLayout= (FrameLayout) findViewById(R.id.content_base);
        frameLayout.addView(view);

    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (mToolbar!=null){
            mToolbar.setTitle(title);
        }
    }

    protected void toast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT)
                .show();
    }
}
