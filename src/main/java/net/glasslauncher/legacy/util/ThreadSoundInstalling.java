package net.glasslauncher.legacy.util;


import com.google.gson.Gson;
import net.glasslauncher.common.CommonConfig;
import net.glasslauncher.common.FileUtils;
import net.glasslauncher.legacy.Config;
import net.glasslauncher.legacy.jsontemplate.MinecraftResource;
import net.glasslauncher.legacy.jsontemplate.MinecraftResources;
import net.glasslauncher.proxy.web.WebUtils;

import java.io.File;
import java.nio.file.Files;

public class ThreadSoundInstalling implements Runnable {
    private String instance;

    public ThreadSoundInstalling(String s)
    {
        instance = s;
    }

    @Override
    public void run() {
        String baseURL = "https://mcresources.modification-station.net/MinecraftResources/";
        String basePath = Config.getInstancePath(instance) + ".minecraft/resources/";
        MinecraftResources minecraftResources;

        try {
            minecraftResources = (new Gson()).fromJson(WebUtils.getStringFromURL(baseURL + "json.php"), MinecraftResources.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            for (MinecraftResource minecraftResource : minecraftResources.getFiles()) {
                File file = new File(basePath + minecraftResource.getFile());
                File cacheFile = new File(CommonConfig.getGlassPath() + "cache/resources/" + minecraftResource.getFile());
                String md5 = minecraftResource.getMd5();
                String url = baseURL + minecraftResource.getFile().replace(" ", "%20");

                FileUtils.downloadFile(url, cacheFile.getParent(), md5);
                file.getParentFile().mkdirs();
                Files.copy(cacheFile.toPath(), file.toPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
