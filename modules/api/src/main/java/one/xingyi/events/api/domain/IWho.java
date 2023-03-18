package one.xingyi.events.api.domain;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

public interface IWho {
    String who(MultiValueMap<String,String> headers);
}
