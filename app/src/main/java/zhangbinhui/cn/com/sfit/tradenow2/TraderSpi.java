package zhangbinhui.cn.com.sfit.tradenow2;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import com.sfit.ctp.thosttraderapi.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import zhangbinhui.cn.com.sfit.tradenow2.adapter.MyBaseAdapter;
import zhangbinhui.cn.com.sfit.tradenow2.field.InstrumentField;
import zhangbinhui.cn.com.sfit.tradenow2.field.PositionDetailField;


/**
 * Created by zhang.binhui on 2015-12-01.
 */

public class TraderSpi extends CThostFtdcTraderSpi {
    CThostFtdcTraderApi traderApi = null;
    MyHandler myhandler = null;

    public static String investorID = null;
    String password = null;
    int nRequestId = 0;
    Context context = null;
    ArrayList<String> pInstrumentArray = new ArrayList<String>();
    public static HashMap<String,InstrumentField> pInstrumentMap = new HashMap<String,InstrumentField>();

    //声明成静态的，可以省去传参数的步奏，但总感觉暂用内存，没时间考虑更好的实现方式
    public static List<PositionDetailField> positionLists =
            new ArrayList<PositionDetailField>();
    public static List<PositionDetailField> positionsDetails_show = new ArrayList<PositionDetailField>();
    public static HashMap<String,Integer> positionIndexMap = new HashMap<String,Integer>();

    DecimalFormat df = new DecimalFormat("#0.0");
    final String currencyID = "CNY";

    public void initspi(CThostFtdcTraderApi api, MyHandler myHandler,String userId,String password,Context loginContext) {
        this.traderApi = api;
        this.myhandler = myHandler;
        this.investorID = userId;
        this.password = password;
        this.context = loginContext;
    }


    @Override
    public void OnFrontConnected()
    {
        //构建Message对象
        //第一个参数：是自己指定的message代号，方便在handler选择性地接收
        //第二三个参数没有什么意义
        //第四个参数需要封装的对象
        Message msg = myhandler.obtainMessage(myhandler.getTRADE_FRONT_CONN(),1,1,"交易前置已经连接\n");
        myhandler.sendMessage(msg); //发送消息

        //请求登录
        CThostFtdcReqUserLoginField loginfld = new CThostFtdcReqUserLoginField();

        loginfld.setBrokerID(MyAPI.brokerId);
        loginfld.setUserID(investorID);
        loginfld.setPassword(password);
        loginfld.setUserProductInfo("android");
        nRequestId++;
        traderApi.ReqUserLogin(loginfld, nRequestId);
    }

    @Override
    public void OnFrontDisconnected(int nReason)
    {
        Message msg = myhandler.obtainMessage(myhandler.getTRADE_FRONT_CONNFAILD(),1,1,"交易前置连接失败\n");
        myhandler.sendMessage(msg); //发送消息
    }

