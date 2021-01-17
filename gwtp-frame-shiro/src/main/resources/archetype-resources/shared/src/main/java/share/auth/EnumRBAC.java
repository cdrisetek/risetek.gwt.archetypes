package ${package}.share.auth;

/**
 * Role USER is a basic role, every user get this role automatic.
 * For example, one user granted ADMIN role on Project A, and this
 * user login to Project B, he will be authenticated and grant to role USER,
 * Project B choice how to hander this user.
 * This will give user a chance to require to join new project. 
 * 
 * @author wangyc@risetek.com
 *
 */
public enum EnumRBAC {
	ADMIN {
		@Override
		public String toString() {
			return "系统管理";
		}
	}, MAINTANCE {
		@Override
		public String toString() {
			return "系统维护";
		}
	}, OPERATOR {
		@Override
		public String toString() {
			return "操作";
		}
	}, VISITOR {
		@Override
		public String toString() {
			return "访客";
		}
	}, DEVELOPER {
		@Override
		public String toString() {
			return "开发";
		}
	}, USER {
		@Override
		public String toString() {
			return "用户";
		}
	};
}
