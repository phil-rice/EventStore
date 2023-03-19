package one.xingyi.event.audit;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

public interface IWho {
    String who(MultiValueMap<String,String> headers);
}
