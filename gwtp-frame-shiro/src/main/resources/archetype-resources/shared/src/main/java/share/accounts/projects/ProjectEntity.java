package ${package}.share.accounts.projects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.type.JavaObjectType;

import com.google.gwt.user.client.rpc.IsSerializable;

@TypeDefs({@TypeDef(name = "other", typeClass = JavaObjectType.class)})
@DynamicUpdate

@Entity
public class ProjectEntity implements Serializable, IsSerializable, Comparable<ProjectEntity> {
	private static final long serialVersionUID = 7962053254007801699L;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(EnumProject attr, String value) {
		if(null == descriptions)
			descriptions = new HashMap<>();
		descriptions.put(attr.name(), value);
	}

	public void setDescription(Map<String, String> descriptions) {
		if(null == this.descriptions)
			this.descriptions = new HashMap<>();
		this.descriptions.putAll(descriptions);
	}

	public String getDescription(EnumProject attr) {
		if(null == descriptions)
			return null;
		return descriptions.get(attr.name());
	}
	
	public Map<String, String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(Map<String, String> descriptions) {
		this.descriptions = descriptions;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NaturalId
	@Column(nullable = false, unique = true, updatable = false, length=64)
	private String name;

	@Column(columnDefinition="OTHER")
	@Type(type = "other")
	private Map<String, String> descriptions;

	@Override
	public int compareTo(ProjectEntity o) {
		return getName().compareTo(o.getName());
	}
}
