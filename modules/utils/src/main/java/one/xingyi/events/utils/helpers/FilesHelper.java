package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.interfaces.FunctionWithException;
import one.xingyi.events.utils.exceptions.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.function.Predicate;

public interface FilesHelper {
    static <T, E extends Exception> List<T> getLines(String fileName, FunctionWithException<String, T, E> lineToT) throws E, IOException {
        try {
            return ListHelper.map(Files.readAllLines(Paths.get(fileName)), lineToT);
        } catch (NoSuchFileException e) {
            throw new NotFoundException();
        }
    }
    static <T, E extends Exception> List<T> collectLines(String fileName, Predicate<String> filter, FunctionWithException<String, T, E> lineToT) throws E, IOException {
        try {
            return ListHelper.collect(Files.readAllLines(Paths.get(fileName)), filter,lineToT);
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
