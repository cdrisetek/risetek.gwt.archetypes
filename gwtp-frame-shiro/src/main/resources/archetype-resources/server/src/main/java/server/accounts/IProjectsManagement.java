package ${package}.server.accounts;

import java.util.List;
import java.util.Set;

import com.gwtplatform.dispatch.shared.ActionException;
import ${package}.share.accounts.projects.ProjectEntity;
public interface IProjectsManagement {
	// Management functions
	List<ProjectEntity> readProjects(Set<ProjectEntity> projects, String like, int offset, int limit) throws ActionException;
	void updateOrInsert(Set<ProjectEntity> entities) throws ActionException;
	
	// DevOps
	void clearDatas() throws ActionException;
}
