package com.salomeMaterial_api.salomeMaterial.Controller;

import com.salomeMaterial_api.salomeMaterial.Service.HookService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HookController {

    @Autowired
    private HookService hookService;

    @GetMapping("webhook/create")
    public ResponseEntity configHook(){
        JSONObject response = this.hookService.configHook();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }
    @GetMapping("list")
    public ResponseEntity listHooks(){
        JSONObject response = this.hookService.listWebHooks();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }
    @PostMapping("webhook")
    public ResponseEntity handleWebhook(@RequestBody String payload) {
        return ResponseEntity.ok().build();
    }
    @PostMapping("webhook/pix")
    public ResponseEntity receiveWebhook(@RequestBody String payload) {
        JSONObject jsonPayload = new JSONObject(payload);
        JSONArray pixArray = jsonPayload.getJSONArray("pix");
        JSONObject pixObject = pixArray.getJSONObject(0);
        this.hookService.verifyPayment(pixObject.getString("txid"),pixObject.getString("valor"));
        return ResponseEntity.ok(jsonPayload);
    }
    @GetMapping("delete")
    public ResponseEntity deleteHooks(){
        JSONObject response = this.hookService.deleteWebhook();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.toString());
    }

}
