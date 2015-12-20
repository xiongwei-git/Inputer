package com.android.ted.inputer.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.ted.inputer.R;

/**
 * Description:
 * Creator: ZhangJinWei
 * Date:15/12/20 上午9:16
 */
public class OrderActivity extends Activity implements View.OnClickListener{

    private android.widget.TextView txtMain;
    private android.widget.TextView txtKeyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order);
        this.txtKeyWord = (TextView) findViewById(R.id.txtKeyWord);
        this.txtMain = (TextView) findViewById(R.id.txtMain);

        txtMain.setOnClickListener(this);
        txtKeyWord.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.txtMain:
                startActivity(new Intent(OrderActivity.this, MainActivity.class));
                break;
            case R.id.txtKeyWord:
                startActivity(new Intent(OrderActivity.this, TestAct.class));
                break;

        }
    }
}
