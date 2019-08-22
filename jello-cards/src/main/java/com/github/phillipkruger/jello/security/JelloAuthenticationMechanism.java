package com.github.phillipkruger.jello.security;

import com.github.phillipkruger.jello.security.token.Token;
import com.github.phillipkruger.jello.security.token.TokenHelper;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import javax.annotation.security.DeclareRoles;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

/**
 * Web based username and password for the JSF screen and token based for API
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@DeclareRoles({"user", "admin"})
@AutoApplySession
@RequestScoped
public class JelloAuthenticationMechanism implements HttpAuthenticationMechanism {
    
    @Inject
    private IdentityStore identityStore;

    @Inject
    private TokenHelper tokenHelper;
    
    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        if (request.getParameter(FORM_USER) != null && request.getParameter(FORM_PASS) != null && !httpMessageContext.isProtected()) {
            return formLoginAuthentication(request,httpMessageContext);
        }else if (request.getHeader(TOKEN_HEADER) != null && httpMessageContext.isProtected()) {
            return tokenAuthentication(request,httpMessageContext);
        }else if(!httpMessageContext.isProtected()){
            return httpMessageContext.doNothing();
        }else{
            return httpMessageContext.responseUnauthorized();
        }
    }

    private AuthenticationStatus tokenAuthentication(HttpServletRequest request, HttpMessageContext httpMessageContext){
        final String key = request.getHeader(TOKEN_HEADER);
        if (key != null) {
            try {
                Token token = tokenHelper.decrypt(key);
                if(token!=null && token.getUser()!=null){
                    return httpMessageContext.notifyContainerAboutLogin(
                        token.getUser(), new HashSet<>(token.getGroups()));
                }
            }catch(InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e){
                // At any error, we do not allow access.
                httpMessageContext.responseUnauthorized();
            }
        }
        return httpMessageContext.responseUnauthorized();
        
    }
    
    // Form based used in GUI
    private AuthenticationStatus formLoginAuthentication(HttpServletRequest request, HttpMessageContext httpMessageContext){
        String name = request.getParameter(FORM_USER);
        Password password = new Password(request.getParameter(FORM_PASS));

        CredentialValidationResult result = identityStore.validate(
                new UsernamePasswordCredential(name, password));

        if (result.getStatus() == VALID) {
            return httpMessageContext.notifyContainerAboutLogin(result);
        } else {    
            return httpMessageContext.responseUnauthorized();
        }
    }
    
    private static final String TOKEN_HEADER = "jello.api.key";
    private static final String FORM_USER = "login:username";
    private static final String FORM_PASS = "login:password";
    
}
