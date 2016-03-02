/*
 * Copyright © 上海庆谷豆信息科技有限公司.
 */

package com.qgd.commons.tv.conf;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import com.qgd.commons.tv.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 */
public class Config {
    private Properties props = new Properties();

    public Config(Properties props) {
        if (props != null) {
            this.props = props;
        }
    }

    public static Config load(Application application) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "qgd/yfb/conf/" + application.getPackageName() + ".properties");
            return load(file);
        } catch (Throwable e) {
            Log.e("Config", "load config fail", e);
        }
        return new Config(new Properties());
    }

    public static Config load(File file) {
        Properties props = new Properties();
        FileInputStream in = null;
        try {
            if (file != null && file.exists()) {
                in = new FileInputStream(file);
                props.load(in);
            }
        } catch (IOException e) {
            Log.e("Config", "load config fail", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Throwable e) {
                    //ignore
                }
            }

            return new Config(props);
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public String getString(String key, String defaultValue) {
        String value = get(key);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    public boolean getBoolean(String key) {
        String value = get(key);
        return Boolean.parseBoolean(value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return StringUtils.isEmpty(value) ? defaultValue : Boolean.parseBoolean(value);
    }

    public int getInt(String key) {
        String value = get(key);
        return Integer.parseInt(value);
    }

    public int getInt(String key, int defaultValue) {
        String value = get(key);
        return StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
    }

    public long getLong(String key) {
        String value = get(key);
        return Long.parseLong(value);
    }

    public long getLong(String key, long defaultValue) {
        String value = get(key);
        return StringUtils.isEmpty(value) ? defaultValue : Long.parseLong(value);
    }
}
