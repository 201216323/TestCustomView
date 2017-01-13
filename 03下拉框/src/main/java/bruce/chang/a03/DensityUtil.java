package bruce.chang.a03;

import android.content.Context;

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
}