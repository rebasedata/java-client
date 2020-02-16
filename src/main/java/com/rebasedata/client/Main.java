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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class that can be used on the command line.
 *
 * @author RebaseData
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("RebaseData Java Client");

        if (args.length == 0) {
            PrintUsageService.execute();
            System.exit(1);
        }

        String command = args[0];

        if (command.equals("help")) {
            PrintUsageService.execute();
            System.exit(1);
        } else if (command.equals("convert")) {
            if (args.length < 3) {
                PrintUsageService.execute();
                System.exit(1);
            }

            List<String> optionArguments = new ArrayList();
            List<String> inputFileArguments = new ArrayList();
            for (int i = 1; i < args.length; i++) {
                String arg = args[i];

                if (arg.startsWith("-") && !arg.startsWith("--")) {
                    System.err.println("Got invalid argument: " + arg);
                    System.exit(1);
                }

                if (arg.startsWith("--")) {
                    if (!arg.contains("=")) {
                        System.err.println("Got invalid option argument: " + arg);
                        System.exit(1);
                    }

                    optionArguments.add(arg);
                } else {
                    inputFileArguments.add(arg);
                }
            }

            String outputFormat = "csv";
            String apiKey = null;
            String protocol = null;
            String host = null;
            Map<String, String> conversionOptions = new HashMap();
            for (String optionArgument : optionArguments) {
                String[] parts = optionArgument.split("=", 2);
                String name = parts[0].substring(2);
                String value = parts[1];

                if (name.equals("output-format")) {
                    outputFormat = value;
                } else if (name.equals("api-key")) {
                    apiKey = value;
                } else if (name.equals("protocol")) {
                    protocol = value;
                } else if (name.equals("host")) {
                    host = value;
                } else if (name.equals("conversion-option")) {
                    String[] conversionOptionParts = value.split("=", 2);
                    String conversionOptionName = conversionOptionParts[0];
                    String conversionOptionValue = conversionOptionParts[1];

                    conversionOptions.put(conversionOptionName, conversionOptionValue);
                } else {
                    System.err.println("Got unknown option: " + name);
                    System.exit(1);
                }
            }

            List<InputFile> inputFiles = new ArrayList();
            for (int i = 0; i < inputFileArguments.size() - 1; i++) {
                String inputFileArgument = inputFileArguments.get(i);

                File file = new File(inputFileArgument);

                if (!file.exists()) {
                    System.err.println("Input file does not exist: " + inputFileArgument);
                    System.exit(1);
                }

                System.out.println("Got input file: " + inputFileArgument);

                inputFiles.add(new InputFile(file));
            }

            Config config = new Config();
            if (apiKey != null) {
                config.setApiKey(apiKey);
            }
            if (protocol != null) {
                config.setProtocol(protocol);
            }
            if (host != null) {
                config.setHost(host);
            }

            String outputPath = args[args.length - 1];
            if (outputPath.endsWith(".zip")) {
                File outputZipFile = new File(outputPath);

                if (!outputZipFile.getParentFile().exists()) {
                    System.err.println("Output ZIP file folder does not exist: " + outputZipFile.getParent());
                    System.exit(1);
                }

                if (outputZipFile.exists()) {
                    System.err.println("Output ZIP file already exists: " + outputPath);
                    System.exit(1);
                }

                System.out.println("Converting using RebaseData..");

                Converter converter = new Converter(config);
                try {
                    converter.convertAndSaveToZipFile(inputFiles, outputFormat, outputZipFile, conversionOptions);
                } catch (Exception e) {
                    System.err.println("Got exception while converting: " + e.getMessage());
                    System.err.println(ConvertStackTraceToStringService.execute(e));
                    System.exit(1);
                }

                System.out.println("Conversion succeeded.");
                System.out.println("Here you have the conversion result: " + outputPath);
            } else {
                System.out.println("Creating directory " + outputPath);

                new File(outputPath).mkdirs();

                System.out.println("Converting using RebaseData..");

                Converter converter = new Converter(config);
                try {
                    converter.convertAndSaveToDirectory(inputFiles, outputFormat, new File(outputPath), conversionOptions);
                } catch (Exception e) {
                    System.err.println("Got exception while converting: " + e.getMessage());
                    System.err.println(ConvertStackTraceToStringService.execute(e));
                    System.exit(1);
                }

                System.out.println("Conversion succeeded.");
                System.out.println("Here you have the conversion result: " + outputPath);
            }

        } else {
            System.err.println("Got unknown command: " + command);
            System.err.println("");
            PrintUsageService.execute();
            System.exit(1);
        }

    }
}
