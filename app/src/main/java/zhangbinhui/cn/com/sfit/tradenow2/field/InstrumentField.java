package zhangbinhui.cn.com.sfit.tradenow2.field;

/**
 * Created by zhang.binhui on 2015-12-18.
 */
public class InstrumentField {
    String exchangeID;
    String instrumentID;
    int volumeMultiple; //合约乘数
    double priceTick; //最小变动价位
    double longMarginRatio; // 多头保证金率
    double shortMarginRatio; // 空头保证金率
    char maxMarginSideAlgorithm; //是否使用大额单边保证金算法 0:不使用，1使用


    public String getExchangeID() {
        return exchangeID;
    }

    public void setExchangeID(String exchangeID) {
        this.exchangeID = exchangeID;
    }

    public String getInstrumentID() {
        return instrumentID;
    }

    public void setInstrumentID(String instrumentID) {
        this.instrumentID = instrumentID;
    }

    public int getVolumeMultiple() {
        return volumeMultiple;
    }

    public void setVolumeMultiple(int volumeMultiple) {
        this.volumeMultiple = volumeMultiple;
    }

    public double getPriceTick() {
        return priceTick;
    }

    public void setPriceTick(double priceTick) {
        this.priceTick = priceTick;
    }

    public double getLongMarginRatio() {
        return longMarginRatio;
    }

    public void setLongMarginRatio(double longMarginRatio) {
        this.longMarginRatio = longMarginRatio;
    }

    public double getShortMarginRatio() {
        return shortMarginRatio;
    }

    public void setShortMarginRatio(double shortMarginRatio) {
        this.shortMarginRatio = shortMarginRatio;
    }

    public char getMaxMarginSideAlgorithm() {
        return maxMarginSideAlgorithm;
    }

    public void setMaxMarginSideAlgorithm(char maxMarginSideAlgorithm) {
        this.maxMarginSideAlgorithm = maxMarginSideAlgorithm;
    }
}
