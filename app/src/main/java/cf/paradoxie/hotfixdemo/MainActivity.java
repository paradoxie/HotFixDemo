package cf.paradoxie.hotfixdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cf.paradoxie.hotfixdemo.hotfix.PatchManger;
import cf.paradoxie.hotfixdemo.hotfix.PatchUpdateInfo;
import cf.paradoxie.hotfixdemo.util.MAppInfoManager;
import cf.paradoxie.hotfixdemo.util.MAppManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.tv);
        File patchJarDir = PatchManger.globalPatchManger.get().patchFileDir.getPatchJarDir();
        tv.setText("补丁初始化路径" + (patchJarDir != null ? patchJarDir.getAbsolutePath() : " error"));
        PatchUpdateInfo mock = PatchUpdateInfo.mock;
        if (mock.targetVersion == MAppInfoManager.getVersionCode(this) && !mock.newPatchMd5.equals(getCurrentPatchMd5())) {
            //更新patch
            //            HotFixManger.updatePatchJar();
        }
        this.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelloHack hack = new HelloHack();
                Toast.makeText(MainActivity.this, hack.showHello(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获得当前Patch.jar Md5
     *
     * @return
     */
    private String getCurrentPatchMd5() {
        String md5 = null;
        File ff = PatchManger.globalPatchManger.get().patchFileDir.getCurrentPatchJar();
        try {
            md5 = DigestUtils.md5Hex(new FileInputStream(ff));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return md5;
    }


    @Override
    public void onBackPressed() {
        // LxApplication.exit();
        MAppManager.AppExit(this);

    }
}
