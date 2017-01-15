package com.bruce.chang.a12.lib;


import com.bruce.chang.a12.lib.core.BaseEffects;
import com.bruce.chang.a12.lib.core.FadeIn;
import com.bruce.chang.a12.lib.core.Fall;
import com.bruce.chang.a12.lib.core.FlipH;
import com.bruce.chang.a12.lib.core.FlipV;
import com.bruce.chang.a12.lib.core.NewsPaper;
import com.bruce.chang.a12.lib.core.RotateBottom;
import com.bruce.chang.a12.lib.core.RotateLeft;
import com.bruce.chang.a12.lib.core.Shake;
import com.bruce.chang.a12.lib.core.SideFall;
import com.bruce.chang.a12.lib.core.SlideBottom;
import com.bruce.chang.a12.lib.core.SlideLeft;
import com.bruce.chang.a12.lib.core.SlideRight;
import com.bruce.chang.a12.lib.core.SlideTop;
import com.bruce.chang.a12.lib.core.Slit;

/**
 * Created by lee on 2014/7/30.
 */
public enum  Effectstype {

    FADEIN(FadeIn.class),
    SLIDELEFT(SlideLeft.class),
    SLIDETOP(SlideTop.class),
    SLIDEBOTTOM(SlideBottom.class),
    SLIDERIGHT(SlideRight.class),
    FALL(Fall.class),
    NEWSPAGER(NewsPaper.class),
    FLIPH(FlipH.class),
    FLIPV(FlipV.class),
    ROTATEBOTTOM(RotateBottom.class),
    ROTATELEFT(RotateLeft.class),
    SLIT(Slit.class),
    SHAKE(Shake.class),
    SIDEFILL(SideFall.class);
    private Class effectsClazz;

    private Effectstype(Class mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
