package ${package}.share.auth.projects;

import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;
import ${package}.share.GetResults;

public class ProjectAction extends UnsecuredActionImpl<GetResults<ProjectEntity>> {

	public static enum OP implements IsSerializable {READ, UPSERT, DELETE, ENABLE};
	protected ProjectAction() {};
	
	/**
	 * @param projects
	 * @param op
	 * @param sequence client request sequence to identify newest result.
	 */
	public ProjectAction(Set<ProjectEntity> projects, OP op, String like, int offset, int limit, long sequence) {
		this.projects = projects;
		this.op = op;
		this.like = like;
		this.offset = offset;
		this.limit = limit;
		this.sequence = sequence;
	}
	
	public String like;
	public int offset;
	public int limit;
	public Set<ProjectEntity> projects;
	public OP op;
	public long sequence;
}
