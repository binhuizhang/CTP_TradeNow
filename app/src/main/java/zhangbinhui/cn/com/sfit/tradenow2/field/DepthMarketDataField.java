package zhangbinhui.cn.com.sfit.tradenow2.field;

/**
 * Created by zhang.binhui on 2015-12-16.
 */
public class DepthMarketDataField {
    double askPrice1; //卖一
    int askVolume1;
    double bidPrice1; //买一
    int bidVolume1;
    double averagePrice;//当日均价
    double closePrice; //收盘价
    String exchangeID;
    double highestPrice;
    String instrumentID;
    double lastPrice;
    double lowerLimitPrice; //跌停价
    double lowestPrice;
    double openInterest; //持仓量
    double openPrice;
    double preClosePirce;
    double preSettlementPrice;
    double settlementPrice;
    String tradingDay;
    double turnover; //成交金额
    int updateMillisec; //最后修改毫秒
    String updateTime; //最后修改时间
    double upperLimitPrice; //涨停价
    double up_down; //自定义的涨跌


    public double getUp_down() {
        return up_down;
    }

    public void setUp_down(double up_down) {
        this.up_down = up_down;
    }

    public double getAskPrice1() {
        return askPrice1;
    }

    public void setAskPrice1(double askPrice1) {
        this.askPrice1 = askPrice1;
    }

    public int getAskVolume1() {
        return askVolume1;
    }

    public void setAskVolume1(int askVolume) {
        this.askVolume1 = askVolume;
    }

    public double getBidPrice1() {
        return bidPrice1;
    }

    public void setBidPrice1(double bidPrice1) {
        this.bidPrice1 = bidPrice1;
    }

    public int getBidVolume1() {
        return bidVolume1;
    }

    public void setBidVolume1(int bidVolume1) {
        this.bidVolume1 = bidVolume1;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public String getExchangeID() {
        return exchangeID;
    }

    public void setExchangeID(String exchangeID) {
        this.exchangeID = exchangeID;
    }

    public double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public String getInstrumentID() {
        return instrumentID;
    }

    public void setInstrumentID(String instrumentID) {
        this.instrumentID = instrumentID;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getLowerLimitPrice() {
        return lowerLimitPrice;
    }

    public void setLowerLimitPrice(double lowerLimitPrice) {
        this.lowerLimitPrice = lowerLimitPrice;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public double getOpenInterest() {
        return openInterest;
    }

    public void setOpenInterest(double openInterest) {
        this.openInterest = openInterest;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getPreClosePirce() {
        return preClosePirce;
    }

    public void setPreClosePirce(double preClosePirce) {
        this.preClosePirce = preClosePirce;
    }

    public double getPreSettlementPrice() {
        return preSettlementPrice;
    }

    public void setPreSettlementPrice(double preSettlementPrice) {
        this.preSettlementPrice = preSettlementPrice;
    }

    public double getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(double settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    public String getTradingDay() {
        return tradingDay;
    }

    public void setTradingDay(String tradingDay) {
        this.tradingDay = tradingDay;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public int getUpdateMillisec() {
        return updateMillisec;
    }

    public void setUpdateMillisec(int updateMillisec) {
        this.updateMillisec = updateMillisec;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public double getUpperLimitPrice() {
        return upperLimitPrice;
    }

    public void setUpperLimitPrice(double upperLimitPrice) {
        this.upperLimitPrice = upperLimitPrice;
    }
}
