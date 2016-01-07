package zhangbinhui.cn.com.sfit.tradenow2;


import android.content.Context;
import android.os.Looper;

import com.sfit.ctp.thostmduserapi.CThostFtdcMdApi;
import com.sfit.ctp.thosttraderapi.CThostFtdcTraderApi;
import com.sfit.ctp.thosttraderapi.THOST_TE_RESUME_TYPE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhang.binhui on 2015-12-04.
 */
public class MyAPI {

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


    public static CThostFtdcTraderApi traderApi = null;
    public static CThostFtdcMdApi mdApi = null;
    private Context context;
    //simnow 地址
    private final String TradeFrontIp = "tcp://180.168.146.187:10000";
    private final String MdFrontIp = "tcp://180.168.146.187:10010";
    public static String brokerId = "9999";

    //东航主席地址
//    private final String TradeFrontIp = "tcp://101.230.192.178:41205";
//    private final String MdFrontIp = "tcp://101.230.192.178:41213";
//    public static String brokerId = "7070";
    String apppath = null;
    //初始化handler
    //主线程的Looper对象
    //这里以主线程的Looper对象创建了handler，
    //所以，这个handler发送的Message会被传递给主线程的MessageQueue。
    MyHandler myHandler = null;

    public MyAPI(Context context){
        this.context = context;
        apppath = context.getFilesDir().getAbsolutePath()+"/";
        myHandler = new MyHandler(Looper.getMainLooper(),context);
    }
//    public MyAPI(Context context,HashMap<String,Integer> instrumentIndex){
//        this.context = context;
//        apppath = context.getFilesDir().getAbsolutePath()+"/";
//        this.instrumentIndex = instrumentIndex;
//        myHandler = new MyHandler(Looper.getMainLooper(),context);
//        myHandler.setInstrumentIndex(instrumentIndex);
//    }

    public MyAPI(Context context,HashMap<String,Integer> instrumentMap){
        this.context = context;
        apppath = context.getFilesDir().getAbsolutePath()+"/";
        myHandler = new MyHandler(Looper.getMainLooper(),context);
        myHandler.setInstrumentMap(instrumentMap);
    }

    public CThostFtdcTraderApi getTraderApi() {
        return traderApi;
    }

    public CThostFtdcMdApi getMdApi() {
        return mdApi;
    }
    public void setMdApi(CThostFtdcMdApi mdApi) {
        this.mdApi = mdApi;
    }

    public void setTraderApi(CThostFtdcTraderApi traderApi) {
        this.traderApi = traderApi;
    }



    public void initTradeApi(String investorID,String password){
        try {
            //交易api初始化
            traderApi = CThostFtdcTraderApi.CreateFtdcTraderApi(apppath);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        //交易traderSpi初始化
        TraderSpi traderSpi = new TraderSpi();

        traderSpi.initspi(traderApi, myHandler,investorID,password,context);
        traderApi.RegisterSpi(traderSpi);
        traderApi.RegisterFront(TradeFrontIp);

        traderApi.SubscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESUME);
        traderApi.SubscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_RESUME);
        setTraderApi(traderApi);

        traderApi.Init();
        traderApi.Join();
    }

    public void initMdApi(){
        try {
            //行情api初始化
            mdApi = CThostFtdcMdApi.CreateFtdcMdApi(apppath);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        //行情MdSpi初始化
        MdSpi mdSpi = new MdSpi(mdApi, myHandler);
        mdApi.RegisterSpi(mdSpi);
        mdApi.RegisterFront(MdFrontIp);
        setMdApi(mdApi);

        mdApi.Init();
        mdApi.Join();
    }
}
