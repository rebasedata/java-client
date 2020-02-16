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
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Converter class
 *
 * @author RebaseData
 */
public class Converter {

    private Config config;

    public Converter() {
        config = new Config();
    }

    public Converter(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void convertAndSaveToDirectory(List<InputFile> inputFiles, String outputFormat, File targetDirectory) throws Exception {
        Map<String, String> options = new HashMap();

        convertAndSaveToDirectory(inputFiles, outputFormat, targetDirectory, options);
    }

    public void convertAndSaveToDirectory(List<InputFile> inputFiles, String outputFormat, File targetDirectory, Map<String, String> options) throws Exception {
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }

        Random random = new Random();
        String randomPart = random.nextInt() + "-" + random.nextInt();

        String temporaryZipFilePath = config.getWorkingDirectory() + File.separator + "temporary-zip-file-" + randomPart;
        File temporaryZipFile = new File(temporaryZipFilePath);

        convertAndSaveToZipFile(inputFiles, outputFormat, temporaryZipFile, options);

        UnzipService.execute(temporaryZipFile, targetDirectory);

        temporaryZipFile.delete();
    }

    public void convertAndSaveToZipFile(List<InputFile> inputFiles, String outputFormat, File outputZipFile) throws Exception {
        Map<String, String> options = new HashMap();

        convertAndSaveToZipFile(inputFiles, outputFormat, outputZipFile, options);
    }

    public void convertAndSaveToZipFile(List<InputFile> inputFiles, String outputFormat, File outputZipFile, Map<String, String> options) throws Exception {
        Random random = new Random();
        String randomPart = random.nextInt() + "-" + random.nextInt();

        String inputZipFilePath = config.getWorkingDirectory() + File.separator + "input-zip-file-" + randomPart;
        File inputZipFile = new File(inputZipFilePath);

        CreateZipFileService.execute(inputZipFile, inputFiles);

        String urlString = config.getProtocol() + "://" + config.getHost() + "/api/v1/convert?";

        // Handle api key
        if (options.containsKey("apiKey")) {
            urlString += "apiKey=" + URLEncoder.encode(options.get("apiKey"), "UTF-8") + "&";
        } else if (config.getApiKey() != null) {
            urlString += "apiKey=" + URLEncoder.encode(config.getApiKey(), "UTF-8") + "&";
        }

        // Handle conversion options
        for (String key : options.keySet()) {
            urlString += key + "=" + URLEncoder.encode(options.get(key), "UTF-8") + "&";
        }

        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream output = conn.getOutputStream();
        byte[] b = new byte[16 * 1024];
        int count;
        FileInputStream fis = new FileInputStream(inputZipFile);
        while ((count = fis.read(b)) > 0) {
            output.write(b, 0, count);
            output.flush();
        }

        inputZipFile.delete();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new Exception("Request failed: " + conn.getResponseCode() + " " + conn.getResponseMessage());
        }

        String contentType = conn.getHeaderField("Content-Type");

        if (contentType.equals("application/json")) {
            String response = ConvertInputStreamToStringService.execute(conn.getInputStream());

            throw new Exception("Conversion failed: " + response);
        }

        InputStream inputStream = conn.getInputStream();

        java.nio.file.Files.copy(inputStream, outputZipFile.toPath());

        inputStream.close();
    }
}
