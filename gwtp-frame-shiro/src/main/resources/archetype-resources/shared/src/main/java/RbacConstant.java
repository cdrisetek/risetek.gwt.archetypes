package ${package};

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RbacConstant {
	public static List<String> roles = Stream.of("admin", "maintenance", "operator", "visitor", "developer")
			.collect(Collectors.toCollection(ArrayList::new));

	public static boolean isValidRole(String role) {
		return (roles.contains(role));
	}

	class RbacInfo {
		String chineseName;
		String explain;
	}

	static {
		HashMap<String, RbacInfo> rbacInfo = new HashMap<>();
	}
	
	
}
