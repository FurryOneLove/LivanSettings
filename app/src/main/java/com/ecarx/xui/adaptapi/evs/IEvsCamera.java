package com.ecarx.xui.adaptapi.evs;

import android.view.SurfaceHolder;

@Deprecated
/* loaded from: classes.dex */
public interface IEvsCamera {
    boolean doTouchDown(int i, int i2);

    boolean doTouchMove(int i, int i2);

    boolean doTouchUp(int i, int i2);

    boolean open(int i);

    boolean release();

    boolean setPreviewDisplay(SurfaceHolder surfaceHolder);

    boolean startPreview();

    boolean stopPreview();
}
