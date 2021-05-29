package ${package}.share.accounts.roles;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.type.JavaObjectType;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * Key是账户名称和项目名称组合的Hash
 * 1、当账户名称和项目名称组合在一起，表明这个RoleSet是该项目对该账户的授权。
 * 2、存在没有账户的情况，这是项目RoleSet的全部。
 * 3、也存在没有项目的情况，这是本地项目（HostProject）对账户的RoleSet授权。
 * 
 * @author wangyc@risetek.com
 *
 */

@TypeDefs({@TypeDef(name = "other", typeClass = JavaObjectType.class)})
@DynamicUpdate

@Entity
public class RoleEntity implements Serializable, IsSerializable {
	private static final long serialVersionUID = -6843590561630553627L;

	public RoleEntity addRoleSet(Set<String> roles) {
		this.roleSet = roles;
		return this;
	}

	public Set<String> getRoleSet() {
		return roleSet;
	}

	@Id
	@Column(nullable = false, unique = true, updatable = false, length=128)
	public String key;

	@NaturalId
	@Column(nullable = true, updatable = false, length=40)
	public String principal;

	@NaturalId
	@Column(nullable = true, updatable = false, length=64)
	public String project;

	@Column(columnDefinition="OTHER")
	@Type(type = "other")
	private Set<String> roleSet;
}
