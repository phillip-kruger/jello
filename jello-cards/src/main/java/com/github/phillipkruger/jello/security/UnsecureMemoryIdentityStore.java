package com.github.phillipkruger.jello.security;

import com.github.phillipkruger.jello.security.token.Token;
import com.github.phillipkruger.jello.security.token.TokenHelper;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import static java.util.Arrays.asList;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.stream.JsonParsingException;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.identitystore.RememberMeIdentityStore;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import lombok.extern.java.Log;

/**
 * Security. This store basically allow anyone in as long as the username
 * is a email address and the password is the same.
 * Not very secure :)
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationScoped
@Log
public class UnsecureMemoryIdentityStore implements IdentityStore, RememberMeIdentityStore {
    
    private final HashSet<String> admingroup = new HashSet<>(asList("user", "admin"));
    private final HashSet<String> usergroup = new HashSet<>(asList("user"));
    
    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(ValidationType.VALIDATE);
    }
    
    @Override
    public CredentialValidationResult validate(Credential credential) {
        UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
        
        String user = usernamePasswordCredential.getCaller();
        String pass = usernamePasswordCredential.getPasswordAsString();
        
        if(isEmail(user) && user.equals(pass)){
            return new CredentialValidationResult(usernamePasswordCredential.getCaller(),getGroupsForUser(user));
        }
        
        return INVALID_RESULT;
    }
    
    @Override
    public CredentialValidationResult validate(RememberMeCredential rmc) {
        try {
            Token token = tokenHelper.decrypt(rmc.getToken());
            if(token!=null && token.getUser()!=null){
                return new CredentialValidationResult(token.getUser(), token.getGroups());
            }else {
                return INVALID_RESULT;
            }
        } catch (JsonParsingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            // At any error, we do not allow access.
            return INVALID_RESULT;
        }
        
    }

    @Override
    public String generateLoginToken(CallerPrincipal callerPrincipal, Set<String> groups) {
        try {
            return tokenHelper.generateToken(callerPrincipal, groups);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            log.log(Level.SEVERE, "Could not create token: {0}", ex.getMessage());
            return null;
        }
    }

    @Override
    public void removeLoginToken(String string) {
        // Stateless authentication means at server side we don't maintain the state
    }
    
    private Set<String> getGroupsForUser(String user){
        if(isAdmin(user)){
            return admingroup;
        }else{
            return usergroup;
        }
    }
    
    private boolean isEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }
    
    // If you have a redhat or microsoft email, you get admin !
    private boolean isAdmin(String email) {
        return email.endsWith("@redhat.com") || email.endsWith("@microsoft.com");
    }
    
    private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    @Inject
    private TokenHelper tokenHelper;
}