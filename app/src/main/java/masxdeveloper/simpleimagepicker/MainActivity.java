package masxdeveloper.simpleimagepicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    PreferencesSetting setting;

    ImageView Preview;
    Button Gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setting = new PreferencesSetting(this);

        Preview = (ImageView) findViewById(R.id.imagePreview);
        Gallery = (Button) findViewById(R.id.openGallery);
        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!permission()) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                } else {
                    Intent Gallery = new Intent(Intent.ACTION_PICK);
                    Gallery.setType("image/*");
                    startActivityForResult(Gallery, 200);
                }

            }
        });

        initPicture();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == RESULT_OK) {

            new setBitmap(data.getData(), "GALLERY").execute();

        }
    }

    private void initPicture() {
        String PATH = setting.getPATH();
        new setBitmap(PATH).execute();
    }

    private boolean permission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED;
    }

    private Bitmap getBitmap(String PATH) {
        Bitmap MyBitmap;
        try {
            Bitmap raw = BitmapFactory.decodeFile(PATH);

            if (raw.getWidth() >= raw.getHeight()) {
                MyBitmap = Bitmap.createBitmap(raw, raw.getWidth() / 2 - raw.getHeight() / 2, 0, raw.getHeight(), raw.getHeight());
            } else {
                MyBitmap = Bitmap.createBitmap(raw, 0, raw.getHeight() / 2 - raw.getWidth() / 2, raw.getWidth(), raw.getWidth());
            }
            return MyBitmap;
        } catch (NullPointerException e) {
            MyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person);
            return MyBitmap;
        }
    }

    private class setBitmap extends AsyncTask<Void, Void, String> {

        private Uri uri;
        private String Path;
        private String Status;

        setBitmap(Uri uri, String Status) {
            this.uri = uri;
            this.Status = Status;
        }

        setBitmap(String path) {
            Path = path;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... voids) {

            if (Status != null) {
                if (Status.equals("GALLERY")) {
                    return FIlePath.getPath(MainActivity.this, uri);
                } else {
                    return Path;
                }
            } else {
                return Path;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setting.setPATH(s);
            Bitmap bmp = getBitmap(s);
            Preview.setImageBitmap(bmp);
        }
    }
}
