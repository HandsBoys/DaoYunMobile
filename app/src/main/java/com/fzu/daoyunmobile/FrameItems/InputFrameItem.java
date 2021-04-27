package com.fzu.daoyunmobile.FrameItems;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.fzu.daoyunmobile.R;

public class InputFrameItem {
    private View priView;
    //当前act的view
    private View actView;
    private EditText editText;
    private ImageView icoImg;

    public InputFrameItem(View view, int priViewId, int editTextId, int icoId, int imgID, String name) {
        actView = view;
        setPriView(priViewId);
        setEditText(editTextId);
        setEditTextHint(name);
        setIcoImg(icoId, imgID);
    }


    public InputFrameItem(View view, int priViewId, int imgID, String name) {
        actView = view;
        setPriView(priViewId);
        setEditText(R.id.input_frameitem_editText);
        setEditTextHint(name);
        setIcoImg(R.id.input_frameitem_img, imgID);
    }


    public void setPriView(int id) {
        priView = actView.findViewById(id);
    }

    public void setEditText(int id) {
        editText = priView.findViewById(id);
    }

    /**
     * @return 获取输入框内容
     */
    public String getEditTextStr() {
        return editText.getText().toString();
    }


    public EditText getEditText() {
        return editText;
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
}
