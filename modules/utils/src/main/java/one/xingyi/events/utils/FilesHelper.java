package one.xingyi.events.utils;

import one.xingyi.store.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

public interface FilesHelper {
    static <T, E extends Exception> List<T> getLines(String fileName, FunctionWithException<String, T, E> lineToT) throws E, IOException {
        try {
            return ListHelper.map(Files.readAllLines(Paths.get(fileName)), lineToT);
        } catch (NoSuchFileException e) {
            throw new NotFoundException();
        }
    }

    static void writeLineToFile(String fileName, String line) throws IOException {
        Path path = Paths.get(fileName);
        File parent = path.getParent().toFile();
        parent.mkdirs();
        Files.writeString(path, line + "\n", StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }

}
