# Сохраняем все классы и интерфейсы автомобильных API
-keep class com.ecarx.xui.adaptapi.** { *; }
-keep interface com.ecarx.xui.adaptapi.** { *; }

-keep class android.car.** { *; }
-keep interface android.car.** { *; }

# Явно сохраняем классы, создаваемые через рефлексию
-keep class com.ecarx.xui.adaptapi.car.impl.CarImpl {
    public <init>(android.content.Context);
}
-keep class com.ecarx.xui.adaptapi.input.impl.InputImpl {
    public <init>(android.content.Context);
}

# Сохраняем атрибуты для корректной работы рефлексии и аннотаций
-keepattributes Signature, *Annotation*, InnerClasses, EnclosingMethod

# Сохраняем имена методов в callback-интерфейсах, так как они могут вызываться из нативного кода (JNI) или других процессов
-keepclassmembers interface * extends com.ecarx.xui.adaptapi.input.IKeyCallback {
    <methods>;
}
