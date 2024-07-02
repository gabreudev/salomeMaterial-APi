package com.salomeMaterial_api.salomeMaterial.Controller;

import com.salomeMaterial_api.salomeMaterial.Entity.PixChargeRequest;
import com.salomeMaterial_api.salomeMaterial.Exceptions.PixServiceException;
import com.salomeMaterial_api.salomeMaterial.Service.PixService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pix")
public class PixController {

    @Autowired
    private PixService pixService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public ResponseEntity<String> pixCreateEVP() {
        try {
            JSONObject response = pixService.pixCreateEVP();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.toString());
        } catch (PixServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse("Erro ao gerar chave EVP: ", e.getMessage()).toString());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<String> pixCreateCharge(@RequestBody PixChargeRequest pixChargeRequest) {
        try {
            JSONObject response = pixService.pixCreateCharge(pixChargeRequest);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.toString());
        } catch (PixServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse("Erro ao gerar cobrança PIX: ", e.getMessage()).toString());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse("Erro ao gerar cobrança PIX: ", e.getMessage()).toString());
        }
    }

    private JSONObject errorResponse(String error, String description) {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error", error);
        errorResponse.put("error_description", description);
        return errorResponse;
    }
}
