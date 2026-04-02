package de.bendyukov.farm.cli;

import de.bendyukov.farm.exceptions.CommandLineParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Utility for reading the lines of a file and printing them.
 *
 * @author Arseniy Bendyukov
 * @version 1.0
 */
final class FileReaderUtil {
    private static final String ERROR_FAILED_READ_FILE = "Failed to read file: %s";
    private static final String ERROR_EMPTY_FILE = "The file is empty: %s";

    private FileReaderUtil() {
    }

    /**
     * Reads all lines from the specified file path and prints them.
     *
     * @param path the path to the file
     * @return a list of the file's lines
     * @throws CommandLineParseException if an error occurs while reading the file
     */
    public static List<String> readAndPrint(String path) throws CommandLineParseException {
        try {
            List<String> lines = Files.readAllLines(Path.of(path));
            for (String line : lines) {
                System.out.println(line);
            }
            if (lines.isEmpty()) {
                throw new CommandLineParseException(String.format(ERROR_EMPTY_FILE, path));
            }
            return lines;
        } catch (IOException exception) {
            throw new CommandLineParseException(String.format(ERROR_FAILED_READ_FILE, path));
        }
    }
}
