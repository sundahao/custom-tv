package com.qgd.commons.tv.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 作者：ethan on 2016/3/15 11:22
 * 邮箱：ethan.chen@fm2020.com
 */
public class FocusSoundUtil {
    private int play=0;
    public static void dispatchKeyEvent(View view, KeyEvent event) {
        try {
            int direction = 0;
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_LEFT;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_RIGHT;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_UP;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_DOWN;
                    }
                    break;
                case KeyEvent.KEYCODE_TAB:
                    if (event.hasNoModifiers()) {
                        direction = View.FOCUS_FORWARD;
                    } else if (event.hasModifiers(KeyEvent.META_SHIFT_ON)) {
                        direction = View.FOCUS_BACKWARD;
                    }
                    break;
            }

            //            if (event.getAction() == KeyEvent.ACTION_UP) {
            //                if (direction != 0) {
            //                    View focused = view.findFocus();
            //                    if (focused != null) {
            //                        View v = focused.focusSearch(direction);
            //                        if (v != null && v != focused) {
            //                            if (direction != View.FOCUS_DOWN && direction != View.FOCUS_UP) {
            //                                if ((view instanceof ViewGroup))
            //                                    v.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            //                            }
            //                        }
            //                    }
            //                }
            //            } else

            if (event.getAction() == KeyEvent.ACTION_UP) {
                if (direction != 0) {
                    View focused = view.findFocus();
                    if (focused != null) {
                        View v = focused.focusSearch(direction);
                        if (v != null && v != focused) {
                            if (direction != View.FOCUS_DOWN && direction != View.FOCUS_UP) {

                                v.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));

                            }
                        } else if (v != focused) {
                            //                            AudioManager mAudioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
                            //                            mAudioManager.playSoundEffect(AudioManager.FX_KEYPRESS_INVALID);

                        }
                    }
                }
            } else if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (direction != 0) {
                    View focused = view.findFocus();
                    if (focused != null) {
                        View v = focused.focusSearch(direction);
                        if (v != null && v != focused) {

                        } else if (v != focused) {
                            if (v == null) {
                                AudioManager mAudioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
                                mAudioManager.playSoundEffect(AudioManager.FX_KEYPRESS_INVALID);
                            }

                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
