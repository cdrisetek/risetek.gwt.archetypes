package ${package}.server.shiro;

public interface IOAuthConfig {
	public final static String client_id = "client-id";
	public final static String client_id_local = "local";
	public final static String client_secret = "client-secret";
	public final static String realm = "realm oauth";
	public final static String issuer = "oauth.com";
	public final static String jwtSignature = "oauth corp.";
	public final long expiration_time = 1000 * 60 * 60 * 24 * 5;

	public final static String state_key = "oauth-state";
	public final static String code_key = "oauth-code";
	public final static String rememberme_key = "rememberme";

	// OAuth server Location.
	// 如果将 OAuth sever 位置设定为 null，那么这个项目具有本地服务能力
	public final static String endpointLocation = null; // "http://devops.yun74.com";
	
	public final static String loginURI = "/app/login.html"; 
	public final static String authzURI = "/oauth/authz";
	public final static String tokenURI = "/oauth/token";
}
