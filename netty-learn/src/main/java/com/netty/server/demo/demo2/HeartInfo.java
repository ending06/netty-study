package com.netty.server.demo.demo2;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * --------------------- Change Logs----------------------
 * <p>@author ruirui.qu Initial Created at 18/10/8<p>
 * -------------------------------------------------------
 **/
public class HeartInfo implements Serializable{
    private String ip;

    private int port;

    private Date lasttime;

    private Map<String,String> cpuInfo = Maps.newHashMap();

    private Map<String,String> memInfo = Maps.newHashMap();

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }

    public Map<String, String> getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(Map<String, String> cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public Map<String, String> getMemInfo() {
        return memInfo;
    }

    public void setMemInfo(Map<String, String> memInfo) {
        this.memInfo = memInfo;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("HeartInfo{");
        sb.append("ip='").append(ip).append('\'');
        sb.append(", port=").append(port);
        sb.append(", lasttime=").append(lasttime);
        sb.append(", cpuInfo=").append(cpuInfo);
        sb.append(", memInfo=").append(memInfo);
        sb.append('}');
        return sb.toString();
    }
}