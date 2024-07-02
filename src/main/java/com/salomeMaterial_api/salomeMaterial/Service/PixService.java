package com.salomeMaterial_api.salomeMaterial.Service;

import br.com.efi.efisdk.EfiPay;
import br.com.efi.efisdk.exceptions.EfiPayException;
import com.salomeMaterial_api.salomeMaterial.Entity.PixChargeRequest;
import com.salomeMaterial_api.salomeMaterial.Exceptions.PixServiceException;
import com.salomeMaterial_api.salomeMaterial.Pix.Credentials;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PixService {

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${CLIENT_SECRET}")
    private String clientSecret;

    public JSONObject pixCreateEVP() {
        JSONObject options = configuringJsonObject();

        try {
            EfiPay efi = new EfiPay(options);
            return efi.call("pixCreateEvp", new HashMap<>(), new JSONObject());
        } catch (EfiPayException e) {
            throw new PixServiceException(e.getErrorDescription(), e);
        } catch (Exception e) {
            throw new PixServiceException("Erro ao criar EVP: " + e.getMessage(), e);
        }
    }

    public JSONObject pixCreateCharge(PixChargeRequest pixChargeRequest) {
        JSONObject options = configuringJsonObject();

        JSONObject body = new JSONObject();
        body.put("calendario", new JSONObject().put("expiracao", 3600));
        body.put("devedor", new JSONObject()
                .put("cpf", pixChargeRequest.cpf())
                .put("nome", pixChargeRequest.nome())
        );
        JSONArray infoAdicionais = new JSONArray();
        infoAdicionais.put(new JSONObject().put("nome", "email").put("valor", pixChargeRequest.email()));
        body.put("infoAdicionais", infoAdicionais);

        body.put("valor", new JSONObject().put("original", "0.01"));
        body.put("chave", "6fb9b45c-bfbf-4ad2-aabc-50e9effbe3ef");

        try {
            EfiPay efi = new EfiPay(options);
            JSONObject response = efi.call("pixCreateImmediateCharge", new HashMap<>(), body);

            int idFromJson = response.getJSONObject("loc").getInt("id");
            String qrCodeBase64 = pixGenerateQRCode(String.valueOf(idFromJson));

            response.put("imagemQrcode", qrCodeBase64);

            return response;
        } catch (EfiPayException e) {
            throw new PixServiceException(e.getErrorDescription(), e);
        } catch (Exception e) {
            throw new PixServiceException("Erro ao criar cobrança PIX: " + e.getMessage(), e);
        }
    }

    private String pixGenerateQRCode(String id) {
        JSONObject options = configuringJsonObject();

        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);

        try {
            EfiPay efi = new EfiPay(options);
            Map<String, Object> response = efi.call("pixGenerateQRCode", params, new HashMap<>());

            return ((String) response.get("imagemQrcode")).split(",")[1];
        } catch (EfiPayException e) {
            throw new PixServiceException(e.getErrorDescription(), e);
        } catch (Exception e) {
            throw new PixServiceException("Erro ao gerar QR Code: " + e.getMessage(), e);
        }
    }

    private JSONObject configuringJsonObject() {
        Credentials credentials = new Credentials();

        JSONObject options = new JSONObject();
        options.put("client_id", clientId);
        options.put("client_secret", clientSecret);
        options.put("certificate", credentials.getCertificate());
        options.put("sandbox", credentials.isSandbox());

        return options;
    }
}
