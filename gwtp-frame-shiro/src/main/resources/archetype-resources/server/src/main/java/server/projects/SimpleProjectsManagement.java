package ${package}.server.projects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import ${package}.share.projects.ProjectEntity;
import ${package}.share.realmgt.RoleEntity;

public class SimpleProjectsManagement implements IProjectsManagement {

	private Map<String, ProjectEntity> projects = new TreeMap<>(); 
	
	@Override
	public List<ProjectEntity> ReadProjects(Set<ProjectEntity> requires, String like, int offset, int limit) {
		List<ProjectEntity> subjects = new Vector<>();
		int start = 0;
		int count = 0;
		int id = 0;
		if(null != requires) {
			requires.forEach(req->{subjects.add(projects.get(req.getName()));});
			return subjects;
		}
		for(Map.Entry<String, ProjectEntity> entry : projects.entrySet()) {
			id++;
			if(!isLike(like, entry))
				continue;

			if(start++ < offset)
				continue;
			
			if(count++ > limit)
				break;

			entry.getValue().setId(id++);
			subjects.add(entry.getValue());
		}
		return subjects;
	}

	@Override
	public void CreateProjects(Set<ProjectEntity> entities) {
		entities.parallelStream().forEach(e->projects.put(e.getName(), e));
	}

	@Override
	public void DeleteProjects(Set<ProjectEntity> entities) {
		entities.parallelStream().forEach(e->projects.remove(e.getName()));
	}

	private boolean isLike(String like, Map.Entry<String, ProjectEntity> entry) {
		if(null == like || null == entry || entry.getKey().indexOf(like) != -1)
			return true;

		ProjectEntity project = entry.getValue();
		if(null == project)
			return true;
		
		if(project.getName().indexOf(like) != -1)
			return true;

		return false;
	}
	
	private void testdata(Map<String, ProjectEntity> projects, int size) {
		for(int count = 0; count < size; count++) {
			ProjectEntity project = new ProjectEntity();
			project.setName(String.format("Project%03d", count));
			if(count % 2 == 0)
				project.setNote("This is a long long long long long long long long"
						+ " long long long long long long long long long long long"
						+ " long long long long long long long long long long long"
						+ " long long note");
			else
				project.setNote("This is a short note");
			
			RoleEntity roles = new RoleEntity();
			roles.addRole("admin").addRole("operator").addRole("viewer");
			project.setRoles(roles);
			
			Map<String, RoleEntity> userRoles = new HashMap<>();
			userRoles.put("wangyc@risetek.com", new RoleEntity().addRole("admin").addRole("operator"));
			userRoles.put("wangyuchun@risetek.com", new RoleEntity().addRole("operator"));
			userRoles.put("risetek@risetek.com", new RoleEntity().addRole("viewer"));
			project.setUserRoles(userRoles);
			
			projects.put(project.getName(), project);
		}
	}
	
	public SimpleProjectsManagement() {
		testdata(projects, 600);
	}
}
