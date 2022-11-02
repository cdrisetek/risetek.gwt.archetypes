package ${package}.share.accounts.projects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;
import ${package}.share.dispatch.GetResults;

public class ProjectAction extends UnsecuredActionImpl<GetResults<ProjectEntity>> {

	public static enum OP implements IsSerializable {READ, UPSERT};
	protected ProjectAction() {};
	
	/**
	 * @param projects
	 * @param op
	 * @param sequence client request sequence to identify newest result.
	 */
	public ProjectAction(Set<ProjectEntity> projects, OP op, String like, int offset, int limit) {
		this.projects = projects;
		this.op = op;
		this.like = like;
		this.offset = offset;
		this.limit = limit;
		this.sequence = sequence_inc++;
	}
	
	public ProjectAction(ProjectEntity...entities) {
		projects = new HashSet<>(Arrays.asList(entities));
		op = OP.READ;
		like = null;
		offset = 0;
		limit = 1;
		this.sequence = sequence_inc++;
	}
	
	private static long sequence_inc = 0;
	public String like;
	public int offset;
	public int limit;
	public Set<ProjectEntity> projects;
	public OP op;
	public long sequence;
}
