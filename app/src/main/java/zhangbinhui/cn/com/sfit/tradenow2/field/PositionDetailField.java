package zhangbinhui.cn.com.sfit.tradenow2.field;

/**
 * Created by zhang.binhui on 2015-12-16.
 * 只能怪API提供的持仓明细Field不能直接使用，所以需要自己新建一个
 */
public class PositionDetailField {
    String instrumentID;
    String brokerID;
    String investorID;
    char hedgeFlag; //投机 '1' 套利 '2' 套保 '3' 做市商 '5'
    char direction; //0：多；1：空
    String openDate;
    double closeAmout;
    int closeVolume;
    String combInstrumentID;
    String exchangeID;
    double exchMargin;
    double lastSettlementPrice;
    double margin;
    double marginRateByMoney;
    double marginRateByVolume;
    double openPrice;
    double positionProfitByDate; //逐日盈亏
    double positionProfitByTrade; //逐笔盈亏
    int settlementID;
    double settlementPrice;
    String tradeID;
    char tradeType;
    String tradingDay;
    int volume;


    public String getInstrumentID() {
        return instrumentID;
    }

    public void setInstrumentID(String instrumentID) {
        this.instrumentID = instrumentID;
    }

    public String getBrokerID() {
        return brokerID;
    }

    public void setBrokerID(String brokerID) {
        this.brokerID = brokerID;
    }

    public String getInvestorID() {
        return investorID;
    }

    public void setInvestorID(String investorID) {
        this.investorID = investorID;
    }

    public char getHedgeFlag() {
        return hedgeFlag;
    }

    public void setHedgeFlag(char hedgeFlag) {
        this.hedgeFlag = hedgeFlag;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public double getCloseAmout() {
        return closeAmout;
    }

    public void setCloseAmout(double closeAmout) {
        this.closeAmout = closeAmout;
    }

    public int getCloseVolume() {
        return closeVolume;
    }

    public void setCloseVolume(int closeVolume) {
        this.closeVolume = closeVolume;
    }

    public String getCombInstrumentID() {
        return combInstrumentID;
    }

    public void setCombInstrumentID(String combInstrumentID) {
        this.combInstrumentID = combInstrumentID;
    }

    public String getExchangeID() {
        return exchangeID;
    }

    public void setExchangeID(String exchangeID) {
        this.exchangeID = exchangeID;
    }

    public double getExchMargin() {
        return exchMargin;
    }

    public void setExchMargin(double exchMargin) {
        this.exchMargin = exchMargin;
    }

    public double getLastSettlementPrice() {
        return lastSettlementPrice;
    }

    public void setLastSettlementPrice(double lastSettlementPrice) {
        this.lastSettlementPrice = lastSettlementPrice;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public double getMarginRateByMoney() {
        return marginRateByMoney;
    }

    public void setMarginRateByMoney(double marginRateByMoney) {
        this.marginRateByMoney = marginRateByMoney;
    }

    public double getMarginRateByVolume() {
        return marginRateByVolume;
    }

    public void setMarginRateByVolume(double marginRateByVolume) {
        this.marginRateByVolume = marginRateByVolume;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getPositionProfitByDate() {
        return positionProfitByDate;
    }

    public void setPositionProfitByDate(double positionProfitByDate) {
        this.positionProfitByDate = positionProfitByDate;
    }

    public double getPositionProfitByTrade() {
        return positionProfitByTrade;
    }

    public void setPositionProfitByTrade(double positionProfitByTrade) {
        this.positionProfitByTrade = positionProfitByTrade;
    }

    public int getSettlementID() {
        return settlementID;
    }

    public void setSettlementID(int settlementID) {
        this.settlementID = settlementID;
    }

    public double getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(double settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    public String getTradeID() {
        return tradeID;
    }

    public void setTradeID(String tradeID) {
        this.tradeID = tradeID;
    }

    public char getTradeType() {
        return tradeType;
    }

    public void setTradeType(char tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradingDay() {
        return tradingDay;
    }

    public void setTradingDay(String tradingDay) {
        this.tradingDay = tradingDay;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
