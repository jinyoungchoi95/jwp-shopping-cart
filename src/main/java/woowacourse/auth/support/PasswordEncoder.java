package woowacourse.auth.support;

public interface PasswordEncoder {

    String encode(final String password);
}