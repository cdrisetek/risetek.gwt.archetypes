package ${package}.share.accounts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.type.JavaObjectType;

import com.google.gwt.user.client.rpc.GwtTransient;
import com.google.gwt.user.client.rpc.IsSerializable;

@TypeDefs({@TypeDef(name = "other", typeClass = JavaObjectType.class)})
@NamedQueries({
	@NamedQuery(name = "Account.findAll", query = "SELECT principal FROM AccountEntity"),
	@NamedQuery(name = "Account.updatePassword", query = "UPDATE AccountEntity SET password = ?1 WHERE principal = ?2"),
	@NamedQuery(name = "Account.updateDescriptions", query = "UPDATE AccountEntity SET descriptions=?1 WHERE principal=?2"),
})
@DynamicUpdate

@Entity
public class AccountEntity implements Serializable, IsSerializable, Comparable<AccountEntity> {
	private static final long serialVersionUID = 4943388498910435482L;
	/*
	 * @Id
	 * 
	 * @GeneratedValue(strategy = GenerationType.IDENTITY)
	 * 
	 * @Column(updatable = false, nullable = false) public Long id;
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NaturalId
	@Column(nullable = false, unique = true, updatable = false, length=40)
	public String principal;

	@Column(nullable = false, length=100)
	@GwtTransient
	public String password;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Column(columnDefinition="OTHER")
	@Type(type = "other")
	public Map<String, String> descriptions;

	public Map<String, String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(Map<String, String> descriptions) {
		this.descriptions = descriptions;
	}

	public void setDescription(String key, String value) {
		if(null == descriptions)
			descriptions = new HashMap<>();
		descriptions.put(key, value);
	}

	public void setDescription(EnumAccount attr, String value) {
		setDescription(attr.name(), value);
	}

	public String getDescription(EnumAccount attr) {
		if(null == descriptions)
			return null;
		return descriptions.get(attr.name());
	}

	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	@Override
	public int compareTo(AccountEntity o) {
		return getPrincipal().compareTo(o.getPrincipal());
	}
}
