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

/**
 * Prints the command line usage
 *
 * @author RebaseData
 */
class PrintUsageService {

    public static void execute() {
        System.err.println("");
        System.err.println("Show help information");
        System.err.println("Usage: rebasedata-client.jar help");
        System.err.println("");
        System.err.println("Execute a conversion");
        System.err.println("Usage: rebasedata-client.jar convert [options] input-files.. output-dir");
        System.err.println("Usage: rebasedata-client.jar convert [options] input-files.. output-file.zip");
        System.err.println("Possible options:");
        System.err.println("--output-format=value          The output format to which the database should be converted to. Default: csv");
        System.err.println("--api-key=value                The API key/Customer Key to use. Empty by default.");
        System.err.println("--protocol=value               The protocol to use for API communication. Default: https");
        System.err.println("--host=value                   The host to use for API communication. Default: www.rebasedata.com");
        System.err.println("--conversion-option=name=value Specify an option for the conversion, for example --conversion-option=password=secret");
    }
}
