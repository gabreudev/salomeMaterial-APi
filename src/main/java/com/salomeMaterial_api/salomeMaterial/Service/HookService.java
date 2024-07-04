package com.salomeMaterial_api.salomeMaterial.Service;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.salomeMaterial_api.salomeMaterial.Entity.User;
import com.salomeMaterial_api.salomeMaterial.Pix.Credentials;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;


@Service
public class HookService {


    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    @Autowired
    private MailService mailService;

    public JSONObject configHook(){

        JSONObject options = configuringJsonObject();

        options.put("x-skip-mtls-checking", "true");

        HashMap<String, String> params = new HashMap<>();
        params.put("chave", "78dad91d-e7f0-4877-8b61-d731d884a47f");

        JSONObject body = new JSONObject();
        body.put("webhookUrl", "https://1fbb-45-170-222-162.ngrok-free.app/webhook");

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixConfigWebhook", params, body);
            System.out.println(response);
            return response;

        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            return null;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public JSONObject listWebHooks(){

        JSONObject options = configuringJsonObject();
        
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("inicio", "2024-01-01T16:01:35Z");
        params.put("fim", "2024-06-30T16:01:35Z");

        try {
            EfiPay efi= new EfiPay(options);
            JSONObject response = efi.call("pixListWebhook", params, new JSONObject());
            System.out.println(response);
            return response;
        }catch (EfiPayException e){
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
            return null;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
    public JSONObject deleteWebhook() {

        JSONObject options = configuringJsonObject();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("chave", "78dad91d-e7f0-4877-8b61-d731d884a47f");

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixDeleteWebhook", params, new JSONObject());
            System.out.println(response);
            return response;
        } catch (EfiPayException e) {
            System.out.println(e.getError());
            System.out.println(e.getErrorDescription());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void verifyPayment(String txid, String valor) {
        if (Objects.equals(valor, "0.01")) {
            JSONObject options = configuringJsonObject();

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("txid", txid);

            try {
                EfiPay efi = new EfiPay(options);
                JSONObject response = efi.call("pixDetailCharge", params, new JSONObject());

                String status = response.getString("status");
                if (!status.equals("CONCLUIDA")) {
                    System.out.println("não esta concluida");
                    return;
                }
                JSONObject devedor = response.getJSONObject("devedor");
                String cpf = devedor.getString("cpf");
                String nome = devedor.getString("nome");
                String email = null;

                JSONArray infoAdicionais = response.getJSONArray("infoAdicionais");
                for (int i = 0; i < infoAdicionais.length(); i++) {
                    JSONObject info = infoAdicionais.getJSONObject(i);
                    if (info.getString("nome").equals("email")) {
                        email = info.getString("valor");

                    }
                }
                if (email != null) {
                    User data = new User(email, nome, cpf);
                    mailService.sendMaterialEmail(data);
                }

            }catch(EfiPayException e){
                System.out.println(e.getError());
                System.out.println(e.getErrorDescription());
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
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

