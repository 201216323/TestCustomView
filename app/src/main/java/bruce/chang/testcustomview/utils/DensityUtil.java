package bruce.chang.testcustomview.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by: BruceChang
 * Date on : 2017/1/13.
 * Time on: 14:41
 * Progect_Name:TestCustomView
 * Source Github：
 * Description: dp sp 和px的相互转换
 */

public class DensityUtil {
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取屏幕的宽
     *
     * @param activity
     * @return
     */
    public final static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高
     *
     * @param activity
     * @return
     */
    public final static int getWindowsHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 得到屏幕宽度
     * @param context
     * @return
     */
    public static int getWinWidth(Activity context) {
        // TODO Auto-generated constructor stub
        return context.getWindowManager().getDefaultDisplay().getWidth();
    }

    /**
     * 得到屏幕高度
     * @param context
     * @return
     */
    public static int getWinHight(Activity context) {
        // TODO Auto-generated constructor stub
        return context.getWindowManager().getDefaultDisplay().getHeight();
    }
}