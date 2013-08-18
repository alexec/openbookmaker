package com.alexecollins.openbookmaker;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Slf4j
public class LifeCycleManager {

	private enum State {READY, STARTING, STARTED, STOPPING}

	private final List<Object> objects = new ArrayList<>();
	private State state = State.READY;

	public <T> T register(T object) {
		log.info("{} {}", state, object);
		objects.add(object);
		return object;
	}

	public void start() throws InvocationTargetException, IllegalAccessException {
		state = State.STARTING;
		invokeAnnotatedMethods(PostConstruct.class);
		state = State.STARTED;
	}

	public void stop() throws InvocationTargetException, IllegalAccessException {
		state = State.STOPPING;
		invokeAnnotatedMethods(PreDestroy.class);
		state = State.READY;
	}

	private void invokeAnnotatedMethods(Class<? extends Annotation> annotationClass) throws IllegalAccessException, InvocationTargetException {
		log.info(state + " {} objects using @{} annotated methods", objects.size(),  annotationClass.getSimpleName());
		for (Object object : objects) {
			log.debug("{} examining {}", state, object);
			for (Method method : object.getClass().getMethods()) {
				log.debug("{} examining {}", state, method);
				if (method.isAnnotationPresent(annotationClass)) {
					log.info(state + " {} {}", object, method.getName());
					method.invoke(object);
				}
			}
		}
		log.info(state + " {} objects", objects.size());
	}

	public boolean contains(Object o) {
		return objects.contains(o);
	}
}
