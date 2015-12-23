package com.jtournie.cml.tasitrack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by jtournie on 23/12/15.
 */

    public class InfoDialog extends DialogFragment
    {
        public InfoDialog()
        {

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            Bundle args = getArguments();
            String title = args.getString("title", "");
            String message = args.getString("message", "");

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Nothing to do
                        }
                    })

                    .create();
        }
    }
