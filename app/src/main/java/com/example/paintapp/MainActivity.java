package com.example.paintapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BlendMode;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;

    private float floatStartX = -1, floatStartY = -1,
            floatEndX = -1, floatEndY = -1;

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        imageView = findViewById(R.id.imageView);
    }

    private void drawPaintSketchImage() {

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(imageView.getWidth(),
                    imageView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            bitmap.setHasAlpha(true);
            canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            paint.setColor(Color.BLUE);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
        }
        canvas.drawLine(floatStartX,
                floatStartY - 220,
                floatEndX,
                floatEndY - 220,
                paint);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
            floatStartX = event.getX();
            floatStartY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
//                File fileSaveImage = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//                        Calendar.getInstance().getTime().toString() + ".jpg");
                String folder_main = "PaintApp";
                File newFolder = new File(Environment.getExternalStorageDirectory(), folder_main);
                if (!newFolder.exists()) {
                    newFolder.mkdirs();
                }
                File fileSaveImage = new File(newFolder + "//" + Calendar.getInstance().getTime().toString() + ".png");
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileSaveImage);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(this,
                            "File Saved Successfully",
                            Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }break;
            case R.id.clear:
                paint.reset();
                bitmap = Bitmap.createBitmap(imageView.getWidth(),
                        imageView.getHeight(),
                        Bitmap.Config.ARGB_8888);
                bitmap.setHasAlpha(true);
                canvas = new Canvas(bitmap);
                canvas.drawColor(Color.WHITE);
                paint.setColor(Color.BLUE);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(8);
                canvas.drawLine(floatStartX,
                        floatStartY - 220,
                        floatEndX,
                        floatEndY - 220,
                        paint);
                imageView.setImageBitmap(bitmap);
                break;
        }


        return true;

    }
}