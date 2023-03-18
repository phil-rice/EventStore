package one.xingyi.events.api.controllers;

import jakarta.activation.MimetypesFileTypeMap;
import one.xingyi.audit.Audit;
import one.xingyi.audit.IWho;
import one.xingyi.events.utils.IMimeHelper;
import one.xingyi.events.utils.ITime;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.events.utils.NullHelper;
import one.xingyi.store.IIdAndValueStore;
import one.xingyi.store.Metadata;
import one.xingyi.store.PutResult;
import one.xingyi.store.ValueAndMetadata;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
public class IdAndValueController {

    public final IIdAndValueStore store;
    private IWho who;
    private ITime time;
    private IMimeHelper mimeHelper;

    public IdAndValueController(@Autowired IIdAndValueStore store, @Autowired IWho who, @Autowired ITime time,
                                @Autowired IMimeHelper mimeHelper) {
        this.store = store;
        this.who = who;
        this.time = time;
        this.mimeHelper = mimeHelper;
    }

    ResponseEntity<byte[]> fromValueAndMetadata(ValueAndMetadata valueAndMetadata) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, valueAndMetadata.metadata().contentType())
                .header("x-metadata", JsonHelper.printJson(valueAndMetadata.metadata()))
                .body(valueAndMetadata.value());
    }

    @GetMapping(value = "/value/{id}")
    public CompletableFuture<ResponseEntity<byte[]>> getValue(@PathVariable String id) {
        return store.get(id).thenApply(this::fromValueAndMetadata);
    }

    @PostMapping(value = "/value")
    public CompletableFuture<PutResult> putValue(@RequestBody byte[] bytes, @RequestParam(required = false) String why, @RequestHeader HttpHeaders headers) {
        String contentType = NullHelper.mapOr(headers.getContentType(), MimeType::toString, "application/octet-stream");
        var mime = mimeHelper.extensionForMime(contentType);
        var vm = new ValueAndMetadata(bytes, new Metadata(mime, contentType,
                new Audit(who.who(headers), time.time(), why)));
        return store.put(vm);
    }
}
