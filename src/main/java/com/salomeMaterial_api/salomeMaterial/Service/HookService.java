package com.salomeMaterial_api.salomeMaterial.Service;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.salomeMaterial_api.salomeMaterial.Pix.Credentials;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class HookService {

    public void configHook(){

        Credentials credentials = new Credentials();

        JSONObject options = new JSONObject();
        options.put("client_id", credentials.getClientId());
        options.put("client_secret", credentials.getClientSecret());
        options.put("certificate", credentials.getCertificate());
        options.put("sandbox", credentials.isSandbox());

        options.put("x-skip-mtls-checking", "true");

        HashMap<String, String> params = new HashMap<>();
        params.put("chave", "2d9c6bfd-d19e-4123-8a35-c2f0caac55db");

        JSONObject body = new JSONObject();
        body.put("webhookUrl", "https://seudominio.com.br/webhook/");

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

}
