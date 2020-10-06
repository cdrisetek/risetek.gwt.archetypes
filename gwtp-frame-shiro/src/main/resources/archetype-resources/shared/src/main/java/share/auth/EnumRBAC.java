package ${package}.share.auth;

/**
 * Role USER is a basic role, every user get this role automatic.
 * For example, one user granted ADMIN role on Project A, and this
 * user login to Project B, he will be authenticated and grant to role USER,
 * Project B choice how to hander this user.
 * This will give user a chance to require to join new project. 
 * 
 * @author wangyc@risetek.com
 *
 */
public enum EnumRBAC {
	ADMIN, MAINTANCE, OPERATOR, VISITOR, DEVELOPER, USER;
}
