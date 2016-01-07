package zhangbinhui.cn.com.sfit.tradenow2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.sfit.ctp.thosttraderapi.CThostFtdcQryTradingAccountField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zhangbinhui.cn.com.sfit.tradenow2.adapter.MyMdBaseAdapter;
import zhangbinhui.cn.com.sfit.tradenow2.field.DepthMarketDataField;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static {
        try {
            System.loadLibrary("thosttraderapi");
            System.loadLibrary("thostmduserapi");
            System.loadLibrary("thosttraderapi_wrap");
            System.loadLibrary("thostmduserapi_wrap");
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static TableLayout table = null;
    public static Snackbar mSnackBar = null;

    Context mainActivityContext = null;
//写成static方便在别的类里面调用，修改
    public static List<DepthMarketDataField> mdList;
    public static MyMdBaseAdapter myMdAdapter;
    public static ListView md_listView;
    RelativeLayout mHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityContext = this.getApplicationContext();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        final String brokerID = bundle.getString("brokerID");
        final String investorID = bundle.getString("investorId");
        final String currencyID = "CNY";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                mSnackBar = Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT);
                mSnackBar.setAction("明细", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(mainActivityContext, PositionActivity.class);
                        //加上这个Flag才不会报错
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mainActivityContext.startActivity(intent);
                    }
                });

                //查询资金

                CThostFtdcQryTradingAccountField tradingAccount = new CThostFtdcQryTradingAccountField();
                tradingAccount.setBrokerID(brokerID);
                tradingAccount.setInvestorID(investorID);
                tradingAccount.setCurrencyID(currencyID);
                MyAPI.traderApi.ReqQryTradingAccount(tradingAccount, 0);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



//--------------之前的写法，用tableRow来画初始行情--------------------
        /*
        table = (TableLayout)findViewById(R.id.table);
        ArrayList<String> instrumentArray = bundle.getStringArrayList("instrument");
        //初始化行情界面，把所有合约ID先填上去
        for(int i=0;i<instrumentArray.size();i++){
            row = new TableRow(this);
            row.setPadding(6, 1, 6, 1);
            row.setGravity(Gravity.CENTER);

            text_instrumentID= new TextView(this);
            text_instrumentID.setGravity(Gravity.CENTER);//文本居中
            text_instrumentID.setTextSize((float) 18);//文本大小
            text_instrumentID.setPadding(15, 2, 15, 2);//边框左、上、右、下
            text_instrumentID.setText(instrumentArray.get(i));

            text_lastPrice = new TextView(this);
            text_lastPrice.setGravity(Gravity.CENTER);//文本居中
            text_lastPrice.setTextSize((float) 18);//文本大小
            text_lastPrice.setPadding(15, 2, 15, 2);//边框左、上、右、下
            text_lastPrice.setText("-");

            text_up_down = new TextView(this);
            text_up_down.setGravity(Gravity.CENTER);//文本居中
            text_up_down.setTextSize((float) 18);//文本大小
            text_up_down.setPadding(15, 2, 15, 2);
            text_up_down.setText("-");

            text_openInterest = new TextView(this);
            text_openInterest.setGravity(Gravity.CENTER);//文本居中
            text_openInterest.setTextSize((float) 18);//文本大小
            text_openInterest.setPadding(15, 2, 15, 2);//边框左、上、右、下
            text_openInterest.setText("-");

            row.addView(text_instrumentID);
            row.addView(text_lastPrice);
            row.addView(text_up_down);
            row.addView(text_openInterest);

            instrumentIndex.put(instrumentArray.get(i),i+1);
            MainActivity.table.addView(row);
        }
        new MyApiThread(this,instrumentIndex).start();
        */
//------------------------------------------------------------------

        //这里开始尝试用ListView实现行情界面

//        LayoutInflater mInflater = getLayoutInflater();
        //inflate 的意思是实例化一个布局
//        View content_main = mInflater.inflate(R.layout.content_main, null);
//        ListView md_listView = (ListView)content_main.findViewById(R.id.md_listView);

        //网上找的方法，ListView 实现左右、上下均可滚动
        mHead = (RelativeLayout) findViewById(R.id.head);
        mHead.setFocusable(true);
        mHead.setClickable(true);
        mHead.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSelected));
        mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

        //这里他妈的一定要注意，下面这个实例化和上面实例化的区别，content_main不是真正的父容器

        md_listView = (ListView)findViewById(R.id.md_listView);
        md_listView.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

        mdList = new ArrayList<DepthMarketDataField>();

        ArrayList<String> instrumentArray = bundle.getStringArrayList("instrument");
        HashMap<String,Integer> instrumentMap = new HashMap<String,Integer>();

        DepthMarketDataField marketDataField;
        for(int i=0;i<instrumentArray.size();i++){
            //初始化行情数据为0；
            marketDataField = new DepthMarketDataField();
            marketDataField.setInstrumentID(instrumentArray.get(i));
            marketDataField.setLastPrice(0);
            marketDataField.setUp_down(0);
            marketDataField.setOpenInterest(0);
            marketDataField.setBidPrice1(0);
            marketDataField.setBidVolume1(0);
            marketDataField.setAskPrice1(0);
            marketDataField.setAskVolume1(0);
            mdList.add(marketDataField);

            //把ArrayList转换成Map，更容易查找
            instrumentMap.put(instrumentArray.get(i),i);
        }

        myMdAdapter = new MyMdBaseAdapter(this,R.layout.md_model_for_listview,mdList,mHead);

        md_listView.setAdapter(myMdAdapter);

        md_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myMdAdapter.setSelectedPosition(position);
                myMdAdapter.notifyDataSetChanged();
            }
        });
        //------------------------------------------------------------
        new MyApiThread(this,instrumentMap).start();
    }

    class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            //当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
            HorizontalScrollView headSrcrollView =
                    (HorizontalScrollView) mHead.findViewById(R.id.horizontalScrollView1);
            headSrcrollView.onTouchEvent(arg1);
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //加了一个线程类
    class MyApiThread extends Thread {
        private Context context;
//        private HashMap<String,Integer> instrumentIndex;
//        public MyApiThread(Context context,HashMap<String,Integer> instrumentIndex) {
//            this.context = context;
//            this.instrumentIndex = instrumentIndex;
//        }
//
//        @Override
//        public void run() {
//            //把当前的Context传过去
//            MyAPI myAPI = new MyAPI(context,instrumentIndex);
//            myAPI.initMdApi();
//        }
        private HashMap<String,Integer> instrumentMap;
        public MyApiThread(Context context,HashMap<String,Integer> instrumentMap){
            this.context = context;
            this.instrumentMap = instrumentMap;
        }
        @Override
        public void run() {
            //把当前的Context传过去
            MyAPI myAPI = new MyAPI(context,instrumentMap);
            myAPI.initMdApi();
        }
    }
}
