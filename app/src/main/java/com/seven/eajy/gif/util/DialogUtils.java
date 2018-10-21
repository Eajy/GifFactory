package com.seven.eajy.gif.util;

import android.app.Dialog;
import android.content.Context;

import com.seven.eajy.gif.R;


/**
 * Created by zhang on 2018.02.10.
 */

public class DialogUtils {

    private static Dialog waitDialog;

    public static Dialog showWaitingDialog(Context context) {
        waitDialog = new Dialog(context, R.style.WaitingDialogTheme);
        waitDialog.setContentView(R.layout.dialog_waiting);
        waitDialog.setCancelable(false);
        waitDialog.show();
        return waitDialog;
    }

    public static void hideWaitingDialog() {
        try {
            if (waitDialog != null && waitDialog.isShowing()) {
                waitDialog.dismiss();
                waitDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
