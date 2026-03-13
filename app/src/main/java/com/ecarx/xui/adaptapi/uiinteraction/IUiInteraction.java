package com.ecarx.xui.adaptapi.uiinteraction;

import android.view.Display;
import com.ecarx.xui.adaptapi.CallStatus;
import com.ecarx.xui.adaptapi.NonNull;
import com.ecarx.xui.adaptapi.Nullable;

/* loaded from: classes.dex */
public interface IUiInteraction {
    IMultiWindow getMultiWindowManager();

    ITouchManager getTouchManager();

    IWindowManager getWindowManager();

    CallStatus startApplicationToDisplay(@NonNull String str, @Nullable Display display, @NonNull Display display2);
}
