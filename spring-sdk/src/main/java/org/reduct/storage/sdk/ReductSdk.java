package org.reduct.storage.sdk;

import org.reduct.storage.sdk.config.ReductConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ReductConfig.class)
public @interface ReductSdk {
}
