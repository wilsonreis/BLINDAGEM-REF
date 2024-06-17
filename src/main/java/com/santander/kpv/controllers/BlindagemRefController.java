package com.santander.kpv.controllers;

import com.santander.kpv.services.sender.SendReceivService;
import org.springframework.jms.JmsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlindagemRefController {

    private SendReceivService enviaMensagem;

    public BlindagemRefController(SendReceivService enviaMensagem) {
        this.enviaMensagem = enviaMensagem;
    }

    @GetMapping("/blindagem")
    String blindagem(@RequestParam(value = "cpf") String cpf){
        try{
            return enviaMensagem.sendSyncReply(cpf);
        }catch(JmsException ex){
            ex.printStackTrace();
            return "FAIL";
        }
    }
}
