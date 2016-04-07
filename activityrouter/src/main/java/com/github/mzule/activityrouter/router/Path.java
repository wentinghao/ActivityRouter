package com.github.mzule.activityrouter.router;

import android.net.Uri;

/**
 * Created by CaoDongping on 4/7/16.
 */
public class Path {
    private String value;
    private Path next;

    private Path(String value) {
        this.value = value;
    }

    public static boolean match(final Path format, final Path link) {
        if (format.length() != link.length()) {
            return false;
        }
        Path x = format;
        Path y = link;
        while (x != null) {
            if (!x.match(y)) {
                return false;
            }
            x = x.next;
            y = y.next;
        }
        return true;
    }

    public static Path create(Uri uri) {
        Path path = new Path(uri.getScheme().concat("://"));
        String urlPath = uri.getPath();
        if (urlPath == null) {
            urlPath = "";
        }
        if (urlPath.endsWith("/")) {
            urlPath = urlPath.substring(0, urlPath.length() - 1);
        }
        parse(path, uri.getHost() + urlPath);
        return path;
    }

    private static void parse(Path scheme, String s) {
        String[] components = s.split("/");
        String path = components[0];
        Path linkComponent = new Path(path);
        scheme.next = linkComponent;
        if (components.length > 1) {
            parse(linkComponent, s.substring(components[0].length() + 1));
        }
    }

    public Path next() {
        return next;
    }

    public int length() {
        Path path = this;
        int len = 1;
        while (path.next != null) {
            len++;
            path = path.next;
        }
        return len;
    }

    private boolean match(Path path) {
        return isArgument() || value.equals(path.value);
    }

    public boolean isArgument() {
        return value.startsWith(":");
    }

    public String argument() {
        return value.substring(1);
    }

    public String value() {
        return value;
    }
}
