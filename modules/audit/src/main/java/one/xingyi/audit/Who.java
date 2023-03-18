package one.xingyi.audit;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

@Service
public class Who implements IWho {
    @Override
    public String who(MultiValueMap<String, String> headers) {
        return "anonymous";
    }
}
