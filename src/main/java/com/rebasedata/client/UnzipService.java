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
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Unzip a ZIP file to a target directory
 *
 * @author RebaseData
 */
class UnzipService {

    public static void execute(File zipFile, File targetDirectory) throws Exception {
        if (!targetDirectory.exists()) {
            throw new Exception("Target directory must exist: " + targetDirectory.getAbsolutePath());
        }

        FileInputStream fis = new FileInputStream(zipFile);
        ZipInputStream zipInputStream = new ZipInputStream(fis);
        ZipEntry ze = zipInputStream.getNextEntry();
        while (ze != null) {
            String fileName = ze.getName();
            File newFile = new File(targetDirectory.getAbsolutePath() + File.separator + fileName);
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            int len;
            byte[] buffer = new byte[2048];
            while ((len = zipInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();

            zipInputStream.closeEntry();
            ze = zipInputStream.getNextEntry();
        }

        zipInputStream.closeEntry();
        zipInputStream.close();
        fis.close();
    }
}
