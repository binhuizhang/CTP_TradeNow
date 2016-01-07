package zhangbinhui.cn.com.sfit.tradenow2;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zhangbinhui.cn.com.sfit.tradenow2.field.PositionDetailField;

/**
 * Created by zhang.binhui on 2015-12-08.
 */
public class LoginActivity extends Activity{
    public static TextView errorInfo = null;
    EditText userNameText = null;
    EditText passwordText = null;
    String investorID = null;
    String password = null;
    ScrollView scroller = null;
    Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userNameText = (EditText)findViewById(R.id.userNameText);
        passwordText = (EditText)findViewById(R.id.passwdText);
        errorInfo = (TextView)findViewById(R.id.errorInfo);
        errorInfo.setMovementMethod(new ScrollingMovementMethod());

        scroller = (ScrollView)findViewById(R.id.scroller);
        final Button bnLogin = (Button) findViewById(R.id.bnLogin);
        context = this.getApplicationContext();
        bnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorInfo.setText("");
                investorID = userNameText.getText().toString();
                password = passwordText.getText().toString();
                if (investorID.isEmpty() || password.isEmpty()) {
                    errorInfo.setText("账号或密码不能为空");
                } else {
                    new TraderApiThread(context,investorID,password).start();

                    //滚动的时候，向下focus
                    scroller.post(new Runnable() {
                        public void run() {
                            scroller.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }
        });
    }

    //加了一个线程类
    class TraderApiThread extends Thread {

        private Context context;
        private String investorID;
        private String password;

        public TraderApiThread(Context context,String investorID,String password) {
            this.context = context;
            this.investorID = investorID;
            this.password = password;
        }

        @Override
        public void run() {
            MyAPI myAPI = new MyAPI(context);
            myAPI.initTradeApi(investorID,password);
        }
    }
}
