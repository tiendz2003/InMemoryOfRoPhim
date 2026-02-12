-keep class * implements com.airbnb.mvrx.MavericksState {
    # Giữ lại constructor cấp 1 (primary constructor)
    <init>(...);
}