package one.xingyi.events.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.function.Supplier;

public class WrappedException extends RuntimeException {
    public WrappedException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public static RuntimeException wrap(Throwable t) {
        if (t instanceof RuntimeException) return (RuntimeException) t;
        return new WrappedException(t);
    }

    public static <T, E extends Exception> T wrapValue(SupplierWithException<T, E> t) {
        try {
            return t.get();
        } catch (Exception e) {
            throw wrap(e);
        }
    }

    public static <T, E extends Exception> Supplier<T> wrapSupplier(SupplierWithException<T, E> t) {
        return () -> {
            try {
                return t.get();
            } catch (Exception e) {
                throw wrap(e);
            }
        };
    }

    public static <E extends Exception> void wrapRunnable(RunnableWithException<E> runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw wrap(e);
        }
    }

    public static <From, To, E extends Throwable> Function<From, To> wrapFn(FunctionWithException<From, To, E> fn) {
        return from -> {
            try {
                return fn.apply(from);
            } catch (Error e) {
                throw e;
            } catch (Throwable e) {
                throw wrap(e);
            }
        };
    }

    public static Throwable unwrap(Throwable t) {
        if (t instanceof WrappedException) return unwrap(t.getCause());
        if (t instanceof CompletionException) return unwrap(t.getCause());
        return t;
    }
}
