package ${package}.share.accounts.projects;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProjectEntity implements IsSerializable {
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

	// Shoud be Unique
	@NotNull @Size(max=64)
	private String name;
	private Map<String, String> descriptions;
}
