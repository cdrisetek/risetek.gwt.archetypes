package ${package}.server.projects;

import java.util.List;
import java.util.Set;

import ${package}.share.projects.ProjectEntity;
public interface IProjectsManagement {
	public List<ProjectEntity> readProjects(Set<ProjectEntity> projects, String like, int offset, int limit);
	public void createProjects(Set<ProjectEntity> projects);
	public void deleteProjects(Set<ProjectEntity> projects);
}
