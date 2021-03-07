package ${package}.server.accounts.projects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.google.inject.Singleton;
import ${package}.server.accounts.roles.IRoleManagement;
import ${package}.share.accounts.projects.EnumProject;
import ${package}.share.accounts.projects.ProjectEntity;
import ${package}.share.accounts.roles.EnumRBAC;

@Singleton
public class SimpleProjectsManagement implements IProjectsManagement {
	public IRoleManagement roleManagement;
	
	@Inject
	public SimpleProjectsManagement(final IRoleManagement roleManagement) {
		this.roleManagement = roleManagement;

		// for Test only 
		for(int index = 0; index < 60; index++) {
			String projectName = String.format("Project%03d", index);
			ProjectRecord project = new ProjectRecord(projectName).setRole(EnumRBAC.ADMIN)
					         .setRole(EnumRBAC.MAINTANCE).setRole(EnumRBAC.GUEST);

			if(index % 2 == 0) {
				project.setDescription(EnumProject.NOTES, "This is a long long long long long long long long"
						+ " long long long long long long long long long long long"
						+ " long long long long long long long long long long long"
						+ " long long note");
			} else {
				project.setDescription(EnumProject.NOTES, "This is a short note");
				project.setRole(EnumRBAC.OPERATOR);
			}
		}
	}

	// Records of Projects
	// Map key for project name
	// subMap key for project descriptions.
	private final static Map<Object, Map<String, String>> descriptionSTORE = new HashMap<>();
	private final static Map<Object, ProjectRecord> projectSTORE = new TreeMap<>(); 

	private class ProjectRecord {
		private String name;

		ProjectRecord(String name) {
			this.name = name;
			projectSTORE.put(name, this);
		}

		ProjectEntity getProjectEntity() {
			ProjectEntity entity = new ProjectEntity();
			entity.setName(name);
			Optional.ofNullable(descriptionSTORE.get(name)).ifPresent(m->entity.setDescription(m));
			return entity;
		}

		ProjectRecord setDescription(EnumProject key, String value) {
			Map<String, String> description = Optional.ofNullable(descriptionSTORE.get(name))
					.orElseGet(()->{descriptionSTORE.put(name, new HashMap<>());return descriptionSTORE.get(name);});

			description.put(key.name(), value);
			return this;
		}

		boolean isLike(String like) {
			if(null == like || name.indexOf(like) != -1)
				return true;
			
			Map<String, String> description = descriptionSTORE.get(name);
			
			if((null != description) && description.values().stream().anyMatch(v->v.indexOf(like) != -1))
				return true;

			return false;
		}

		ProjectRecord setRole(EnumRBAC role) {
			Set<String> roles = roleManagement.getRoleSet(null, name);
			roles.add(role.name());
			roleManagement.setRoleSet(null, name, roles);
			
			return this;
		}
	}

	@Override
	@RequiresPermissions("projects:read")
	public List<ProjectEntity> readProjects(Set<ProjectEntity> requires, String like, int offset, int limit) {
		List<ProjectEntity> entities = new Vector<>();

		// For given entity, we fill all attribute to it.
		if(null != requires) {
			requires.forEach(e->Optional.ofNullable(projectSTORE.get(e.getName())).ifPresent(c->entities.add(c.getProjectEntity())));
			return entities;
		}

		// Only fill basic attribute for project entity.
		int start = 0, count = 0;
		for(ProjectRecord project:projectSTORE.values()) {
			ProjectEntity entity = project.getProjectEntity();
			if(!project.isLike(like) || start++ < offset)
				continue;
			
			if(count++ >= limit)
				break;

			entities.add(entity);
		}

		return entities;
	}

	@Override
	@RequiresPermissions("projects:create,update")
	public void updateOrInsert(Set<ProjectEntity> entities) throws Exception {
		entities.stream().forEach(entity -> {
			// ensure project exist.
			Optional.ofNullable(projectSTORE.get(entity.getName())).orElseGet(() -> {
				// create one
				ProjectRecord newproject = new ProjectRecord(entity.getName());
				projectSTORE.put(entity.getName(), newproject);
				return projectSTORE.get(entity.getName());
			});

			// ensure description map exist.
			Map<String, String> description = Optional.ofNullable(descriptionSTORE.get(entity.getName())).orElseGet(() -> {
				descriptionSTORE.put(entity.getName(), new HashMap<>());
				return descriptionSTORE.get(entity.getName());
			});
			
			// insert or update descriptions with no-empty-value and remove description with empty-value.
			Optional.of(entity.getDescriptions()).ifPresent(newDescriptions -> {
				// Split input descriptions Map into null-value-map and none-null-value-map.
				Map<Boolean, Map<String, String>> groups = newDescriptions.entrySet().stream()
						.collect(Collectors.partitioningBy(e -> (null == e.getValue() || e.getValue().isEmpty()),
						         Collectors.toMap(e -> e.getKey(), e -> null == e.getValue() ? "" : e.getValue() )));
				
				// Update descriptions items when key with none-null-value
				description.putAll(groups.get(Boolean.valueOf(false /* value not empty */)));

				// Remove descriptions items when key with null-value.
				Optional.of(groups.get(Boolean.valueOf(true /* value is empty */)))
				  .ifPresent(m -> m.entrySet().stream().forEach(e -> description.remove(e.getKey())));
			});
		});
	}

	@Override
	public String provider() {
		return "Memory based SimpleProjectsManagement";
	}
}
