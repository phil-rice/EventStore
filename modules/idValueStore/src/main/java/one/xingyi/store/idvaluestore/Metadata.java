package one.xingyi.store.idvaluestore;

import one.xingyi.event.audit.Audit;
import one.xingyi.events.utils.helpers.StringHelper;
import one.xingyi.events.utils.tuples.Tuple2;
import one.xingyi.events.optics.iso.IIso;
import one.xingyi.events.signedData.SignedData;

import java.util.function.Function;

public record Metadata(String extension, String contentType, Audit audit) {

    private static Function<String, Tuple2<String, String>> splitter = StringHelper.splitIn2("\t");

    public static IIso<String, Metadata> iso(String secret) {
        return SignedData.iso(secret, Metadata.class);
    }
}
