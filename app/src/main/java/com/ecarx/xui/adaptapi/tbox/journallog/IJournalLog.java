package com.ecarx.xui.adaptapi.tbox.journallog;

import com.ecarx.xui.adaptapi.FunctionStatus;

@Deprecated
/* loaded from: classes.dex */
public interface IJournalLog {

    public interface JournalLogStatusCallback {
        void onJournalLogServiceOnOff(boolean z);
    }

    boolean isJournalLogServiceOn();

    FunctionStatus isJournalLogSupported();

    boolean setJournalLogServiceOn(boolean z);

    void setJournalLogStatusCallback(JournalLogStatusCallback journalLogStatusCallback);

    void unsetJournalLogStatusCallback(JournalLogStatusCallback journalLogStatusCallback);
}
