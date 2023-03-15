package one.xingyi.events.lens;

public interface ILensTC<T> {
    ILens<T, T> mapLens(String key);


    ILens<T, T> mapNth(int n);

    ILens<T, T> append();

    ILensTC<Object> jsonLensTc = new JsonLensTC();


}