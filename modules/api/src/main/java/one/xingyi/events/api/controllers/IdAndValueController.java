package one.xingyi.events.api.controllers;

import one.xingyi.audit.Audit;
import one.xingyi.audit.IWho;
import one.xingyi.events.utils.ITime;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.events.utils.NullHelper;
import one.xingyi.store.IIdAndValueStore;
import one.xingyi.store.Metadata;
import one.xingyi.store.PutResult;
import one.xingyi.store.ValueAndMetadata;
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

    public IdAndValueController(@Autowired IIdAndValueStore store, @Autowired IWho who, @Autowired ITime time) {
        this.store = store;
        this.who = who;
        this.time = time;
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
    public CompletableFuture<PutResult> putValue(@RequestBody byte[] bytes, @RequestHeader HttpHeaders headers) {
        var contentType = headers.getContentType();
        var vm = new ValueAndMetadata(bytes, new Metadata(".dat", NullHelper.mapOr(contentType, MimeType::toString, "application/octet-stream"),
                new Audit(who.who(headers), time.time(), "put")));
        return store.put(vm);
    }
}
