package com.ecarx.xui.adaptapi.navigation.ehp;

import android.os.Bundle;
import com.ecarx.xui.adaptapi.FunctionStatus;
import com.ecarx.xui.adaptapi.VendorDefinition;
import com.ecarx.xui.adaptapi.navigation.ehp.v2.IV2Manager;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IEHP {
    public static final int EHP_PROTOCOL_V1 = 1;
    public static final int EHP_PROTOCOL_V2 = 2;
    public static final int EHP_PROTOCOL_V3 = 3;
    public static final int EHP_PROTOCOL_V4 = 4;
    public static final int EHP_PROTOCOL_V5 = 5;

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface EHPProtocol {
    }

    public interface IEHPDataInfo {
        Bundle getExtendInformation();

        int getProtocol();

        byte[] getProtocolData();
    }

    public interface IEHPProviderListener {
        void onEHPProviderRequest(int i, byte[] bArr);
    }

    IV2Manager getEHPV2Manager();

    int[] getSupportedEHPProtocol();

    FunctionStatus isEHPByteProtocolSupported(int i);

    FunctionStatus isEHPSupported();

    boolean registerEHPProviderListener(int i, IEHPProviderListener iEHPProviderListener);

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "EX11")
    void registerEhpCallback(IEHPCallBack iEHPCallBack);

    boolean unregisterEHPProviderListener(IEHPProviderListener iEHPProviderListener);

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "EX11")
    void unregisterEhpCallback(IEHPCallBack iEHPCallBack);

    int updadteEHPProtocolData(int i, IEHPDataInfo iEHPDataInfo);

    @VendorDefinition(author = "@ECARX", date = "2021-06-18", project = "EX11")
    void updateEhpDtc(boolean z, boolean z2, boolean z3, boolean z4);
}
