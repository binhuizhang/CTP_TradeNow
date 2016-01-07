package zhangbinhui.cn.com.sfit.tradenow2;

import android.support.v4.view.ViewPager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * Created by zhang.binhui on 2015-12-09.
 */
public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
    int offset;
    int bmpW;
    int currIndex;
    ImageView cursor;
    int one;
    int two;
    public MyOnPageChangeListener(int offset,int bmpW,int currIndex,ImageView cursor){
        this.offset = offset;
        this.bmpW = bmpW;
        this.currIndex = currIndex;
        this.cursor = cursor;
        one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        two = one * 2;// 页卡1 -> 页卡3 偏移量
    }



    @Override
    public void onPageSelected(int arg0) {
        Animation animation = null;
        switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(0, one, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, one, 0, 0);
                }
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(0, two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(one, two, 0, 0);
                }
                break;
        }
        currIndex = arg0;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }
}
