package com.salomeMaterial_api.salomeMaterial.Service;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.salomeMaterial_api.salomeMaterial.Pix.Credentials;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;



@Service
public class HookService {


    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    public void configHook(){

        Credentials credentials = new Credentials();
        
        JSONObject options = configuringJsonObject();

        options.put("x-skip-mtls-checking", "true");

        HashMap<String, String> params = new HashMap<>();
        params.put("chave", "2d9c6bfd-d19e-4123-8a35-c2f0caac55db");

        JSONObject body = new JSONObject();
        body.put("webhookUrl", "https://5e94-45-170-222-201.ngrok-free.app/webhook/");

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixConfigWebhook", params, body);
            System.out.println(response);

        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void listWebHooks(){

        Credentials credentials = new Credentials();


        JSONObject options = configuringJsonObject();
        
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("chave", "2d9c6bfd-d19e-4123-8a35-c2f0caac55db");

        try {
            EfiPay efi= new EfiPay(options);
            JSONObject response = efi.call("pixDetailWebhook", params, new JSONObject());
            System.out.println(response);
        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private JSONObject configuringJsonObject(){
        Credentials credentials = new Credentials();

        JSONObject options = new JSONObject();
        options.put("client_id", clientId);
        options.put("client_secret", clientSecret);
        options.put("certificate", credentials.getCertificate());
        options.put("sandbox", credentials.isSandbox());

        return options;
    }
}

