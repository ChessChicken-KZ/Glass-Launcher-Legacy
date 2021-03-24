package net.glasslauncher.legacy.util;

import kz.chesschicken.rubetalogin.*;

import net.glasslauncher.legacy.Config;
import net.glasslauncher.legacy.Main;
import net.glasslauncher.legacy.jsontemplate.LoginInfo;

import javax.swing.*;

public class MojangLoginHandler {

    public static void login(String username, String password) {
        LoginInfo loginInfo = Config.getLauncherConfig().getLoginInfo();
        try {
            if (loginInfo != null && loginInfo.getAccessToken() != null && Config.getLauncherConfig().getClientToken() != null && !loginInfo.getAccessToken().isEmpty() && !Config.getLauncherConfig().getClientToken().isEmpty()) {
                try {
                    Main.LOGGER.info("Validating cached token...");
                    //OpenMCAuthenticator.validate(loginInfo.getAccessToken(), Config.getLauncherConfig().getClientToken());
                    //RefreshResponse response = OpenMCAuthenticator.refresh(loginInfo.getAccessToken(), Config.getLauncherConfig().getClientToken());
                    AuthResponse response = RuBetaLogin.refresh(loginInfo.getUsername(), loginInfo.getAccessToken(), password);
                    Config.getLauncherConfig().setClientToken(response.getToken());
                    Config.getLauncherConfig().setLoginInfo(new LoginInfo(response.getUsername(), response.getToken()));
                    Main.LOGGER.info("Cached token validated!");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Main.LOGGER.info("Token is invalid! Login required.");
                    Config.getLauncherConfig().setLoginInfo(null);
                    return;
                }
            }


            /*
            Useless

            if (Config.getLauncherConfig().getClientToken() == null || Config.getLauncherConfig().getClientToken().isEmpty()) {
                Main.LOGGER.info("Generating a random client token.");
                Config.getLauncherConfig().setClientToken(UUID.randomUUID().toString());
            }
             */

            if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
                Main.LOGGER.info("Getting account info from username and password...");
                AuthResponse response = RuBetaLogin.auth(username, password);
                Config.getLauncherConfig().setClientToken(response.getToken());
                Config.getLauncherConfig().setLoginInfo(new LoginInfo(response.getUsername(), response.getToken()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Server responded with\"" + e.getMessage() + "\". Make sure your username and password are correct and try again.");
            Config.getLauncherConfig().setLoginInfo(null);
        }

    }

    /*
    public static void login(String username, String password) {
        LoginInfo loginInfo = Config.getLauncherConfig().getLoginInfo();
        try {
            if (loginInfo != null && loginInfo.getAccessToken() != null && Config.getLauncherConfig().getClientToken() != null && !loginInfo.getAccessToken().isEmpty() && !Config.getLauncherConfig().getClientToken().isEmpty()) {
                try {
                    Main.LOGGER.info("Validating cached token...");
                    OpenMCAuthenticator.validate(loginInfo.getAccessToken(), Config.getLauncherConfig().getClientToken());
                    RefreshResponse response = OpenMCAuthenticator.refresh(loginInfo.getAccessToken(), Config.getLauncherConfig().getClientToken());
                    Config.getLauncherConfig().setClientToken(response.getClientToken());
                    Config.getLauncherConfig().setLoginInfo(new LoginInfo(response.getSelectedProfile().getName(), response.getAccessToken()));
                    Main.LOGGER.info("Cached token validated!");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Main.LOGGER.info("Token is invalid! Login required.");
                    Config.getLauncherConfig().setLoginInfo(null);
                    return;
                }
            }
            if (Config.getLauncherConfig().getClientToken() == null || Config.getLauncherConfig().getClientToken().isEmpty()) {
                Main.LOGGER.info("Generating a random client token.");
                Config.getLauncherConfig().setClientToken(UUID.randomUUID().toString());
            }
            if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
                Main.LOGGER.info("Getting account info from username and password...");
                AuthenticationResponse response = OpenMCAuthenticator.authenticate(username, password, Config.getLauncherConfig().getClientToken());
                Config.getLauncherConfig().setClientToken(response.getClientToken());
                Config.getLauncherConfig().setLoginInfo(new LoginInfo(response.getSelectedProfile().getName(), response.getAccessToken()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Server responded with\"" + e.getMessage() + "\". Make sure your username and password are correct and try again.");
            Config.getLauncherConfig().setLoginInfo(null);
        }
    }

     */
}
