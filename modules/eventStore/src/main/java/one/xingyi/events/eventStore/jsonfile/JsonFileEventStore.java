package one.xingyi.events.eventStore.jsonfile;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.event.audit.AndVersionIdAndAudit;
import one.xingyi.event.audit.AuditIdVersionIso;
import one.xingyi.events.eventStore.IEventStore;
import one.xingyi.events.events.IEvent;
import one.xingyi.events.optics.iso.IIso;
import one.xingyi.events.utils.helpers.AsyncHelper;
import one.xingyi.events.utils.helpers.FilesHelper;
import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.helpers.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class JsonFileEventStore implements IEventStore {

    static String version = "0";
    static Logger logger = LoggerFactory.getLogger(JsonFileEventStore.class);
    private Executor executor;
    public final BiFunction<String, String, String> nameAndNameSpaceToFileName;

    public static JsonFileEventStore storeUniqueFiles(Executor executor, String dir, String separator, int... pattern) {
        return new JsonFileEventStore(executor, JsonFileEventStore.nameAndNameSpaceToUniqueFileName(dir, separator, pattern), defaultIso);
    }

    public static JsonFileEventStore storeSharedFiles(Executor executor, String dir, String separator, int... pattern) {
        return new JsonFileEventStore(executor, JsonFileEventStore.nameAndNameSpaceToUniqueFileName(dir, separator, pattern), defaultIso);
    }


    public static IIso<String, AndVersionIdAndAudit<IEvent>> defaultIso = AuditIdVersionIso.iso(IIso.jsonIso(IEvent.class));
    private final IIso<String, AndVersionIdAndAudit<IEvent>> iso;


    public static BiFunction<String, String, String> nameAndNameSpaceToUniqueFileName(String dir, String separator, int... pattern) {
        var toDir = StringHelper.asDirectories(separator, pattern);
        return (ns, n) -> String.join(separator, ListHelper.<String>filter(List.of(dir, ns, toDir.apply(StringHelper.sha256(n)), n), s -> s.length() > 0)) + ".dat";
    }


    public static BiFunction<String, String, String> nameAndNameSpaceToSharedFileName(String dir, String separator, int... pattern) {
        var toFile = StringHelper.asDirectories(separator, pattern);
        return (ns, n) -> {
            String hash = StringHelper.sha256(n);
            return String.join(separator, List.of(dir, ns, toFile.apply(hash))) + ".dat";
        };
    }

    public JsonFileEventStore(Executor executor, BiFunction<String, String, String> nameAndNameSpaceToFileName, IIso<String, AndVersionIdAndAudit<IEvent>> iso) {
        this.executor = executor;
        this.nameAndNameSpaceToFileName = nameAndNameSpaceToFileName;
        this.iso = iso;
    }

    @Override
    public CompletableFuture<List<AndAudit<IEvent>>> getEvents(String nameSpace, String name) {
        String match = version + "\t" + name + "\t";
        Predicate<String> filter = (String s) -> s.startsWith(match);
        return AsyncHelper.wrapSupplier(executor, () -> {
            List<AndVersionIdAndAudit<IEvent>> lines = FilesHelper.collectLines(nameAndNameSpaceToFileName.apply(nameSpace, name), filter, iso::to);
            return ListHelper.map(lines, AndVersionIdAndAudit::toAndAudit);
        });
    }

    @Override
    public CompletableFuture<Void> appendEvent(String nameSpace, String name, AndAudit<IEvent> eventAndAudit) {
        return AsyncHelper.wrapRunnable(executor, () -> {
            String fileName = nameAndNameSpaceToFileName.apply(nameSpace, name);
//            logger.debug("Appending to file " + fileName + " event " + eventAndAudit);
            var withVersionAndId = new AndVersionIdAndAudit<>(version, name, eventAndAudit.payload(), eventAndAudit.audit());
            FilesHelper.writeLineToFile(fileName, true,(iso.from(withVersionAndId )+"\n").getBytes());
        });
    }
}
