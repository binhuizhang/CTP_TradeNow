package zhangbinhui.cn.com.sfit.tradenow2.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;
import java.util.List;

import zhangbinhui.cn.com.sfit.tradenow2.R;
import zhangbinhui.cn.com.sfit.tradenow2.field.PositionDetailField;

/**
 * Created by zhang.binhui on 2015-12-10.
 */
public class MyBaseAdapter extends BaseAdapter {
    DecimalFormat df = new DecimalFormat("#0.0");
    private List<PositionDetailField> positionsDetails;
    Context context;

    public MyBaseAdapter(Context context,List<PositionDetailField> positionsDetails){
        this.context = context;
        this.positionsDetails = positionsDetails;
    }


    @Override
    public int getCount(){
        return (positionsDetails==null)?0:positionsDetails.size();
    }

    @Override
    public Object getItem(int position){
        return positionsDetails.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public class ViewHolder{
        TextView textViewItem01;
        TextView textViewItem02;
        TextView textViewItem03;
        TextView textViewItem04;
        TextView textViewItem05;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        final PositionDetailField positionDetail =
                (PositionDetailField)getItem(position);
        ViewHolder viewHolder = null;
        if(convertView==null){
            //-----这里放的是listview要如何显示的自定义布局-----
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.position_1, null);

            viewHolder = new ViewHolder();
            viewHolder.textViewItem01 = (TextView)convertView.findViewById(R.id.instrumentId);
            viewHolder.textViewItem02 = (TextView)convertView.findViewById(R.id.direction);
            viewHolder.textViewItem03 = (TextView)convertView.findViewById(R.id.volume);
            viewHolder.textViewItem04 = (TextView)convertView.findViewById(R.id.averagePrice);
            viewHolder.textViewItem05 = (TextView)convertView.findViewById(R.id.profit);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String instrumentId = positionDetail.getInstrumentID();
        viewHolder.textViewItem01.setText(instrumentId);

        char direction_char = positionDetail.getDirection();
        String direction;
        if(direction_char == '0'){
            viewHolder.textViewItem02.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            direction = "多";
        }else {
            viewHolder.textViewItem02.setTextColor(ContextCompat.getColor(context, R.color.colorHailv));
            direction = "空";
        }
        viewHolder.textViewItem02.setText(direction);

        int volume = positionDetail.getVolume();
        viewHolder.textViewItem03.setText(String.valueOf(volume));

        double openPrice = positionDetail.getOpenPrice();
        viewHolder.textViewItem04.setText(df.format(openPrice));

        double profitByTrade = positionDetail.getPositionProfitByTrade();
        viewHolder.textViewItem05.setText(df.format(profitByTrade));

        if(profitByTrade>0){
            viewHolder.textViewItem05.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }else{
            viewHolder.textViewItem05.setTextColor(ContextCompat.getColor(context, R.color.colorHailv));
        }

        //对ListView中第1个TextView配置OnClick事件
        viewHolder.textViewItem01.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(context,
                        "[textViewItem01.setOnClickListener]点击了" + positionDetail.getInstrumentID(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //对ListView中的每一行信息配置OnClick事件
        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(context,
                        "[convertView.setOnClickListener]点击了"+positionDetail.getInstrumentID(),
                        Toast.LENGTH_SHORT).show();
            }

        });

        //对ListView中的每一行信息配置OnLongClick事件
        convertView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,
                        "[convertView.setOnLongClickListener]点击了"+positionDetail.getInstrumentID(),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return convertView;
    }
}
