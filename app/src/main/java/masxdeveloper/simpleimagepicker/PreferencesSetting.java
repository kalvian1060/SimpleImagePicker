package masxdeveloper.simpleimagepicker;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Masx Developer on 4/8/17.
 * https://masx-dev.blogspot.com
 */

public class PreferencesSetting {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public PreferencesSetting(Context ctx) {
        preferences = ctx.getSharedPreferences("SET" ,  Context.MODE_PRIVATE);
    }

    public void setPATH(String Path){
        editor = preferences.edit();
        editor.putString("PATH" , Path);
        editor.apply();
    }

    public String getPATH(){
        return preferences.getString("PATH" , "");
    }
}
