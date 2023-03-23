package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.ByteArrayHelper;
import one.xingyi.events.utils.concurrent.FilenameMutex;
import one.xingyi.events.utils.exceptions.WrappedException;
import one.xingyi.events.utils.interfaces.FunctionWithException;
import one.xingyi.events.utils.exceptions.NotFoundException;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface FilesHelper {


    static byte[] getLines(String fileName) {
        List<byte[]> result = new ArrayList<byte[]>();
        try (FileInputStream stream = new FileInputStream(fileName)) {
            try (var lock = stream.getChannel().lock(0L, Long.MAX_VALUE, true)) {
                var available = stream.available();
                while (available > 0) {
                    byte[] bytes = new byte[available];
                    stream.read(bytes);
                    result.add(bytes);
                    available = stream.available();
                }
                return ByteArrayHelper.append(result);
            }
        } catch (NoSuchFileException e) {
            throw new NotFoundException();
        } catch (IOException e) {
            throw WrappedException.wrap(e);
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

    static void writeLineToFile(String fileName, boolean append,byte[] line) {
        Path path = Paths.get(fileName);
        File parent = path.getParent().toFile();
        parent.mkdirs();
        try {
            mutex.wantToUseFile(fileName, () -> {
                try (var stream = new FileOutputStream(path.toFile(), append)) {
                    try (var lock = stream.getChannel().lock()) {
                        stream.write(line);
                    }
                }
            });
        } catch (IOException e) {
            throw new WrappedException(e);
        }
    }


}
