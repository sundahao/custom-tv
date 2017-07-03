package com.example.yangke.app1;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by yangke on 2017/7/3.
 */
public class AudioHelper {
    public static final String XIRI_DEV_PREFIX = "AUDIO_USB_U0416";
    public static final String USB_DEV_PREFIX = "AUDIO_USB_";

    private static final String TAG = "AudioIn";
    private AudioManager audioManager;
    private String usbAudioIn = null;
    private String xiriAudioIn = null;
    private String activeAudioIn = null;

    public AudioHelper(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 设置为USB摄像头或USB麦克风作为音频输入设备
     */
    public void setActiveAudioInUsb() {
        readAudioSettings();
        if (usbAudioIn == null) {
            Log.i(TAG, "no usb audio in device");
            return;
        }

        if (!usbAudioIn.equals(activeAudioIn)) {
            Log.i(TAG, "set active device: " + usbAudioIn);
            audioManager.setParameters("audio_devices_in_active=" + usbAudioIn);
        }
    }

    /**
     * 设置默认音频输入。如果有语音遥控器，会把语音遥控器作为默认音频输入设备
     */
    public void setActiveAudioInDefault() {
        readAudioSettings();
        if (xiriAudioIn != null && !xiriAudioIn.equals(activeAudioIn)) {
            Log.i(TAG, "set active device: " + xiriAudioIn);
            audioManager.setParameters("audio_devices_in_active=" + xiriAudioIn);
        }
    }

    private void readAudioSettings() {
        activeAudioIn = audioManager.getParameters("audio_devices_in_active");
        String deviceStr = audioManager.getParameters("audio_devices_in");
        String[] devs = deviceStr.split(",");
        for (String dev : devs) {
            if (dev.startsWith(USB_DEV_PREFIX)) {
                if (dev.startsWith(XIRI_DEV_PREFIX)) {
                    xiriAudioIn = dev;
                } else {
                    usbAudioIn = dev;
                }
            }
        }
    }
}
