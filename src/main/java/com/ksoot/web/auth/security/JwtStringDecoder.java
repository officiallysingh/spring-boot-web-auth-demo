package com.ksoot.web.auth.security;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

class JwtStringDecoder  implements JwtDecoder {

	@Override
	public Jwt decode(final String token) throws JwtException {
		return JwtUtils.decodeToken(token);
	}
}
