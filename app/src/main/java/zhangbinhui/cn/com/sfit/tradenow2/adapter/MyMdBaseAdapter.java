package zhangbinhui.cn.com.sfit.tradenow2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import zhangbinhui.cn.com.sfit.tradenow2.R;
import zhangbinhui.cn.com.sfit.tradenow2.field.DepthMarketDataField;
import zhangbinhui.cn.com.sfit.tradenow2.mdview.MyHScrollView;

/**
 * Created by zhang.binhui on 2015-12-10.
 */
public class MyMdBaseAdapter extends BaseAdapter {
    public List<MDViewHolder> mHolderList = new ArrayList<MDViewHolder>();
    int id_row_layout;
    LayoutInflater mInflater;
    private List<DepthMarketDataField> mdList;
    Context context;
    DecimalFormat df = new DecimalFormat("#0.0");
    DecimalFormat df_int = new DecimalFormat("#");
    RelativeLayout mHead;
    int selectedPosition;

    public MyMdBaseAdapter(Context context, int id_row_layout, List<DepthMarketDataField> mdList,RelativeLayout mHead){
        this.context = context;
        this.mdList = mdList;
        this.id_row_layout = id_row_layout;
        mInflater = LayoutInflater.from(context);
        this.mHead = mHead;
        selectedPosition = -1;
    }

    public void setSelectedPosition(int selectedPosition){
        this.selectedPosition = selectedPosition;
    }


    @Override
    public int getCount(){
        return (mdList==null)?0:mdList.size();
    }

    @Override
    public Object getItem(int position){
        return mdList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public class MDViewHolder{
        TextView textViewItem01;
        TextView textViewItem02;
        TextView textViewItem03;
        TextView textViewItem04;
        TextView textViewItem05;
        TextView textViewItem06;
        TextView textViewItem07;
        TextView textViewItem08;
        HorizontalScrollView scrollView;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        final DepthMarketDataField md = (DepthMarketDataField)getItem(position);
        MDViewHolder viewHolder = null;

        if(convertView==null){
            //据说这个叫加锁。。。锁住线程
            synchronized (context) {
                //-----这里放的是listview要如何显示的自定义布局-----
                convertView = mInflater.inflate(id_row_layout, null);
                MyHScrollView scrollView1 = (MyHScrollView) convertView
                        .findViewById(R.id.horizontalScrollView1);

                viewHolder = new MDViewHolder();
                viewHolder.scrollView = scrollView1;
                viewHolder.textViewItem01 = (TextView) convertView.findViewById(R.id.md_instrument);
                viewHolder.textViewItem02 = (TextView) convertView.findViewById(R.id.md_lastprice);
                viewHolder.textViewItem03 = (TextView) convertView.findViewById(R.id.md_up_down);
                viewHolder.textViewItem04 = (TextView) convertView.findViewById(R.id.md_bid1);
                viewHolder.textViewItem05 = (TextView) convertView.findViewById(R.id.md_bid_volume1);
                viewHolder.textViewItem06 = (TextView) convertView.findViewById(R.id.md_ask1);
                viewHolder.textViewItem07 = (TextView) convertView.findViewById(R.id.md_ask_volume1);
                viewHolder.textViewItem08 = (TextView) convertView.findViewById(R.id.md_operInterest);

                MyHScrollView headSrcrollView = (MyHScrollView) mHead.findViewById(R.id.horizontalScrollView1);
                headSrcrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView1));

                convertView.setTag(viewHolder);
                mHolderList.add(viewHolder);
            }
        }else{
            viewHolder = (MDViewHolder)convertView.getTag();
        }

        double up_down = md.getUp_down();
        if(up_down >= 0){
            viewHolder.textViewItem02.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            viewHolder.textViewItem03.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }else{
            viewHolder.textViewItem02.setTextColor(ContextCompat.getColor(context, R.color.colorHailv));
            viewHolder.textViewItem03.setTextColor(ContextCompat.getColor(context, R.color.colorHailv));
        }

        viewHolder.textViewItem01.setText(md.getInstrumentID());
        viewHolder.textViewItem02.setText(df.format(md.getLastPrice()));
        viewHolder.textViewItem03.setText(df.format(up_down));

        if(md.getBidPrice1() > md.getUpperLimitPrice()){
            viewHolder.textViewItem04.setText("-");
        }else{
            viewHolder.textViewItem04.setText(df.format(md.getBidPrice1()));
        }

        viewHolder.textViewItem05.setText(df_int.format(md.getBidVolume1()));

        if(md.getAskPrice1() > md.getUpperLimitPrice()){
            viewHolder.textViewItem06.setText("-");
        }else{
            viewHolder.textViewItem06.setText(df.format(md.getAskPrice1()));
        }

        viewHolder.textViewItem07.setText(df_int.format(md.getAskVolume1()));
        viewHolder.textViewItem08.setText(df_int.format(md.getOpenInterest()));

        if(selectedPosition == position){
            viewHolder.textViewItem01.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelected));
        }else{
            viewHolder.textViewItem01.setBackgroundColor(Color.TRANSPARENT);
        }

        //对ListView中第1个TextView配置OnClick事件
//        viewHolder.textViewItem01.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,
//                        "[textViewItem01.setOnClickListener]点击了" + md.get("md_instrument"),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

        //对ListView中的每一行信息配置OnLongClick事件,但是这么做会很卡。。。
//        convertView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(context,
//                        "[convertView.setOnLongClickListener]点击了" + md.getInstrumentID(),
//                        Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

        return convertView;
    }

    class OnScrollChangedListenerImp implements MyHScrollView.OnScrollChangedListener {
        MyHScrollView mScrollViewArg;

        public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
            mScrollViewArg = scrollViewar;
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            mScrollViewArg.smoothScrollTo(l, t);
        }
    }
}
