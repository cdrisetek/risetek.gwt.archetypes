package ${package}.share.accounts.hosts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 每一个项目开发，都需要设定项目本身的role及其permissions
 * 服务端按照这个设定进行授权
 * 
 * @author wangyc@risetek.com
 *
 */
public enum HostProjectRBAC {
	ADMIN {
		@Override
		public String toString() {
			return "系统管理";
		}

		private final Set<String> permissions = new HashSet<>(Arrays.asList(
				"accounts:create,update,read",
				"projects:create,update,read"));
		@Override
		public
		Set<String> permissions() {
			return permissions;
		}
	}, MAINTANCE {
		@Override
		public String toString() {
			return "系统维护";
		}
		private final Set<String> permissions = new HashSet<>(Arrays.asList(
				"accounts:create,update,read",
				"projects:create,update,read"));
		@Override
		public
		Set<String> permissions() {
			return permissions;
		}
	}, OPERATOR {
		@Override
		public String toString() {
			return "操作";
		}
		private final Set<String> permissions = new HashSet<>(Arrays.asList(
				"accounts:create,update,read",
				"projects:create,update,read"));
		@Override
		public
		Set<String> permissions() {
			return permissions;
		}
	}, DEVELOPER {
		@Override
		public String toString() {
			return "开发";
		}
		private final Set<String> permissions = new HashSet<>(Arrays.asList(
				"accounts:create,update,read",
				"projects:create,update,read"));
		@Override
		public
		Set<String> permissions() {
			return permissions;
		}
	}, GUEST {
		@Override
		public String toString() {
			return "用户";
		}
		private final Set<String> permissions = new HashSet<>(Arrays.asList(
				"subject:update,read"));
		@Override
		public
		Set<String> permissions() {
			return permissions;
		}
	};
	
	abstract public Set<String> permissions();
}
