package org.koreait.member.test.annotations;

import org.koreait.member.constants.Authority;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory.class)
public @interface MockMember {
    long seq() default 1L;
    String email() default "user01@test.org";
    String password() default "_aA123456";
    String name() default "사용자01";
    String nickName() default "닉네임01";
    Authority[] authority() default {Authority.USER};
}
