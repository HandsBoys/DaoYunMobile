package com.fzu.daoyunmobile.FrameItems;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fzu.daoyunmobile.R;

import java.util.Locale;

public class InputVCodeFrameItem {
    private View priView;
    //当前act的view
    private View actView;
    private EditText editText;
    private ImageView icoImg;
    private Button submitBtn;
    private int seconds = 60;
    private int oriSeconds = 60;

    public InputVCodeFrameItem(View view, int priViewId, int editTextId, int btnId, int icoId, int imgID) {
        actView = view;
        setPriView(priViewId);
        setEditText(editTextId);
        setIcoImg(icoId, imgID);
        setSubBtn(btnId);
    }

    /**
     * @param view      当前main act view
     * @param priViewId 当前layout下的view id
     * @param imgID     需要替换的图片ID
     */
    public InputVCodeFrameItem(View view, int priViewId, int imgID) {
        actView = view;
        setPriView(priViewId);
        setEditText(R.id.input_vericode_text);
        setIcoImg(R.id.input_vericode_icon, imgID);
        setSubBtn(R.id.inputbt_vericode_submit);
    }


    public void setPriView(int id) {
        priView = actView.findViewById(id);
    }

    public void setEditText(int id) {
        editText = priView.findViewById(id);
    }

    public void setSubBtn(int id) {
        submitBtn = priView.findViewById(id);
    }
    
    public Button getSubBtn() {
        return submitBtn;
    }


    /**
     * @return 获取输入框内容
     */
    public String getEditText() {
        return editText.getText().toString();
    }


    /**
     * @param name 设值输入框的名称
     */
    public void setEditTextHint(String name) {
        editText.setHint(name);
    }

    public void setIcoImg(int id, int imgId) {
        icoImg = priView.findViewById(id);
        Bitmap bm = BitmapFactory.decodeResource(actView.getResources(), imgId);
        icoImg.setImageBitmap(bm);
    }

    //倒计时
    public void startBtnDownTime(int seconds) {
        this.seconds = seconds;
        this.oriSeconds = seconds;
        submitBtn.post(downTimrRunnable);
    }


    /**
     * 倒计时线程
     */
    private Runnable downTimrRunnable = new Runnable() {
        @Override
        public void run() {
            submitBtn.setText(seconds <= 0 ? "重新获取" : String.format(Locale.CHINA, "%ds", seconds));
            submitBtn.setEnabled(seconds <= 0);
            seconds--;
            if (seconds >= 0) {
                submitBtn.postDelayed(this, 1000);//递归执行
            } else {
                seconds = oriSeconds;//复位
            }
        }
    };
}
