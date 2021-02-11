package nl.feedme.api.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final String TABLE_SECRET = "SuchTableSecretMuchSecure";
    public static final long EXPIRATION_TIME = 864_000_00 * 7; // 1 day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "chef/register";
}
