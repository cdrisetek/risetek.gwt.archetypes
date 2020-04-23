package ${package}.realmgt;

import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;
import ${package}.GetResults;

public class SubjectAction extends UnsecuredActionImpl<GetResults<SubjectEntity>> {

	public static enum OP implements IsSerializable { CREATE, READ, UPDATE, DELETE, ENABLE, DISABLE }; 	

	protected SubjectAction(){}

	
	public SubjectAction(Set<SubjectEntity> subjects, OP op, int offset, int size, String like, long sequence) {
		this.subjects = subjects;
		this.op = op;
		this.offset = offset;
		this.size = size;
		this.like = like;
		this.sequence = sequence;
	}
	
	public long sequence;
	public int offset;
	public int size;
	public String like;
	public OP op;
	public Set<SubjectEntity> subjects;
}
