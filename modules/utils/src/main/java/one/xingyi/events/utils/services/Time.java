package one.xingyi.events.utils.services;

import org.springframework.stereotype.Service;

@Service
public class Time implements ITime {
    @Override
    public long time() {
        return System.currentTimeMillis();
    }

}
