package org.embulk.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

public class EmbulkExample
{
    public void createExample(String basePathInString)
            throws IOException
    {
        createExample(Paths.get(basePathInString));
    }

    public void createExample(Path basePath)
            throws IOException
    {
        // TODO(dmikurube): Log with a kind of loggers instead of |System.out|.
        System.out.printf("Creating %s directory...\n", basePath.toString());

        System.out.printf("  Creating %s/\n", basePath.toString());
        Path csvPath = basePath.resolve("csv");
        Files.createDirectories(csvPath);
        System.out.printf("  Creating %s/\n", csvPath.toString());

        Path csvSamplePath = csvPath.resolve("sample_01.csv.gz");
        System.out.printf("  Creating %s\n", csvSamplePath.toString());
        outputSampleCsv(csvSamplePath);

        Path ymlSamplePath = basePath.resolve("seed.yml");
        System.out.printf("  Creating %s\n", ymlSamplePath.toString());
        outputSampleYml(ymlSamplePath);

        System.out.println("");
        System.out.println("Run following subcommands to try embulk:");
        System.out.println("");
        System.out.printf("   1. embulk guess %s -o config.yml\n", ymlSamplePath.toString());
        System.out.println("   2. embulk preview config.yml");
        System.out.println("   3. embulk run config.yml");
        System.out.println("");
    }

    private void outputSampleCsv(Path path)
            throws IOException
    {
        // TODO(dmikurube): Move the data into Java resources.
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("id,account,time,purchase,comment\n");
        csvBuilder.append("1,32864,2015-01-27 19:23:49,20150127,embulk\n");
        csvBuilder.append("2,14824,2015-01-27 19:01:23,20150127,embulk jruby\n");
        csvBuilder.append("3,27559,2015-01-28 02:20:02,20150128,\"Embulk \"\"csv\"\" parser plugin\"\n");
        csvBuilder.append("4,11270,2015-01-29 11:54:36,20150129,NULL\n");
        csvBuilder.append("\n");
        byte[] csvSample = csvBuilder.toString().getBytes(StandardCharsets.UTF_8);

        try (GZIPOutputStream output = new GZIPOutputStream(Files.newOutputStream(path))) {
            output.write(csvSample);
        }
    }

    private void outputSampleYml(Path path)
            throws IOException
    {
        // TODO(dmikurube): Move the data into Java resources.
        StringBuilder ymlBuilder = new StringBuilder();
        ymlBuilder.append("in:\n");
        ymlBuilder.append("  type: file\n");
        ymlBuilder.append("  path_prefix: \"#{File.expand_path File.join(path, 'csv', 'sample_')}\"\n");
        ymlBuilder.append("out:\n");
        ymlBuilder.append("  type: stdout\n");
        byte[] ymlSample = ymlBuilder.toString().getBytes(StandardCharsets.UTF_8);

        try (OutputStream output = Files.newOutputStream(path)) {
            output.write(ymlSample);
        }
    }
}
