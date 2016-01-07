package zhangbinhui.cn.com.sfit.tradenow2;


import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by zhang.binhui on 2015-12-04.
 */
public class MyHandler extends Handler {

    private final int MD_FRONT_CONN = 0;// 连接行情前置成功
    private final int MD_FRONT_CONNFAILD = 1;// 连接行情前置失败
    private final int MD_USER_LOGIN = 2;// 行情前置登录
    private final int MD_SUB = 3;//订阅行情
    private final int MD_RSP_ERROR = 4;//行情报错
    private final int TRADE_FRONT_CONN = 5;//连接交易前置成功
    private final int TRADE_FRONT_CONNFAILD = 6;// 连接交易前置失败
    private final int TRADE_USER_LOGIN = 7;// 交易前置登录
    private final int TRADE_USER_LOGIN_FAILD = 8;//交易前置登录失败
    private final int TRADE_QRY_INSTRUMENT = 9; //查询合约成功
    private final int TRADE_QRY_ACCOUNT = 10; //查询资金成功
    private final int TRADE_QRY_POSITION = 11;//查询持仓
    private final int TRADE_QRY_POSITION_NULL = 12;//持仓为空
    private final int TRADE_UPDATE_POSITION_BY_MD = 13; //根据行情更新持仓盈亏

    HashMap<String,Integer> instrumentMap = null;

    public int getMD_FRONT_CONN() {
        return MD_FRONT_CONN;
    }

    public int getMD_FRONT_CONNFAILD() {
        return MD_FRONT_CONNFAILD;
    }

    public int getMD_USER_LOGIN(){
        return MD_USER_LOGIN;
    }

    public int getMD_SUB(){
        return MD_SUB;
    }

    public int getMD_RSP_ERROR(){
        return MD_RSP_ERROR;
    }

    public int getTRADE_FRONT_CONN(){
        return TRADE_FRONT_CONN;
    }

    public int getTRADE_FRONT_CONNFAILD(){
        return TRADE_FRONT_CONNFAILD;
    }

    public int getTRADE_USER_LOGIN(){
        return TRADE_USER_LOGIN;
    }

    public int getTRADE_USER_LOGIN_FAILD(){
        return TRADE_USER_LOGIN_FAILD;
    }

    public int getTRADE_QRY_INSTRUMENT(){
        return TRADE_QRY_INSTRUMENT;
    }

    public int getTRADE_QRY_ACCOUNT(){
        return TRADE_QRY_ACCOUNT;
    }

    public int getTRADE_QRY_POSITION(){
        return TRADE_QRY_POSITION;
    }

    public int getTRADE_QRY_POSITION_NULL(){
        return TRADE_QRY_POSITION_NULL;
    }

    public int getTRADE_UPDATE_POSITION_BY_MD(){
        return TRADE_UPDATE_POSITION_BY_MD;
    }

    private Context mainActivityContext;
    private List<Map<String, String>> positionLists;

    DecimalFormat df = new DecimalFormat("#0.0");
    public long md_StartTime;
    public long pos_StartTime;

    public MyHandler(Looper looper,Context context) {

        super(looper);
        this.mainActivityContext = context;
    }


    public HashMap<String, Integer> getInstrumentMap() {
        return instrumentMap;
    }

    public void setInstrumentMap(HashMap<String, Integer> instrumentMap) {
        this.instrumentMap = instrumentMap;
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case TRADE_FRONT_CONN:
                LoginActivity.errorInfo.append(msg.obj.toString());
                break;
            case TRADE_FRONT_CONNFAILD:
                LoginActivity.errorInfo.append(msg.obj.toString());
                break;
            case TRADE_USER_LOGIN:
                LoginActivity.errorInfo.append(msg.obj.toString());
                break;
            case TRADE_USER_LOGIN_FAILD:
                LoginActivity.errorInfo.append(msg.obj.toString());
                break;
            case TRADE_QRY_INSTRUMENT:
                LoginActivity.errorInfo.append(msg.obj.toString());
                break;
            case TRADE_QRY_ACCOUNT:
                Bundle bundle1 =  (Bundle)msg.obj;
                Double balance = bundle1.getDouble("balance");
                Double currenMargin = bundle1.getDouble("currenMargin");
                Double available = bundle1.getDouble("available");
                String accountInfo = "权益: "+df.format(balance)+" "
                        +"保证金: "+df.format(currenMargin)+"\n"
                        +"可用: "+df.format(available);
                MainActivity.mSnackBar.setText(accountInfo);
                MainActivity.mSnackBar.show();
                break;
            case MD_FRONT_CONN:
//                MainActivity.md_data.append(msg.obj.toString());
                break;
            case MD_FRONT_CONNFAILD:
//                MainActivity.md_data.append("行情断开，错误码："+msg.obj.toString()+"\n");
                break;
            case MD_USER_LOGIN:
                md_StartTime = msg.getWhen();
                pos_StartTime = msg.getWhen();
                break;
            case MD_SUB:
                //这里一定要注意，bundle如果是putDouble，对应的也要用getDouble，否则取不出数据
                //第二个要注意：textView需要设置字体颜色等属性，否则也显示不出来；
                //第三个要注意：每一个作为children的view都不能复用，必须重新new一个；
//                Bundle bundle =  (Bundle)msg.obj;
//                String instrumentId = bundle.getString("instrumentId");
//                double lastPrice = bundle.getDouble("lastPrice");
//                int askVolume1 = bundle.getInt("askVolume1");

                long updateTime = msg.getWhen();
                if(updateTime - md_StartTime >1000){
                    MainActivity.myMdAdapter.notifyDataSetChanged();
                    md_StartTime = updateTime;
                }
                break;
            case TRADE_QRY_POSITION:
                LoginActivity.errorInfo.append(msg.obj.toString());
                break;
            case TRADE_QRY_POSITION_NULL:
                LoginActivity.errorInfo.append(msg.obj.toString());
                break;
            case TRADE_UPDATE_POSITION_BY_MD:
                //只有在myBaseAdapter初始化之后，才能刷新
                if(PositionActivity.myBaseAdapter!=null){

                    long pos_updateTime = msg.getWhen();
                    if(pos_updateTime - pos_StartTime >1000){
                        PositionActivity.myBaseAdapter.notifyDataSetChanged();
                        pos_StartTime = pos_updateTime;
                    }
                }
                break;
            case MD_RSP_ERROR:
//                MainActivity.md_data.append(msg.obj.toString());
                break;
        }
    }
}

