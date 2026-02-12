-keep class com.airbnb.mvrx.** { *; }
-keep interface com.airbnb.mvrx.** { *; }
-keep class * implements com.airbnb.mvrx.MavericksState {
    # Giữ lại constructor cấp 1 (primary constructor)
    <init>(...);
}
# Nếu dùng ViewModel là Nested Class (ví dụ Companion Object Factory)
-keepattributes InnerClasses