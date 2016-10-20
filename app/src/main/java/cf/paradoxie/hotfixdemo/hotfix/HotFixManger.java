package cf.paradoxie.hotfixdemo.hotfix;

import android.content.Context;
import android.text.TextUtils;

import com.dodola.rocoofix.RocooFix;

import java.io.File;

import cf.paradoxie.hotfixdemo.util.MAppInfoManager;

/**
 * Rocoo统一入口
 * Created by shoyu666@163.com on 16/7/5.
 */
public class HotFixManger {
    /**
     * @CallBy attachBaseContext()
     * attachBaseContext 中调用Rocoo初始化和patch加载
     * @param context
     */
    public static void init(Context context) {
        try {
            RocooFix.init(context);
            String proces = MAppInfoManager.getCurProcessName(context);
            if(!TextUtils.isEmpty(proces)&&!proces.endsWith(":push")){
                //因为我的项目有push 这里过滤掉push进程
                installPach(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void installPach(Context context) {
        File lastPach = PatchManger.globalPatchManger.get().patchFileDir.getCurrentPatchJar();
        if (lastPach != null && lastPach.exists()&&lastPach.canRead()) {
            RocooFix.applyPatch(context, lastPach.getAbsolutePath());
            System.out.println("   installPach"+ lastPach.getAbsolutePath());
        }else{
            System.out.println(" no  installPach");
        }
    }


    /**
     * 需要更新patch文件的时候调用
     */
    public static void updatePatchJar(){
        PatchManger.globalPatchManger.get().patchDownloader.downloadPatch();
    }
}
