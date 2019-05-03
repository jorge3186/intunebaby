package com.chunkymonkey.itb.constants;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Common security constants that can be used throughout the
 * application.
 * 
 * @author alphonso.jordan2
 *
 */
public class SecurityConstants {

	/** 
	 * The Server scope.<br>
	 * Used as the value of a {@linkplain PreAuthorize}
	 * annotation.<br><br>
	 *  
	 * {@code #oauth2.hasScope('server')} 
	 */
	public static final String SERVER_SCOPE 		= 	"#oauth2.hasScope('server')";
	
	/**
	 * The UI scope.<br>
	 * Used as the value of a {@linkplain PreAuthorize}
	 * annotation.<br><br>
	 * 
	 * {@code #oauth2.hasScope('ui')}<br>
	 */
	public static final String UI_SCOPE 			=	"#oauth2.hasScope('ui')";
	
	/**
	 * The authenicated expression.<br>
	 * Used as the value f a {@linkplain PreAuthorize}
	 * annotation.<br><br>
	 * 
	 * {@code isAuthenticated()}
	 */
	public static final String AUTHENTICATED		=	"isAuthenticated()";
	
}
