package android.car.diagnostic;

import android.util.SparseArray;
import android.util.SparseIntArray;

/* loaded from: classes.dex */
public class CarDiagnosticEvent {
    public String dtc;
    private SparseArray<Float> floatValues;
    public int frameType;
    private SparseIntArray intValues;
    public long timestamp;
}