    @Override
    public void OnRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
                               CThostFtdcRspInfoField pRspInfo,
                               int nRequestID, boolean bIsLast){
        if(pRspInfo.getErrorID()==0){
            Message msg = myhandler.obtainMessage(myhandler.getTRADE_USER_LOGIN(),1,1,"登入成功！\n");
            myhandler.sendMessage(msg); //发送消息MSG_LOGIN_SUCCESS

            //先请求查询历史持仓，初始化持仓
            reqQryPositionDetail();

        }else{
            Message msg = myhandler.obtainMessage(myhandler.getTRADE_USER_LOGIN_FAILD(),1,1,pRspInfo.getErrorMsg());
            myhandler.sendMessage(msg);
        }
    }

    public void reqQryPositionDetail(){

        CThostFtdcQryInvestorPositionDetailField pInvestorPositionDetail = new CThostFtdcQryInvestorPositionDetailField();
        pInvestorPositionDetail.setBrokerID(MyAPI.brokerId);
        pInvestorPositionDetail.setInvestorID(investorID);

        traderApi.ReqQryInvestorPositionDetail(pInvestorPositionDetail, nRequestId++);
    }

    public void reqQryInvestAccount(){

        CThostFtdcQryTradingAccountField tradingAccount = new CThostFtdcQryTradingAccountField();
        tradingAccount.setBrokerID(MyAPI.brokerId);
        tradingAccount.setInvestorID(investorID);
        tradingAccount.setCurrencyID(currencyID);
        traderApi.ReqQryTradingAccount(tradingAccount, nRequestId++);

    }

    public void reqQyrInstrument(){
        CThostFtdcQryInstrumentField pQryInstrument = new CThostFtdcQryInstrumentField();
        pQryInstrument.setExchangeID("SHFE");
        traderApi.ReqQryInstrument(pQryInstrument, nRequestId++);
    }

    @Override
    public void OnRspQryInvestorPosition(CThostFtdcInvestorPositionField pInvestorPosition,
                                         CThostFtdcRspInfoField pRspInfo,
                                         int nRequestID, boolean bIsLast) {
    }

    @Override
    public void OnRspQryInvestorPositionDetail(CThostFtdcInvestorPositionDetailField pInvestorPositionDetail,
                                               CThostFtdcRspInfoField pRspInfo,
                                               int nRequestID, boolean bIsLast){
        if(pRspInfo == null || pRspInfo.getErrorID()==0){
            //正常的返回
            if(bIsLast == false){
                //有多笔持仓分次返回
                PositionDetailField positionDetailField = new PositionDetailField();
                positionDetailField.setInstrumentID(pInvestorPositionDetail.getInstrumentID());
                positionDetailField.setDirection(pInvestorPositionDetail.getDirection());
                positionDetailField.setVolume(pInvestorPositionDetail.getVolume());
                positionDetailField.setOpenPrice(pInvestorPositionDetail.getOpenPrice());
                positionDetailField.setPositionProfitByTrade(pInvestorPositionDetail.getPositionProfitByTrade());
                positionDetailField.setOpenDate(pInvestorPositionDetail.getOpenDate());
                positionDetailField.setTradingDay(pInvestorPositionDetail.getTradingDay());
                positionDetailField.setLastSettlementPrice(pInvestorPositionDetail.getLastSettlementPrice());
                positionLists.add(positionDetailField);
            }else {
                //最后一笔持仓，或者也有可能持仓为空的情况
                if (pInvestorPositionDetail != null) {
                    //最后一笔持仓
                    PositionDetailField positionDetailField = new PositionDetailField();
                    positionDetailField.setInstrumentID(pInvestorPositionDetail.getInstrumentID());
                    positionDetailField.setDirection(pInvestorPositionDetail.getDirection());
                    positionDetailField.setVolume(pInvestorPositionDetail.getVolume());
                    positionDetailField.setOpenPrice(pInvestorPositionDetail.getOpenPrice());
                    positionDetailField.setPositionProfitByTrade(pInvestorPositionDetail.getPositionProfitByTrade());
                    positionDetailField.setOpenDate(pInvestorPositionDetail.getOpenDate());
                    positionDetailField.setTradingDay(pInvestorPositionDetail.getTradingDay());
                    positionDetailField.setLastSettlementPrice(pInvestorPositionDetail.getLastSettlementPrice());
                    positionLists.add(positionDetailField);
                }

                Message msg = myhandler.obtainMessage(myhandler.getTRADE_QRY_POSITION(),1,1,"初始化持仓数据\n");
                myhandler.sendMessage(msg);

                //查询有流控，必须停一秒才能发起第二笔查询
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //查询合约，初始化行情界面
                reqQyrInstrument();

            }

        }
    }

    //去除今仓
    public void removeTodayPoistion(List<PositionDetailField> positionLists){
        if(positionLists.size()!=0) {
            for (int i = 0; i < positionLists.size(); i++) {
                PositionDetailField positionDetail = positionLists.get(i);
                if (positionDetail.getOpenDate().equals(positionDetail.getTradingDay())) {
                    //把今仓去除
                    positionLists.remove(i);
                    //递归调用，因为删除一个之后，list的size发生了改变，原先的i已经不起作用
                    removeTodayPoistion(positionLists);
                }
            }
        }
    }
    //先将持仓明细改成一个HashMap<InstrumentID+direction,PositionDetailField>
    public HashMap<String,PositionDetailField> genPositionMap(List<PositionDetailField> positionLists){
        HashMap<String,PositionDetailField> positionMap = new HashMap<String,PositionDetailField>();
        for(int i=0;i<positionLists.size();i++){
            if(positionMap.size()==0){
                positionMap.put(positionLists.get(i).getInstrumentID()+positionLists.get(i).getDirection(),positionLists.get(i));
            }else{
                //如果查不到这个key，则直接添加，不需要汇总
                if(positionMap.get(positionLists.get(i).getInstrumentID()+positionLists.get(i).getDirection()) == null){
                    positionMap.put(positionLists.get(i).getInstrumentID()+positionLists.get(i).getDirection(),positionLists.get(i));
                }else{
                    PositionDetailField oldPosition = positionMap.get(positionLists.get(i).getInstrumentID()+positionLists.get(i).getDirection());
                    PositionDetailField newPosition = positionLists.get(i);
                    PositionDetailField lastPosition = new PositionDetailField();

                    int oldVolume = oldPosition.getVolume();
                    int newVolume = positionLists.get(i).getVolume();
                    double oldOpenprice = oldPosition.getOpenPrice();
                    double newOpenprice = positionLists.get(i).getOpenPrice();
                    int totalVolume = oldVolume+newVolume;
                    double totalCost = oldVolume * oldOpenprice + newVolume * newOpenprice;

                    lastPosition.setVolume(totalVolume);
                    lastPosition.setOpenPrice(totalCost / totalVolume);
                    //这里还缺少一个合约乘数
                    InstrumentField instrumentField = pInstrumentMap.get(oldPosition.getInstrumentID());
                    if(oldPosition.getDirection()=='0'){
                        lastPosition.setPositionProfitByTrade((oldPosition.getLastSettlementPrice() * totalVolume - totalCost)
                                *instrumentField.getVolumeMultiple());
                    }else{
                        lastPosition.setPositionProfitByTrade((totalCost - oldPosition.getLastSettlementPrice() * totalVolume)
                                *instrumentField.getVolumeMultiple());
                    }

                    lastPosition.setInstrumentID(oldPosition.getInstrumentID());
                    lastPosition.setDirection(oldPosition.getDirection());

                    //用旧的key，覆盖新的值
                    positionMap.put(lastPosition.getInstrumentID()+lastPosition.getDirection(),lastPosition);
                }
            }
        }
        return positionMap;
    }



    @Override
    public void OnRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount,
                                       CThostFtdcRspInfoField pRspInfo,
                                       int nRequestID, boolean bIsLast) {

        //这里的pRspInfo无论返回结果是正确与否，返回值都为空
        Bundle bundle = new Bundle();
        bundle.putDouble("balance", pTradingAccount.getBalance());
        bundle.putDouble("currenMargin", pTradingAccount.getCurrMargin());
        bundle.putDouble("available", pTradingAccount.getAvailable());

        Message msg = myhandler.obtainMessage(myhandler.getTRADE_QRY_ACCOUNT(), 1, 1,bundle);
        myhandler.sendMessage(msg);
    }
    InstrumentField instrument;
    @Override
    public void OnRspQryInstrument(CThostFtdcInstrumentField pInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {

        if(bIsLast==false){
            pInstrumentArray.add(pInstrument.getInstrumentID());

            instrument = new InstrumentField();
            instrument.setInstrumentID(pInstrument.getInstrumentID());
            instrument.setExchangeID(pInstrument.getExchangeID());
            instrument.setLongMarginRatio(pInstrument.getLongMarginRatio());
            instrument.setShortMarginRatio(pInstrument.getShortMarginRatio());
            instrument.setMaxMarginSideAlgorithm(pInstrument.getMaxMarginSideAlgorithm());
            instrument.setPriceTick(pInstrument.getPriceTick());
            instrument.setVolumeMultiple(pInstrument.getVolumeMultiple()); //合约乘数

            pInstrumentMap.put(pInstrument.getInstrumentID(),instrument);

        }else {
            pInstrumentArray.add(pInstrument.getInstrumentID());
            Message msg = myhandler.obtainMessage(myhandler.getTRADE_QRY_INSTRUMENT(),1,1,"初始化行情数据\n");
            myhandler.sendMessage(msg); //发送消息MSG_LOGIN_SUCCESS

            instrument = new InstrumentField();
            instrument.setInstrumentID(pInstrument.getInstrumentID());
            instrument.setExchangeID(pInstrument.getExchangeID());
            instrument.setLongMarginRatio(pInstrument.getLongMarginRatio());
            instrument.setShortMarginRatio(pInstrument.getShortMarginRatio());
            instrument.setMaxMarginSideAlgorithm(pInstrument.getMaxMarginSideAlgorithm());
            instrument.setPriceTick(pInstrument.getPriceTick());
            instrument.setVolumeMultiple(pInstrument.getVolumeMultiple()); //合约乘数

            pInstrumentMap.put(pInstrument.getInstrumentID(), instrument);

            //拿到合约数据了，才能知道合约乘数，这个时候将持仓明细汇总
            if(positionLists.size()!=0){
                //只留下昨仓
                removeTodayPoistion(positionLists);
                //先将持仓明细改成一个HashMap<InstrumentID+direction,PositionDetailField>
                HashMap<String, PositionDetailField> positionMap = genPositionMap(positionLists);
                //遍历Map，组成新的List,这种遍历方法效率更高
                Iterator iter = positionMap.entrySet().iterator();
                int index = 0;
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    //这个list用于adapter的绑定，展示在前台界面
                    positionsDetails_show.add((PositionDetailField) entry.getValue());
                    //这个map用于记录每个positionDetailfield在list中的位置，到时候直接修改
                    //entry.getKey().toString()包含了合约id和方向0或者1
                    positionIndexMap.put(entry.getKey().toString(), index);
//                    String instrument_id = entry.getKey().toString().substring(0,entry.getKey().toString().length()-1);
//                    positionIndexMap.put(instrument_id, index);
                    index++;
                }
            }

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("instrument", pInstrumentArray);
            bundle.putString("brokerID",MyAPI.brokerId);
            bundle.putString("investorId",investorID);

            //从LoginActivity 转到 MainActivity
            intentMainActivity(bundle);
        }
    }

    public void intentMainActivity(Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        //加上这个Flag才不会报错
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }



}

