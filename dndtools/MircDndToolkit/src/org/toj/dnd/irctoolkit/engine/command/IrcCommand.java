package org.toj.dnd.irctoolkit.engine.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IrcCommand {
    String command();
    CommandSegment[] args();
    String summary() default "";
    String desc() default "";

    enum CommandSegment {
        NULLABLE_STRING(true, "string"),
        STRING(false, "string"),
        NULLABLE_INT(true, "int"),
        INT(false, "int"),
        NULLABLE_DOUBLE(true, "double"),
        DOUBLE(false, "double"),
        NULLABLE_LIST(true, "list"),
        LIST(false, "list");

        private boolean nullable;
        private String type;
        private String value;

        CommandSegment(boolean nullable, String type) {
            this.nullable = nullable;
            this.type = type;
        }

        public boolean isNullable() {
            return nullable;
        }

        public String type() {
            return type;
        }

        public String value() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
