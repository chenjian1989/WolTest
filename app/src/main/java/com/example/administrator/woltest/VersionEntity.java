package com.example.administrator.woltest;

public class VersionEntity {

    /**
     * name : WolTest
     * version : 2
     * changelog : 2
     * updated_at : 1484815055
     * versionShort : 1.0.1
     * build : 2
     * installUrl : http://download.fir
     * .im/v2/app/install/5880791b959d692c1800078f?download_token=90c866de72849fc18fa54d7baf0ca22c0026source=update
     * install_url : http://download.fir
     * .im/v2/app/install/5880791b959d692c1800078f?download_token=90c866de72849fc18fa54d7baf0ca22c0026source=update
     * direct_install_url : http://download.fir
     * .im/v2/app/install/5880791b959d692c1800078f?download_token=90c866de72849fc18fa54d7baf0ca22c0026source=update
     * update_url : http://fir.im/f7y5
     * binary : {"fsize":1320265}
     */

    private String name;
    private String version;
    private String changelog;
    private int updated_at;
    private String versionShort;
    private double build;
    private String installUrl;
    private String install_url;
    private String direct_install_url;
    private String update_url;
    private BinaryBean binary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public double getBuild() {
        return build;
    }

    public void setBuild(double build) {
        this.build = build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public BinaryBean getBinary() {
        return binary;
    }

    public void setBinary(BinaryBean binary) {
        this.binary = binary;
    }

    public static class BinaryBean {
        /**
         * fsize : 1320265
         */

        private int fsize;

        public int getFsize() {
            return fsize;
        }

        public void setFsize(int fsize) {
            this.fsize = fsize;
        }
    }
}
