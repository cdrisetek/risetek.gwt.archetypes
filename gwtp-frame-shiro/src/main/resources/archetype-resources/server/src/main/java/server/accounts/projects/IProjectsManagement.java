package ${package}.server.accounts.projects;

import java.util.List;
import java.util.Set;

import ${package}.share.accounts.projects.ProjectEntity;
public interface IProjectsManagement {
	// Management functions
	List<ProjectEntity> readProjects(Set<ProjectEntity> projects, String like, int offset, int limit);
	void updateOrInsert(Set<ProjectEntity> entities) throws Exception;
}
