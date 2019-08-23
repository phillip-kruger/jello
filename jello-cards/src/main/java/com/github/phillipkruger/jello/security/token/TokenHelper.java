package com.github.phillipkruger.jello.security.token;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.security.enterprise.SecurityContext;
import lombok.extern.java.Log;

/**
 * Generate a basic token
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@Log
@ApplicationScoped
public class TokenHelper {
    private Key key;
    
    public String generateToken() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        return generateToken(getCallerPrincipal(), getGroups());
    }

    public String generateToken(Principal callerPrincipal,Set<String> groups) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        String token = getStringToEncrypt(callerPrincipal,groups);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(token.getBytes());
        return new String(Base64.getEncoder().encode(encValue));
    }
    
    public Token decrypt(String encryptedValue) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return JsonbBuilder.create().fromJson(decryptedValue, Token.class);
    }
    
    private String getStringToEncrypt(Principal callerPrincipal,Set<String> groups){
        Token token = new Token(callerPrincipal.getName(), groups);
        return JsonbBuilder.create().toJson(token);
    }
    
    private Set<String> getGroups(){
        Set<String> groups = new HashSet<>();
        if(securityContext!=null){
            if(securityContext.isCallerInRole("user"))groups.add("user");
            if(securityContext.isCallerInRole("admin"))groups.add("admin");
        }
        return groups;
    }
    
    private Principal getCallerPrincipal(){
        return securityContext.getCallerPrincipal();
    }
    
    @PostConstruct
    public void init(){
        key = new SecretKeySpec(KEY_VALUE, ALGORITHM);
    }
    
    @Inject
    private SecurityContext securityContext;
    
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY_VALUE = new byte[] { 'T', 'o', 'p', 'S', 'e', 'c', 'r', 'e', 't', 'P', 'a', 's', 's', 'K', 'e', 'y' };
}
