package com.salomeMaterial_api.salomeMaterial.Controller;

import com.salomeMaterial_api.salomeMaterial.Service.HookService;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("webhook")
public class HookController {

    @Autowired
    private HookService hookService;

    @PutMapping("create")
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
    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload) {
        JSONObject jsonPayload = new JSONObject(payload);
        System.out.println("Recebido webhook: " + jsonPayload.toString());

        // Process the payment confirmation
        // Exemplo: Atualizar status do pedido no banco de dados

        // Retornar uma resposta de sucesso
        return ResponseEntity.ok("Webhook recebido com sucesso");
    }

}
