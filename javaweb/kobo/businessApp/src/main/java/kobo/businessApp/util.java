package kobo.businessApp;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import kobo.entities.AuthUser;

public class util {
	public static AuthUser getLoggedInUser() {
		Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (object instanceof AuthUser) {
		return (AuthUser) object;
		} 
		if (object instanceof String) { 
		throw new SessionAuthenticationException(object.toString());
		}

		throw new SessionAuthenticationException("unknown");
		}
}
