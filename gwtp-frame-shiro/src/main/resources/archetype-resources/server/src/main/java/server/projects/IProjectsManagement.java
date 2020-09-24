package ${package}.server.projects;

import java.util.List;
import java.util.Set;

import ${package}.share.projects.ProjectEntity;
public interface IProjectsManagement {
	public List<ProjectEntity> ReadProjects(Set<ProjectEntity> projects, String like, int offset, int limit);
	public void CreateProjects(Set<ProjectEntity> projects);
	public void DeleteProjects(Set<ProjectEntity> projects);
}
