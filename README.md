# rebasedata-java-client

Introduction
------------

This library allows to convert various database formats in Java using the RebaseData API. When processing a database, the database is first sent to the secure RebaseData servers which then return the converted data. See below for a list of examples.

Installation
------------

Download the [latest JAR file](https://search.maven.org/artifact/com.rebasedata/client).
There are no dependencies.

Command line examples
---------------------

See detailed instructions how to run command line tool:

```shell
java -jar rebasedata-client.jar
```

Convert a MDB file and save the CSV files (one for each table) to a ZIP file. Keep in mind that CSV is the default output format.

```shell
java -jar rebasedata-client.jar convert database.mdb output.zip
```

Convert a MDB file and save the CSV files (one for each table) to a directory. Keep in mind that CSV is the default output format.

```shell
java -jar rebasedata-client.jar convert database.mdb /tmp/output/
```

Convert a MDB file to MySQL and save it in a local directory. After conversion, you'll have a MySQL script file in /tmp/output/data.sql.

```shell
java -jar rebasedata-client.jar convert --output-format=mysql database.mdb /tmp/output/
```

Convert a MDB file to MySQL and use an API key or a Customer Key.

```shell
java -jar rebasedata-client.jar convert --api-key=your-api-or-customer-key --output-format=mysql database.mdb /tmp/output/
```

Code examples
-------------

Convert a MDB database to a set of CSV files (one CSV for each table) and save it as a ZIP file.

```java
import com.rebasedata.client.Converter;
import com.rebasedata.client.InputFile;

List<InputFile> inputFiles = new ArrayList();
inputFiles.add(new InputFile(new File("access.mdb")));

Converter converter = new Converter();
converter.convertAndSaveToZipFile(inputFiles, "csv", new File("/tmp/output.zip"));
```

Convert a MDB database to MySQL and save the result in a local directory.

```java
import com.rebasedata.client.Converter;
import com.rebasedata.client.InputFile;

List<InputFile> inputFiles = new ArrayList();
inputFiles.add(new InputFile(new File("access.mdb")));

Converter converter = new Converter();
converter.convertAndSaveToDirectory(inputFiles, "mysql", new File("/tmp/output/"));
```

You can also change the configuration of the library:
```java
import com.rebasedata.client.Config;
import com.rebasedata.client.Converter;

Config config = new Config();
config.setApiKey("your-api-key"); // In case you have an API or Customer Key
config.setWorkingDirectory("/tmp/working-dir"); // In case you want to change the working directory

Converter converter = new Converter(config);
```

License
-------

This code is licensed under the [MIT license](https://opensource.org/licenses/MIT).


Feedback
--------

We love to get feedback from you! Did you discover a bug? Do you need an additional feature? Open an issue on Github and RebaseData will try to resolve your issue as soon as possible! Thanks in advance for your feedback!
