package org.toj.dnd.irctoolkit.engine.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IrcCommand {
    int argsMin();

    int argsMax() default Integer.MAX_VALUE;

    String[] patterns();
}
