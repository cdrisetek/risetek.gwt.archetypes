package ${package}.server.accounts;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.eclipse.microprofile.config.Config;

import ${package}.share.accounts.AccountEntity;
import ${package}.share.accounts.EnumAccount;
import ${package}.share.accounts.hosts.HostProjectRBAC;
import java.util.logging.Logger;

public class TemporaryAccount {
	private IAuthorizingHandler authorizing;
	private String temporaryAccountCredential = null;
	private String temporaryAccountPrincipal = null;
	private AccountEntity accountEntity = null;
	private Set<HostProjectRBAC> roles = null;

	public TemporaryAccount(String account, String password, IAuthorizingHandler authorizing) {
		this.authorizing = authorizing;
		temporaryAccountPrincipal = account;
		temporaryAccountCredential = password;
		accountEntity = new AccountEntity();
		accountEntity.setPrincipal(temporaryAccountPrincipal);
		accountEntity.setDescription(EnumAccount.NOTES, "Temporary account for bootup");
		accountEntity.setDescription(EnumAccount.EMAIL, "deploy@noanswer.com");
		roles = Arrays.asList(HostProjectRBAC.DEVELOPER, HostProjectRBAC.GUEST).stream().collect(Collectors.toSet());
	}

	public Set<HostProjectRBAC> getRoleSet(Object principal) {
		return roles;
	}

	public AccountEntity getAccountEntity(String principal, Object credential) throws OAuthProblemException {
		if (null == accountEntity || !principal.equals(temporaryAccountPrincipal))
			return null;
		if (authorizing.passwordsMatch(credential, temporaryAccountCredential))
			return accountEntity;
		throw OAuthProblemException.error("invalid password");
	}

	@Singleton
	public static class deployAccountProvider implements Provider<TemporaryAccount> {
		static Logger logger = Logger.getLogger(deployAccountProvider.class.getName());
		@Inject
		Config config;
		@Inject
		IAuthorizingHandler authorizing;

		@Override
		public TemporaryAccount get() {
			logger.info("========= deploy config is:" + config);
			String account = config.getOptionalValue("deploy.account", String.class).orElse(null);
			if (null == account)
				return null;

			String password = config.getOptionalValue("deploy.password", String.class).orElse(null);
			if (null == password)
				return null;

			logger.info(" Deploy account generated");
			return new TemporaryAccount(account, authorizing.encryptRealmPassword(password), authorizing);
		}
	}
}
