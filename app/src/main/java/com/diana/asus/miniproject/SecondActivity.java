package com.diana.asus.miniproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SecondActivity extends AppCompatActivity {
    private ImageView mResultImageView;
    private String url;
    private String fixedUrl;

    private ProgressDialog progress;
    private Button mButtonSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mResultImageView = findViewById(R.id.resultImageView);
        mButtonSave = findViewById(R.id.buttonSave);

        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("KEY_URL", null);
            fixedUrl = "https://api.apiflash.com/v1/urltoimage?access_key=d271fd0032864f3dbe60f29f19f0c4f3&format=png&url=http://"  + url;
            Glide.with(this).load(fixedUrl).listener(requestListener).into(mResultImageView);
        }
    }
    private RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            progress.dismiss();
            Toast.makeText(SecondActivity.this, "Load gambar gagal, link tidak valid", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            progress.dismiss();
            mButtonSave.setVisibility(View.VISIBLE);
            Toast.makeText(SecondActivity.this, "Load gambar sukses", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    public void handleSave(View view) {
        Bitmap bitmap = ((BitmapDrawable) mResultImageView.getDrawable()).getBitmap();
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/GGWP/");
        dir.mkdir();
        String imagename = url + ".PNG";
        File file = new File(dir, imagename);
        OutputStream out;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(SecondActivity.this, "Image Successfully Saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
