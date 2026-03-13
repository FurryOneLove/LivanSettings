package com.ecarx.xui.adaptapi.audio.audiofx;

import com.ecarx.xui.adaptapi.FunctionStatus;
import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface IEqualizer {
    public static final int MAX_BASS_LEVEL_HIGH = 3;
    public static final int MAX_BASS_LEVEL_LOW = 1;
    public static final int MAX_BASS_LEVEL_MEDIUM = 2;

    @VendorDefinition(author = "@ECARX", date = "2021-06-30", project = "Lambda")
    @Retention(RetentionPolicy.SOURCE)
    public @interface MaxBassLevel {
    }

    short getBand(int i);

    int[] getBandFreqRange(short s);

    short getBandLevel(short s);

    short[] getBandLevelRange();

    int getCenterFreq(short s);

    short getCurrentPreset();

    @VendorDefinition(author = "@ECARX", date = "2021-06-30", project = "Lambda")
    int getMaxBassLevel();

    short getNumberOfBands();

    short getNumberOfPresets();

    String getPresetName(short s);

    FunctionStatus isEqualizerSupported();

    @VendorDefinition(author = "@ECARX", date = "2021-06-30", project = "Lambda")
    boolean isMaxBassOn();

    void setBandLevel(short s, short s2);

    @VendorDefinition(author = "@ECARX", date = "2021-06-30", project = "Lambda")
    void setMaxBassLevel(int i);

    @VendorDefinition(author = "@ECARX", date = "2021-07-13", project = "Lambda")
    void setMaxBassOn(boolean z);

    void usePreset(short s);
}
