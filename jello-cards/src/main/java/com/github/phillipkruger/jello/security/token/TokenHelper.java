package com.github.phillipkruger.jello.security.token;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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
    
    @Inject
    private SecurityContext securityContext;
    
    public String generateToken() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        String token = getStringToEncrypt();
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
    
    private String getStringToEncrypt(){
        Token token = new Token();
        if(securityContext!=null && securityContext.getCallerPrincipal()!=null){
            token.setUser(securityContext.getCallerPrincipal().getName());
            if(securityContext.isCallerInRole("user"))token.addGroup("user");
            if(securityContext.isCallerInRole("amin"))token.addGroup("admin");
        }
        return JsonbBuilder.create().toJson(token);
    }
    
    @PostConstruct
    public void init(){
        key = new SecretKeySpec(KEY_VALUE, ALGORITHM);
    }
    
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY_VALUE = new byte[] { 'T', 'o', 'p', 'S', 'e', 'c', 'r', 'e', 't', 'P', 'a', 's', 's', 'K', 'e', 'y' };
}
