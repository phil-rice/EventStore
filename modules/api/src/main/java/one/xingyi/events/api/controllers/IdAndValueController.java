package one.xingyi.events.api.controllers;

import one.xingyi.event.audit.Audit;
import one.xingyi.event.audit.IWho;
import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.events.utils.helpers.NullHelper;
import one.xingyi.events.utils.services.IMimeHelper;
import one.xingyi.events.utils.services.ITime;
import one.xingyi.store.idvaluestore.IIdAndValueStore;
import one.xingyi.store.idvaluestore.Metadata;
import one.xingyi.store.idvaluestore.PutResult;
import one.xingyi.store.idvaluestore.ValueAndMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
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
