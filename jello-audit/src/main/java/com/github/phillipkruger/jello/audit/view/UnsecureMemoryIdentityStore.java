package com.github.phillipkruger.jello.audit.view;

import java.util.HashSet;
import static java.util.Arrays.asList;
import java.util.EnumSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
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
public class UnsecureMemoryIdentityStore implements IdentityStore {
    
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
        
        return CredentialValidationResult.INVALID_RESULT;
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
    
}