package one.xingyi.events.utils;

import jakarta.activation.MimeTypeRegistry;
import jakarta.activation.MimetypesFileTypeMap;

import java.util.Map;

import static one.xingyi.events.utils.WrappedException.wrapRunnable;
import static one.xingyi.events.utils.WrappedException.wrapValue;

public interface IMimeHelper {
    String extensionForMime(String mime);

    IMimeHelper defaultMimeHelper = new MimeHelper();
}

class MimeHelper implements IMimeHelper {

    private final Map<String, String> mimes = Map.of(
            "application/json", "json",
            "application/xml", "xml",
            "text/plain", "txt",
            "image/png", "png",
            "image/jpeg", "jpg",
            "image/gif", "gif",
            "image/bmp", "bmp",
            "image/tiff", "tiff"
    );

    @Override
    public String extensionForMime(String mime) {
        return mimes.getOrDefault(mime, "dat");
    }
}