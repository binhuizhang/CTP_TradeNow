package zhangbinhui.cn.com.sfit.tradenow2;


import android.os.Bundle;
import android.os.Message;

import com.sfit.ctp.thostmduserapi.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import zhangbinhui.cn.com.sfit.tradenow2.field.DepthMarketDataField;
import zhangbinhui.cn.com.sfit.tradenow2.field.InstrumentField;
import zhangbinhui.cn.com.sfit.tradenow2.field.PositionDetailField;

/**
 * Created by zhang.binhui on 2015-12-02.
 */
public class MdSpi extends CThostFtdcMdSpi{

    CThostFtdcMdApi mdApi = null;
    MyHandler myhandler = null;
    String userId = "012207";
    String password = "pa55word";
    int nRequestId = 0;
    Bundle bundle = new Bundle();

    public MdSpi(CThostFtdcMdApi mdApi,MyHandler myhandler) {
        this.mdApi = mdApi;
        this.myhandler = myhandler;
    }

    @Override
    public void OnFrontConnected() {

        CThostFtdcReqUserLoginField pReqUserLoginField = new CThostFtdcReqUserLoginField();
        pReqUserLoginField.setBrokerID(MyAPI.brokerId);
        pReqUserLoginField.setUserID(userId);
        pReqUserLoginField.setPassword(password);
        nRequestId++;
        mdApi.ReqUserLogin(pReqUserLoginField,nRequestId);

        //构建Message对象
        //第一个参数：是自己指定的message代号，方便在handler选择性地接收
        //第二三个参数没有什么意义
        //第四个参数需要封装的对象
    }

    @Override
    public void OnFrontDisconnected(int nReason) {
        Message msg = myhandler.obtainMessage(myhandler.getMD_FRONT_CONNFAILD(),1,1,nReason);
        myhandler.sendMessage(msg); //发送消息
    }


    @Override
    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        //行情登录成功后，就开始请求订阅行情
//        ArrayList<String> instrumentArray = myhandler.instrumentArray;
//        int nCount = instrumentArray.size();
//        String[] ppInstrumentID = new String[nCount];
//        Iterator iter = instrumentArray.iterator();
//        int index = 0;
//        while (iter.hasNext()) {
//            ppInstrumentID[index] = iter.next().toString();
//            index++;
//        }
//
//        mdApi.SubscribeMarketData(ppInstrumentID, nCount);

//        HashMap<String,Integer> instrumentArray = myhandler.getInstrumentIndex();
//        int nCount = instrumentArray.size();
//        String[] ppInstrumentID = new String[nCount];
//        Iterator iter = instrumentArray.entrySet().iterator();
//        int index = 0;
//        while (iter.hasNext()) {
//            Map.Entry entry = (Map.Entry) iter.next();
//            ppInstrumentID[index] = entry.getKey().toString();
//            index++;
//        }
//
//        mdApi.SubscribeMarketData(ppInstrumentID, nCount);

        //把每一个合约单独请求
        HashMap<String,Integer> instrumentMap = myhandler.getInstrumentMap();
        String[] ppInstrumentID = new String[1];
        Iterator iter = instrumentMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            ppInstrumentID[0] = entry.getKey().toString();
            mdApi.SubscribeMarketData(ppInstrumentID, 1);
        }
    }

    @Override
    public void OnRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        Message msg = myhandler.obtainMessage(myhandler.getMD_RSP_ERROR(),1,1,"行情处理有误\n");
        myhandler.sendMessage(msg); //发送消息
    }

    @Override
    public void OnRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        super.OnRspSubMarketData(pSpecificInstrument, pRspInfo, nRequestID, bIsLast);
        //如果订阅行情返回成功，则每半秒刷新界面
//        if(pRspInfo == null || pRspInfo.getErrorID()==0){
//            myRefreshThread = new RefreshThread(0);
//            myRefreshThread.start();
//        }else{
//            if(myRefreshThread.flag == 1){
//                myRefreshThread = null;
//            }
//        }
    }

    @Override
    public void OnRspUnSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        super.OnRspUnSubMarketData(pSpecificInstrument, pRspInfo, nRequestID, bIsLast);
    }

    @Override
    public void OnRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {

        //直接在这里赋值，省去跳转的过程，优化
        DepthMarketDataField md_row;
        int md_index = myhandler.getInstrumentMap().get(pDepthMarketData.getInstrumentID());
        md_row = (DepthMarketDataField)MainActivity.mdList.get(md_index);
        md_row.setLastPrice(pDepthMarketData.getLastPrice());
        md_row.setUp_down(pDepthMarketData.getLastPrice() - pDepthMarketData.getPreSettlementPrice());
        md_row.setOpenInterest(pDepthMarketData.getOpenInterest());
        md_row.setAskPrice1(pDepthMarketData.getAskPrice1());
        md_row.setAskVolume1(pDepthMarketData.getAskVolume1());
        md_row.setBidPrice1(pDepthMarketData.getBidPrice1());
        md_row.setBidVolume1(pDepthMarketData.getBidVolume1());
        md_row.setUpperLimitPrice(pDepthMarketData.getUpperLimitPrice());

        Message msg = myhandler.obtainMessage(myhandler.getMD_SUB(),1,1,0);
        myhandler.sendMessage(msg); //发送消息

        if(TraderSpi.positionIndexMap.size()!=0){

            String longPosition = (md_row.getInstrumentID()+"0").toString();
            String shortPosition = (md_row.getInstrumentID()+"1").toString();

            //多头持仓合约的行情来了
            if(TraderSpi.positionIndexMap.get(longPosition)!=null){
                int positioIndex = TraderSpi.positionIndexMap.get(longPosition);
                PositionDetailField positionDetail = TraderSpi.positionsDetails_show.get(positioIndex);
                InstrumentField instrumentField = TraderSpi.pInstrumentMap.get(md_row.getInstrumentID());
                double positionProfitByTrade = (md_row.getLastPrice() - positionDetail.getOpenPrice())
                        *positionDetail.getVolume()*instrumentField.getVolumeMultiple();

                positionDetail.setPositionProfitByTrade(positionProfitByTrade);
                Message msg1 = myhandler.obtainMessage(myhandler.getTRADE_UPDATE_POSITION_BY_MD(),1,1,0);
                myhandler.sendMessage(msg1); //发送消息
            }
            //空头持仓合约的行情来了
            if(TraderSpi.positionIndexMap.get(shortPosition)!=null){
                int positioIndex_short = TraderSpi.positionIndexMap.get(shortPosition);
                PositionDetailField positionDetail_short = TraderSpi.positionsDetails_show.get(positioIndex_short);
                InstrumentField instrumentField_short = TraderSpi.pInstrumentMap.get(md_row.getInstrumentID());
                double positionProfitByTrade = (positionDetail_short.getOpenPrice() - md_row.getLastPrice())
                        *positionDetail_short.getVolume()*instrumentField_short.getVolumeMultiple();

                positionDetail_short.setPositionProfitByTrade(positionProfitByTrade);

                Message msg2 = myhandler.obtainMessage(myhandler.getTRADE_UPDATE_POSITION_BY_MD(),1,1,0);
                myhandler.sendMessage(msg2); //发送消息
            }
        }
    }

    //android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
//    class RefreshThread extends Thread {
//        int flag;
//        public RefreshThread(int flag){
//            this.flag = flag;
//        }
//        @Override
//        public void run() {
//            flag = 1;
//            try{
//                while(true){
//                    Thread.sleep(500);
//                    MainActivity.myMdAdapter.notifyDataSetChanged();
//                }
//            }catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

}
