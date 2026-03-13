package com.ecarx.xui.adaptapi.car.vehicle;

import com.ecarx.xui.adaptapi.VendorDefinition;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public interface ISceneMode {
    public static final int SCENE_FUNC_AWAKENING = 788660736;

    @VendorDefinition(author = "@ECARX", date = "2022-01-12", project = "smart", requirement = "")
    public static final int SCENE_FUNC_AWAKENING_OFF_REASON = 788660752;
    public static final int SCENE_FUNC_BIOCHEMICAL_MODE = 788595712;
    public static final int SCENE_FUNC_CLEAN_MODE = 788595456;

    @VendorDefinition(author = "@ECARX", date = "2021-05-14", project = "KX11/EX11")
    public static final int SCENE_FUNC_KING_MODE = 788662016;

    @VendorDefinition(author = "@ECARX", date = "2021-08-19", project = "E02")
    public static final int SCENE_FUNC_NAP_MODE = 788662272;

    @VendorDefinition(author = "@ECARX", date = "2021-05-14", project = "KX11/EX11")
    public static final int SCENE_FUNC_NORMAL_MODE = 788661504;
    public static final int SCENE_FUNC_PARENT_CHILD = 788660992;
    public static final int SCENE_FUNC_PET_MODE = 788595968;

    @VendorDefinition(author = "@ECARX", date = "2021-05-14", project = "KX11/EX11")
    public static final int SCENE_FUNC_ROMANTIC_MODE = 788661760;
    public static final int SCENE_FUNC_SMOKING = 788660480;
    public static final int SCENE_FUNC_THEATER_MODE = 788594944;
    public static final int SCENE_FUNC_WASH_MODE = 788595200;
    public static final int SCENE_FUNC_YUEDONG = 788661248;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SceneAwakingReasonMode {
        public static final int FAN_OFF = 788660753;
        public static final int TIME_OUT = 788660754;
        public static final int USAGEMODE_CHANGE = 788660755;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SceneFunction {
    }
}
