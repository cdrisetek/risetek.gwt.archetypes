package ${package}.server.accounts;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import ${package}.share.accounts.AccountEntity;
import ${package}.share.accounts.EnumAccount;
import ${package}.share.accounts.hosts.HostProjectRBAC;

@Singleton
public class TemporaryAccount {
	@Inject
	IAccountManagement accountManagement;

	private String temporaryAccountCredential = null;
	private String temporaryAccountPrincipal = null;
	private AccountEntity accountEntity = null;
	private Set<HostProjectRBAC> roles1 = null, roles2 = null;

	public void createTemporaryAccount() {
		if(accountManagement.anyMatchedRole("DEVELOPER"))
			return;

		temporaryAccountPrincipal = "deploy";
		// TODO: random password.
		temporaryAccountCredential = "admin";

		System.out.println("--------------- deploy -----------------------------");
		System.out.println("--------------- deploy -----------------------------");
		System.out.println("--------------- " + temporaryAccountCredential + " -----------------------------");
		System.out.println("--------------- deploy -----------------------------");
		System.out.println("--------------- deploy -----------------------------");
		
		accountEntity = new AccountEntity();
		accountEntity.setPrincipal(temporaryAccountPrincipal);
		accountEntity.setDescription(EnumAccount.NOTES, "Temporary account for bootup");
		accountEntity.setDescription(EnumAccount.EMAIL, "demo@risetek.com");
		roles1 = Arrays.asList(HostProjectRBAC.DEVELOPER, HostProjectRBAC.GUEST).stream().collect(Collectors.toSet());
		roles2 = Arrays.asList(HostProjectRBAC.DEVELOPER, HostProjectRBAC.GUEST).stream().collect(Collectors.toSet());
	}

	/*
	 * should invoke only once.
	 */
	public Object getCredential(Object principal) {
		if(temporaryAccountPrincipal == null || !temporaryAccountPrincipal.equals(principal))
			return null;

		String tmp = temporaryAccountCredential;
		temporaryAccountCredential = null;
		temporaryAccountPrincipal = null;
		return tmp;
	}

	/**
	 * IAuthorizingHandler.doAuthorizationAction
	 * and AuthorizingRealm.doGetAuthorizationInfo both need this, twice.
	 * @param principal
	 * @return
	 */
	public Set<HostProjectRBAC> getRoleSet(Object principal) {
		Set<HostProjectRBAC> tmp;
		if(null != roles1) {
			tmp = roles1;
			roles1 = null;
		} else {
			tmp = roles2;
			roles2 = null;
		}

		return tmp;
	}

	/**
	 * 
	 * ISubjectManagement getSubjectEntity need this, only once.
	 * @return
	 */
	public AccountEntity getAccountEntity() {
		AccountEntity tmp = accountEntity;
		accountEntity = null;
		return tmp;
	}
}
