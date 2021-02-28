package ${package}.server;

public class Permissions {
	public enum Permission {CREATE_ACCOUNT};
	protected void requiresPermission(Permission...permission) throws Exception {
		
	}
}
