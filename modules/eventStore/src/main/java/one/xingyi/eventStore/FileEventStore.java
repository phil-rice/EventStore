package one.xingyi.eventStore;

import one.xingyi.events.EventAndAudit;
import one.xingyi.events.IEventParserPrinter;
import one.xingyi.events.utils.FilesHelper;
import one.xingyi.optics.iso.IIso;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class FileEventStore implements IEventStore {
    public final BiFunction<String, String, String> nameAndNameSpaceToFileName;
    private IIso<String, EventAndAudit> iso;

    public static FileEventStore store(String dir) {
        return new FileEventStore(FileEventStore.nameAndNameSpaceToFileName(dir), IEventParserPrinter.iso);
    }

    public static BiFunction<String, String, String> nameAndNameSpaceToFileName(String dir) {
        return (ns, n) -> String.join(File.separator, List.of(dir, ns, n));
    }

    public FileEventStore(BiFunction<String, String, String> nameAndNameSpaceToFileName, IIso<String, EventAndAudit> iso) {
        this.nameAndNameSpaceToFileName = nameAndNameSpaceToFileName;
        this.iso = iso;
    }

    @Override
    public CompletableFuture<List<EventAndAudit>> getEvents(String nameSpace, String name) {
        try {
            List<EventAndAudit> events = FilesHelper.getLines(nameAndNameSpaceToFileName.apply(nameSpace, name), l -> iso.to(l));
            return CompletableFuture.completedFuture(events);
        } catch (NoSuchFileException e) {
            return CompletableFuture.completedFuture(List.of());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Void> appendEvent(String nameSpace, String name, EventAndAudit eventAndAudit) {
        try {
            String fileName = nameAndNameSpaceToFileName.apply(nameSpace, name);
            FilesHelper.writeLineToFile(fileName, iso.from(eventAndAudit));
            return CompletableFuture.completedFuture(null);
        } catch (IOException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
