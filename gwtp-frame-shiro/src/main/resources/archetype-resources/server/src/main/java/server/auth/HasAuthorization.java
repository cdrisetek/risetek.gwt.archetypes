package ${package}.server.auth;

import java.util.Set;

import ${package}.share.UniqueID;

/**
 * 本接口提供用户管理中实现对角色的管理功能。
 * 
 * @author wangyc@risetek.com
 *
 */
public interface HasAuthorization {
	public Set<String> getUserRoles(UniqueID id);
	public HasAuthorization addRoleSet(String role);
}
