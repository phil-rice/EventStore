package one.xingyi.store;

import one.xingyi.audit.Audit;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.events.utils.StringHelper;
import one.xingyi.events.utils.Tuple2;
import one.xingyi.optics.iso.IIso;
import one.xingyi.signedData.SignedData;

import java.util.function.Function;

public record Metadata(String extension, String contentType, Audit audit) {

    private static Function<String, Tuple2<String, String>> splitter = StringHelper.splitIn2("\t");

    public static IIso<String, Metadata> iso(String secret) {
        return SignedData.iso(secret, Metadata.class);
    }
}
