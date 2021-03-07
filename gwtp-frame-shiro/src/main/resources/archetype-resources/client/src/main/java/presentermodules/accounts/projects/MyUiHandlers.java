package ${package}.presentermodules.accounts.projects;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.gwtplatform.mvp.client.UiHandlers;

interface MyUiHandlers extends UiHandlers {
	public enum ProjectValidate {EMPTY, CHECKING, VALIDATE, INVALIDATE}

	void onGoBackPlace();

	void onAccountSelect();

	void onProjectSelect();
	void onProjectCreate();
	void onProjectEdit();
	void onProjectRole();
	
	void createProject(String name, Map<String, String> descriptions);
	void updateProject(String name, Map<String, String> descriptions);
	void updateProjectRoleSet(String project, Set<String> roles);
	void grantAccountRoles(String account, String project, Set<String> roles);

	void getProject(BiConsumer<String, Map<String, String>> project);
	void getProjectRoles(BiConsumer<String, Set<String>> role);
	void getRoles(BiConsumer<String, String> names, BiConsumer<Set<String>, Set<String>> roleSet);
	
	void checkValidate(String value, Consumer<ProjectValidate> state);
}
