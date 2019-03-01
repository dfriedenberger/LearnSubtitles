package de.frittenburger;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import de.frittenburger.impl.UserRepositoryImpl;

@Component
public class LoginAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler  {
		
	    @Autowired
	    private LocaleResolver localeResolver;
	 
		private static final Logger logger = LogManager
			.getLogger(LoginAuthenticationSuccessHandler.class);
	
	    @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

			String userId = SecurityContextHolder.getContext().getAuthentication().getName();

			Map<String, String> user = UserRepositoryImpl.getInstance().read(
					userId, "configuration");
						
	    	logger.info("successful login for {} {}",userId,user);
	    	if(user != null && user.containsKey("language"))
	    	{
				Locale userLocale = new Locale(user.get("language"));
				localeResolver.setLocale(request, response, userLocale);

	    		
	    	}
	    	
	    	super.onAuthenticationSuccess(request, response, authentication);
	    }
}



