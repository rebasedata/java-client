/*
 * The MIT License
 *
 * Copyright 2020 RebaseData.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.rebasedata.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Config class
 *
 * @author RebaseData
 */
public class Config {

    private String protocol = "https";
    private String host = "www.rebasedata.com";
    private File workingDirectory = null;
    private String apiKey = null;
    private boolean cacheEnabled = false;
    private File cacheDirectory = null;

    public Config() {
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) throws Exception {
        if (!protocol.equals("http") && !protocol.equals("https")) {
            throw new Exception("Got invalid protocol: " + protocol);
        }

        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public File getWorkingDirectory() throws IOException {
        if (workingDirectory == null) {
            File defaultWorkingDirectory = Files.createTempDirectory("rebasedata-java-client-working-dir").toFile();

            if (!defaultWorkingDirectory.exists()) {
                defaultWorkingDirectory.mkdir();
            }

            return defaultWorkingDirectory;
        }

        return workingDirectory;
    }

    public void setWorkingDirectory(File workingDirectory) throws Exception {
        if (!workingDirectory.isDirectory()) {
            throw new Exception("The given working directory File object must be a directory!");
        }

        this.workingDirectory = workingDirectory;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean getCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public File getCacheDirectory() throws IOException {
        if (cacheDirectory == null) {
            File defaultCacheDirectory = new File(getWorkingDirectory().getAbsolutePath() + File.separator + "cache");

            if (!defaultCacheDirectory.exists()) {
                defaultCacheDirectory.mkdir();
            }

            return defaultCacheDirectory;
        }

        return cacheDirectory;
    }

    public void setCacheDirectory(File cacheDirectory) throws Exception {
        if (!cacheDirectory.isDirectory()) {
            throw new Exception("The given cache directory File object must be a directory!");
        }

        this.cacheDirectory = cacheDirectory;
    }
}
