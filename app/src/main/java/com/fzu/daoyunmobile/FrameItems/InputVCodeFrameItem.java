package com.fzu.daoyunmobile.FrameItems;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fzu.daoyunmobile.R;

public class InputVCodeFrameItem {
    private View priView;
    //当前act的view
    private View actView;
    private EditText editText;
    private ImageView icoImg;
    private Button submitBtn;

    public InputVCodeFrameItem(View view, int priViewId, int editTextId, int btnId, int icoId, int imgID) {
        actView = view;
        SetPriView(priViewId);
        SetEditText(editTextId);
        SetIcoImg(icoId, imgID);
        SetSubBtn(btnId);
    }

    /**
     * @param view      当前main act view
     * @param priViewId 当前layout下的view id
     * @param imgID     需要替换的图片ID
     */
    public InputVCodeFrameItem(View view, int priViewId, int imgID) {
        actView = view;
        SetPriView(priViewId);
        SetEditText(R.id.input_vericode_text);
        SetIcoImg(R.id.input_vericode_icon, imgID);
        SetSubBtn(R.id.inputbt_vericode_submit);
    }


    public void SetPriView(int id) {
        priView = actView.findViewById(id);
    }

    public void SetEditText(int id) {
        editText = priView.findViewById(id);
    }

    public void SetSubBtn(int id) {
        System.out.println("INITFUCK");
        submitBtn = priView.findViewById(id);
        submitBtn.setOnClickListener(v -> {
            System.out.println("FUCK");
        });

    }

    public void SetClickListener(View.OnClickListener clickListener) {
        submitBtn.setOnClickListener(clickListener);
    }

    public Button GetSubBtn() {
        return submitBtn;
    }


    /**
     * @return 获取输入框内容
     */
    public String GetEditText() {
        return editText.getText().toString();
    }


    /**
     * @param name 设值输入框的名称
     */
    public void SetEditTextHint(String name) {
        editText.setHint(name);
    }

    public void SetIcoImg(int id, int imgId) {
        icoImg = priView.findViewById(id);
        Bitmap bm = BitmapFactory.decodeResource(actView.getResources(), imgId);
        icoImg.setImageBitmap(bm);
    }
}
