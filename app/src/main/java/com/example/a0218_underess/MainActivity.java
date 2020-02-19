package com.example.a0218_underess;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //找到控件
        final ImageView foreImageView = (ImageView) findViewById(R.id.iv_fr);

        //onCreate方法里面，控件的大小还没有测量好
        foreImageView.post(new Runnable() {
            @Override
            public void run() {
                //System.out.println(foreImageView.getWidth());

                /**
                 * 读取操作的图片（不能对原图操作）
                 * BitmapFactory: 管理位图
                 * decodeResource: 利用工程资源路径下的资源文件生成一个位图
                 * getResources(): 工程的资源路径
                 * R.drawable.fr: 资源路径下drawable文件里面的一个文件的资源id
                 */
                Bitmap orgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fr);

                /**
                 * 拷贝位图
                 */
                final Bitmap copBitmap = Bitmap.createBitmap(orgBitmap.getWidth(), orgBitmap.getHeight(), orgBitmap.getConfig());

                /**
                 * Canvas: 画布
                 * Paint: 画笔
                 * Matrix: 矩阵
                 */
                Canvas canvas = new Canvas(copBitmap);
                Paint paint = new Paint();
                Matrix matrix = new Matrix();
                canvas.drawBitmap(orgBitmap,matrix,paint);

                /**
                 * View---触摸事件
                 */
                foreImageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        //获得触摸的事件
                        int action = event.getAction();

                        //判断事件类型
                        if(action == MotionEvent.ACTION_MOVE){
                            //获得相对于 foreImageView 的坐标
                            float x = event.getX();
                            float y = event.getY();

                            //防止手指的范围越界：因为是以触摸点的四周操作
                            int temp = 30;
                            if((x > temp+10 && x < foreImageView.getWidth()-temp-10) && (y > temp+10 && y < foreImageView.getHeight()-temp-10)){

                                //操作的是原图，原图通过拉扯填充屏幕
                                //我们需要将触摸点在 foreImageView 上的比例算出来
                                float scaleX = x / foreImageView.getWidth();
                                float scaleY = y / foreImageView.getHeight();

                                //以触摸点为原点，temp为半径的圆的范围像素填充为透明
                                for (int i = -temp; i < temp; i++) {
                                    for (int j = -temp; j < temp; j++) {
                                        if (temp * temp >= i * i + j * j) {
                                            copBitmap.setPixel((int) (copBitmap.getWidth() * scaleX + i), (int) (copBitmap.getHeight() * scaleY + j), Color.TRANSPARENT);
                                        }
                                    }
                                }

                                //重新设置修改后的图片
                                foreImageView.setImageBitmap(copBitmap);
                            }

                        }

                        return true;
                    }
                });
            }
        });
    }
}
