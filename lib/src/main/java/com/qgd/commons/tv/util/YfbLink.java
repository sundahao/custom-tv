package com.qgd.ttz.tv.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.qgd.commons.tv.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangke on 2017/6/12.
 */

public class YfbLink {
    private static final Pattern URI_PATTERN = Pattern.compile("^([a-zA-Z0-9]+)://([a-zA-Z0-9._]+)([/a-zA-Z0-9._]*)?(\\?.*)?$");
    private String protocol;
    private String module;
    private String path;
    private LinkedHashMap<String, String> params;

    public YfbLink() {
    }

    public YfbLink(String linkUri) {
        Matcher m = URI_PATTERN.matcher(linkUri);
        if (!m.matches()) {
            throw new IllegalArgumentException("illegal uri: " + linkUri);
        }

        protocol = m.group(1);
        module = m.group(2);
        path = m.group(3);
        if (StringUtils.isBlank(path)) {
            path = "/";
        }
        String ps = m.group(4);
        if (StringUtils.isNotEmpty(ps)) {
            params = decodeParams(ps);
        }
    }

    public static LinkedHashMap<String, String> decodeParams(String paramString) {
        if (StringUtils.isBlank(paramString)) {
            return new LinkedHashMap<>();
        }

        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        String[] temp = paramString.substring(1).split("&");
        for (String s : temp) {
            int idx = s.indexOf('=');
            if (idx != -1) {
                params.put(s.substring(0, idx), s.substring(idx + 1));
            }
        }
        return params;
    }

    public static String encodeParams(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder buf = new StringBuilder();
        for(Map.Entry<String,String> entry : params.entrySet()) {
            try {
                buf.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append('&');
            } catch (UnsupportedEncodingException e) {
                //ignore
            }
        }
        if (buf.length() > 0) {
            //remove last '&'
            buf.setLength(buf.length() - 1);
        }
        return buf.toString();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String toUri() {
        StringBuilder buf = new StringBuilder(StringUtils.isNotEmpty(protocol) ? protocol : "activity").append("://");
        buf.append(this.module);
        if (StringUtils.isNotEmpty(this.path)) {
            buf.append("/").append(this.path);
        }
        if(params != null && params.size() > 0) {
            buf.append("?").append(encodeParams(params));
        }
        return buf.toString();
    }

    public String toString() {
        return toUri();
    }

    public void open(Context context) {
        switch (protocol) {
            case "activity":
                context.startActivity(toIntentOld());
                break;

            case "intent":
                context.startActivity(toIntentNew());
                break;

            case "app":
                context.startActivity(context.getPackageManager().getLaunchIntentForPackage(module));
                break;

            case "broadcast":
                context.sendBroadcast(toIntentNew());
                break;

            default:
                throw new IllegalStateException("unsupported protocol");
        }
    }

    private Intent toIntentOld() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(module, module + "." + path.substring(1)));
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES | Intent.FLAG_ACTIVITY_NEW_TASK);
        putExtras(intent, true);
        return intent;
    }

    private Intent toIntentNew() {
        Intent intent = new Intent(module);
        intent.putExtra("_path", path);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES | Intent.FLAG_ACTIVITY_NEW_TASK);
        putExtras(intent, false);
        return intent;
    }

    private void putExtras(Intent intent, boolean old) {
        if (params == null || params.isEmpty()) {
            return;
        }

        boolean first = true;
        for (Map.Entry<String, String> e : params.entrySet()) {
            if ("activity".equals(protocol) && first) {
                //遗留问题：protocol以activity开头的第1个参数需要是整数
                intent.putExtra(e.getKey(), Integer.valueOf(e.getValue()));
                first = false;
            } else {
                intent.putExtra(e.getKey(), e.getValue());
            }
        }
    }
}
