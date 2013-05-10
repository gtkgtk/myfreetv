package org.rom.myfreetv.config;

public class AutoPath {

    private boolean enabled;
    private String url;

    public AutoPath(boolean enabled, String url) {
        this.enabled = enabled;
        setUrl(url);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setUrl(String url) {
        this.url = url == null ? "" : url;
    }

}
