package cn.finalteam.gradlebuildchannels;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

/**
 * 多渠道打包：
 *
 * 博客：http://www.jianshu.com/p/ec5178fbd838
 * Github:https://github.com/pengjianbo/MutiChannelPackup
 *
 * 参考：http://stormzhang.com/devtools/2015/01/15/android-studio-tutorial6/
 *
 * 使用assemble命令结合Build Variants来创建task。
 */

/**
 * 方法一
 * Gradle版多渠道打包使用方法(推荐：比其他写法简洁)：
 * 一、添加channels.properties渠道信息配置文件。
 *    #默认渠道
 *    channel.default=paojiao
 *    #全部渠道列表
 *    channel.list=baidu,360,hiapk....
 * 二、在module的build.gradle引入channels.gradle配置文件。
 *    apply from: "https://raw.githubusercontent.com/MutiChannelPackup/gradle-build/master/channels.gradle"
 *  或者将channels.gradle下载到本地引入：apply from: "../channels.gradle"
 * 三、在manifest中添加配置。
 *    <meta-data android:name="UMENG_CHANNEL" android:value="${CHANNEL_VALUE}" />
 *    <meta-data android:name="JPUSH_CHANNEL" android:value="${CHANNEL_VALUE}" />
 *    ...
 * 四、执行打包命令./gradlew assembleRelease或通过android studio->generate signed apk打包。
 * 五、在module目录下可以看到所有的渠道包。
 *
 *
 * 执行打包命令：
 * 打开命令行定位到project目录，执行命令： gradlew assembleRelease，
 * 不出意外的话 bulid->outputs->apk 下面会有各个渠道的包。
 */


/**
 * 方法二
 * Gradle版多渠道打包使用方法：
 * 一、在module的build.gradle中添加如下配置(productFlavors必须写在buildTypes前面，否则报错)：
 * productFlavors{
 *   wandoujia{}
 *   baidu{}
 *   c360{}
 *   xiaomi{}
 *   qunadai{}
 *
 *   productFlavors.all{
 *      flavor -> flavor.manifestPlaceholders = [ONEAPM_TEST_CHANNEL: name]
 *   }
 * }
 *
 * productFlavors表示特性，productFlavors.all表示遍历。
 *
 * 二、在AndroidManifest.xml中配置(以友盟统计为例)
 *    <meta-data android:name="TD_CHANNEL_ID" android:value="${ONEAPM_TEST_CHANNEL}" />
 *
 * 注意事项：
 * 1、productFlavors 定义的时候里面的类似 wandoujia、baidu，不能是数字开头，不能是关键字 test 等，因为你要意识到你在写 gradle 脚本，要符合 groovy 语法。
 * 2、flavor.manifestPlaceholders = [ONEAPM_TEST_CHANNEL: name]中的ONEAPM TEST CHANNEL 一定要和 AndroidManifext.xml 定义的一致。
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        //获得渠道信息
        try {
            ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String umengChannel = appInfo.metaData.getString("UMENG_CHANNEL");
            String jpushChannel = appInfo.metaData.getString("JPUSH_CHANNEL");

            Toast.makeText(this, "多渠道打包UmengChannel=" + umengChannel, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "多渠道打包JPushChannel=" + jpushChannel, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
