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

    public InputFrameItem(View view, int priViewId, int editTextId, int icoId, int imgID) {
        actView = view;
        SetPriView(priViewId);
        SetEditText(editTextId);
        SetIcoImg(icoId, imgID);
    }

    public InputFrameItem(View view, int priViewId, int imgID) {
        new InputFrameItem(view, priViewId, R.id.input_frameitem_editText, R.id.input_frameitem_img, imgID);
    }


    public void SetPriView(int id) {
        priView = actView.findViewById(id);
    }

    public void SetEditText(int id) {
        editText = priView.findViewById(id);
        // editText.setText("1066666655");
    }

    public void SetEditTextHint(String name) {
        editText.setHint(name);
    }

    public void SetIcoImg(int id, int imgId) {
        icoImg = priView.findViewById(id);
        Bitmap bm = BitmapFactory.decodeResource(actView.getResources(), imgId);
        icoImg.setImageBitmap(bm);
    }
}
