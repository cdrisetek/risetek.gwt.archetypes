package ${package}.server.projects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import com.google.inject.Singleton;
import ${package}.server.auth.HasAuthorization;
import ${package}.share.UniqueID;
import ${package}.share.projects.ProjectEntity;
import ${package}.share.users.EnumUserDescription;

@Singleton
public class SimpleProjectsManagement implements IProjectsManagement {
	public final static Map<String, Project> projects = new TreeMap<>(); 
	
	public SimpleProjectsManagement() {
		loadProjects();
	}

	private final static Map<UniqueID, Map<String, String>> descriptions = new HashMap<>();
	private final static Map<UniqueID, Map<UniqueID, Set<String>>> authorizations = new HashMap<>();

	private static class Project implements HasAuthorization {
		private final UniqueID ID = new UniqueID();
		private String name;
		private Set<String> roleSet = new HashSet<>();

		public Project(String name) {
			this.name = name;
		}

		public Project append() {
			projects.put(name, this);
			return this;
		}
		
		public ProjectEntity getSummaryProjectEntity() {
			ProjectEntity entity = new ProjectEntity();
			entity.setName(name);
			Optional.ofNullable(descriptions.get(ID)).ifPresent(m->entity.setNote(m.get(EnumUserDescription.NOTES.name())));
			return entity;
		}

		public ProjectEntity getProjectEntity() {
			ProjectEntity entity = getSummaryProjectEntity();
			entity.setRoleSet(roleSet);
			Optional.ofNullable(authorizations.get(ID)).ifPresent(m->entity.setRoles(m));
			return entity;
		}

		public Project setDescription(String key, String value) {
			Map<String, String> description = Optional.ofNullable(descriptions.get(ID))
					.orElseGet(()->{descriptions.put(ID, new HashMap<>());return descriptions.get(ID);});

			description.put(key, value);
			return this;
		}
		
		@Override
		public Set<String> getUserRoles(UniqueID id) {
			Map<UniqueID, Set<String>> authorization = authorizations.get(ID);
			if(null == authorization)
				return null;

			return authorization.get(id);
		}
		
		@Override
		public Project addRoleSet(String role) {
			roleSet.add(role);
			return this;
		}
		
		public boolean isLike(String like) {
			if(null == like)
				return true;

			if(name.indexOf(like) != -1)
				return true;
			
			Map<String, String> description = descriptions.get(ID);
			
			if((null != description) && description.values().stream().anyMatch(v->v.indexOf(like) != -1))
				return true;

			return false;
		}
	}	
	@Override
	public List<ProjectEntity> readProjects(Set<ProjectEntity> requires, String like, int offset, int limit) {
		List<ProjectEntity> entities = new Vector<>();

		// For given entity, we fill all attribute to it.
		if(null != requires) {
			requires.forEach(e->Optional.ofNullable(projects.get(e.getName())).ifPresent(c->entities.add(c.getProjectEntity())));
			return entities;
		}

		// Only fill basic attribute for project entity.
		int start = 0, count = 0;
		for(Project project:projects.values()) {
			ProjectEntity entity = project.getSummaryProjectEntity();

			if(!project.isLike(like))
				continue;
			
			if(start++ < offset)
				continue;
			
			if(count++ > limit)
				break;

			entities.add(entity);
		}

		return entities;
	}

	@Override
	public void createProjects(Set<ProjectEntity> entities) {
		entities.parallelStream().forEach(e->{
			Project project = new Project(e.getName()).append();
			project.setDescription(EnumUserDescription.NOTES.name(), e.getNote());
			// TODO: roleSet or method to update roleSet? account role?
			}
		);
	}

	@Override
	public void deleteProjects(Set<ProjectEntity> entities) {
		entities.parallelStream().forEach(e->projects.remove(e.getName()));
	}

	// for Test only 
	private static void loadProjects() {
		for(int index = 0; index < 60; index++) {
			String projectName = String.format("Project%03d", index);
			Project project = new Project(projectName).addRoleSet("admin")
					         .addRoleSet("maintance").addRoleSet("viewer").append();

			if(index % 2 == 0) {
				project.setDescription(EnumUserDescription.NOTES.name(), "This is a long long long long long long long long"
						+ " long long long long long long long long long long long"
						+ " long long long long long long long long long long long"
						+ " long long note");
				project.addRoleSet("admin").addRoleSet("operator").addRoleSet("viewer");
			} else {
				project.setDescription(EnumUserDescription.NOTES.name(), "This is a short note");
				project.addRoleSet("operator").addRoleSet("viewer");
			}
		}
	}
}
