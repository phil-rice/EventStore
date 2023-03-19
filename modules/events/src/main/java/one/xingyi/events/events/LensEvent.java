package one.xingyi.events.events;

/** THe object is a json object. aka Map or List or null or String or Number or Boolean */
public record LensEvent(String lens, Object value) implements IEvent {

    @Override
    public boolean isSource() {
        return false;
    }
}
