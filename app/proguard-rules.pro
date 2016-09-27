# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Proc\Android\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#这里是针对第三方的jar包混淆#
-dontwarn com.ut.mini.**
-dontwarn okio.**
-dontwarn android.support.v4.**
-dontwarn okhttp.**

# 这里是针对系统的jar混淆#
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }

#--------Glide-------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

#--------Gson--------
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.**{*;}
-keep public class com.google.gson.** {public private protected *;}
-keepclassmembers class * implements java.io.Serializable {*;}
-keep class com.jingxiangyu.september.network.parser.** { *;}

#--------Android--------
-keepclassmembers class **.R$* {
  public static <fields>;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep public class android.webkit.WebViewClient{
    *;
}
-keep public class android.webkit.WebChromeClient{
    *;
}
-keep public interface android.webkit.WebChromeClient$CustomViewCallback {
    *;
}
-keep public interface android.webkit.ValueCallback {
    *;
}
-keep class * implements android.webkit.WebChromeClient {
    *;
}

-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** { *; }
-keep class de.hdodenhof.circleimageview.** { *; }
-keep class com.bumptech.** { *; }
-keep class android.net.** { *; }
-keep class org.apache.** { *; }