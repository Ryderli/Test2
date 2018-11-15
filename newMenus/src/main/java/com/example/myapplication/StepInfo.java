package com.example.myapplication;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by 立灏 on 2017/5/14.
 */

public class StepInfo  implements Serializable {
    private String step_title;
    private String imageView;


    public StepInfo(String step_title, String imageView) {
        this.step_title = step_title;
        this.imageView = imageView;
    }

    public String getStep_title() {
        return step_title;
    }

    public void setStep_title(String step_title) {
        this.step_title = step_title;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }
}
