package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.concurrent.FilenameMutex;
import one.xingyi.events.utils.interfaces.FunctionWithException;
import one.xingyi.events.utils.exceptions.NotFoundException;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
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
        List<T> result = new ArrayList<>();
        try (FileInputStream stream = new FileInputStream(fileName)) {
            try (var lock = stream.getChannel().lock(0L, Long.MAX_VALUE, true)) {
                var reader = new BufferedReader(new InputStreamReader(stream));
                String line = reader.readLine();
                while (line != null) {
                    if (filter.test(line)) result.add(lineToT.apply(line));
                    line = reader.readLine();
                }
                return result;
            }
        } catch (NoSuchFileException e) {
            throw new NotFoundException();
        }
    }

    static FilenameMutex mutex = new FilenameMutex();

    static void writeLineToFile(String fileName, String line) throws IOException {
        Path path = Paths.get(fileName);
        File parent = path.getParent().toFile();
        parent.mkdirs();
        mutex.wantToUseFile(fileName, () -> {
            try (var stream = new FileOutputStream(path.toFile(), true)) {
                try (var lock = stream.getChannel().lock()) {
                    var writer = new BufferedWriter(new OutputStreamWriter(stream));
                    writer.write(line);
                    writer.newLine();
                    writer.flush();
                }
            }
        });
    }

}
