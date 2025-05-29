package at.ac.fhcampuswien.fhmdb.factory;

import javafx.util.Callback;
import java.util.HashMap;
import java.util.Map;

public class ControllerFactory implements Callback<Class<?>, Object> {
    private final Map<Class<?>, Object> singletons = new HashMap<>();

    @Override
    public Object call(Class<?> controllerClass) {
        return singletons.computeIfAbsent(controllerClass, requestedController -> {
            try {
                return requestedController.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Could not create controller: " + requestedController.getName(), e);
            }
        });
    }
}