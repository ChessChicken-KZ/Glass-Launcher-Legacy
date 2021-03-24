package kz.chesschicken.rubetalogin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class RuBetaLogin {
    public static AuthResponse auth(String u, String p)
    {
        AuthResponse response = new AuthResponse(u);
        try {
            URLConnection urlConnection = (new URL("http://client.rubeta.net/connect.php")).openConnection();
            urlConnection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write("&Password=" + MD5Password.getMD5(p));
            writer.write("&Username=" + u);
            writer.write("&Ip=" + InetAddress.getLocalHost().getHostAddress());
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String ANSWER;
            while ( (ANSWER = reader.readLine()) != null)
            {
                if(ANSWER.contains("ERRORSQL"))
                {
                    response.setStatus(AuthStatus.SQLERROR);
                    continue;
                }
                if(ANSWER.contains("WRONGPASS"))
                {
                    response.setStatus(AuthStatus.WRONGPASS);
                    continue;
                }

                if(ANSWER.contains("TOKEN:"))
                {
                    ANSWER = ANSWER.substring(ANSWER.length() - 33);
                    response.setToken(ANSWER);
                    response.setStatus(AuthStatus.SUCCESS);
                    continue;
                }

                response.setStatus(AuthStatus.ERROR);
            }

            writer.close();
            reader.close();

        } catch (IOException e) {
            response.setStatus(AuthStatus.ERROR);
        }
        return response;
    }

    public static boolean check(String u, String t)
    {
        try {
            URLConnection urlConnection = (new URL("")).openConnection();
            urlConnection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write("&Username="+u);
            writer.write("&Token="+t);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String ANSWER;
            while ( (ANSWER = reader.readLine()) != null )
            {
                if(!ANSWER.contains("SUCCESS"))
                    return false;
            }
            writer.close();
            reader.close();

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public static AuthResponse refresh(String u, String t, String p)
    {
        if(check(u, t))
            return auth(u, p);
        return null;
    }

}
