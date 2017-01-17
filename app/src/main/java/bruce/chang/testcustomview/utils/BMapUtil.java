package bruce.chang.testcustomview.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.View.MeasureSpec;

public class BMapUtil {
	/**
	 * 从view 得到图片
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromViews(View view) {

		int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);  
        view.measure(spec, spec);  
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());  
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),  
                Bitmap.Config.ARGB_8888); 
        Canvas c = new Canvas(b);  
        c.translate(-view.getScrollX(), -view.getScrollY());  
        view.draw(c);  
        view.setDrawingCacheEnabled(true);  
        Bitmap cacheBmp = view.getDrawingCache();  
        Bitmap bitmap = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);  
        view.destroyDrawingCache();  
        return bitmap;
	}
}
