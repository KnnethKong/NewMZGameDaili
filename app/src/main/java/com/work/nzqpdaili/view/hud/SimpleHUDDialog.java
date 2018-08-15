package com.work.nzqpdaili.view.hud;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.work.nzqpdaili.R;


class SimpleHUDDialog extends Dialog {

    public SimpleHUDDialog(Context context, int theme) {
        super(context, theme);
    }

    public static SimpleHUDDialog createDialog(Context context) {
        SimpleHUDDialog dialog = new SimpleHUDDialog(context, R.style.SimpleHUD);
        dialog.setContentView(R.layout.activity_app_hud);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return dialog;
    }

    public void setMessage(String message) {
        TextView msgView = (TextView) findViewById(R.id.simplehud_message);
        msgView.setText(message);
    }

    public void setImage(Context ctx, int resId) {
        ImageView image = (ImageView) findViewById(R.id.simplehud_image);
        image.setImageResource(resId);

        if (resId == R.drawable.simplehud_spinner) {
            Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.progressbar);
            anim.start();
            image.startAnimation(anim);
        }
    }


}
