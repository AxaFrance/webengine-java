package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.api.IFunction;
import fr.axa.automation.webengine.global.SettingsWeb;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.function.Function;

@Slf4j
public final class FunctionUtil {

    public static <T, R> R perform(Function<T, R> function) throws Exception {
        return perform(function, null);
    }

    public static <T, R> R perform(Function<T, R> function, T param) throws Exception {
        return function.apply(param);
    }

    public static  <T, R> R retry(IFunction<T, R> function, T param) throws Exception {
        return retry(function,param, SettingsWeb.TIMEOUT_SECONDS);
    }

    public static  <T, R> R retry(IFunction<T, R> function, T param, Integer timeOutInSeconds) throws Exception {
        LocalDateTime timeOut = LocalDateTime.now().plusSeconds(timeOutInSeconds);
        Exception exception = new Exception();

        while (LocalDateTime.now().isBefore(timeOut)) {
            try {
                R r = function.call(param);
                return r;
            } catch (Exception e ) {
                exception = e;
                log.debug("The exception is :"+e.getMessage());
                waitInMillisecondes(SettingsWeb.RETRY_MILLISECONDS);
            }
        }
        throw exception;
    }

    private static void waitInMillisecondes(Long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }
}
