package com.github.phillipkruger.jello.security;

import java.util.HashSet;
import static java.util.Arrays.asList;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import lombok.extern.java.Log;

/**
 * This store basically allow anyone in as long as the username
 * is a email address and the password is the same
 * Not very secure :)
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationScoped
@Log
public class InMemoryIdentityStore implements IdentityStore {
    
//    @Override
//    public int priority() {
//        return 70;
//    }
// 
//    @Override
//    public Set<ValidationType> validationTypes() {
//        return EnumSet.of(ValidationType.VALIDATE,ValidationType.PROVIDE_GROUPS);
//    }
 
    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        String user = credential.getCaller();
        String pass = credential.getPasswordAsString();
        
        log.severe("user = " + user);
        log.severe("pass = " + pass);
        
        if(isEmail(user) && user.equals(pass)){
            log.severe("Come on in !");
            return new CredentialValidationResult(credential.getCaller(),new HashSet<>(asList("user", "admin")));
        }
        
        return CredentialValidationResult.INVALID_RESULT;
    }
    
//    @Override
//    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
//        log.severe("getCallerGroups = " + validationResult);
//        HashSet<String> groups = new HashSet<>();
//        groups.add("user");
//        return groups;
//    }

    private boolean isEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }
    
    private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    
}