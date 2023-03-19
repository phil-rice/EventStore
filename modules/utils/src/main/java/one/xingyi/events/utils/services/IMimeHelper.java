package one.xingyi.events.utils.services;

import java.util.Map;

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