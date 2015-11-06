package lakala.com.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import butterknife.Bind;

import butterknife.OnClick;
import lakala.com.userinfo.activity.BaseActivity;
import lakala.com.userinfo.activity.CallLogActivity;
import lakala.com.userinfo.activity.ShortMessageActivity;

public class MainActivity extends BaseActivity {

    @Bind(R.id.shortcutmessage_button) Button shortMessageButton;
    @Bind(R.id.callog_button) Button callLogButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 去通话记录页面
     */
    @OnClick(R.id.callog_button)
     void goCallLogPage(){
       startActivity(new Intent(this, CallLogActivity.class));
    }

    /**
     * 去短信记录页面
     */
    @OnClick(R.id.shortcutmessage_button)
     void goShortMessagePage(){
        startActivity(new Intent(this, ShortMessageActivity.class));
    }
}
