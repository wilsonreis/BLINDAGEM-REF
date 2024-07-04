package com.santander.kpv.controllers;

import com.santander.kpv.services.sender.SendReceiverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BlindagemRefControllerSant {

    private SendReceiverService enviaMensagem;

    public BlindagemRefControllerSant(SendReceiverService enviaMensagem) {
        this.enviaMensagem = enviaMensagem;
    }

    @GetMapping("/blindagemSant")
    String blindagem(@RequestParam(value = "cpf") String cpf) {
        try {
            String xml = enviaMensagem.getMensagem(cpf);
            log.info("BlindagemRefControllerSant : passou ");
            return enviaMensagem.send(xml);
        } catch (JmsException ex) {
            ex.printStackTrace();
            return "FAIL";
        }
    }
}
