package fr.axa.automation.webengine.core;

public interface IFunction<T,R> {
    R call(T t) throws Exception;
}
