package fr.axa.automation.webengine.api;

public interface IFunction<T,R> {
    R call(T t) throws Exception;
}
