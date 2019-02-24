package de.frittenburger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.frittenburger.impl.UserRepositoryImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = LogManager
			.getLogger(UserDetailsServiceImpl.class);

	@Override
	public UserDetails loadUserByUsername(String username) {

		try {
			Map<String, String> user = UserRepositoryImpl.getInstance().read(
					username, "user");
			
			if (user == null)
				throw new UsernameNotFoundException(username);

			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			for (String role : user.get("roles").split(";")) {
				if (role.trim().equals(""))
					continue;
				grantedAuthorities.add(new SimpleGrantedAuthority(role.trim()));
			}

			return new User(user.get("username"), user.get("password"),
					grantedAuthorities);

		} catch (IOException e) {
			logger.error(e);
		}
		throw new UsernameNotFoundException(username);
	}
}
