package org.example.spring;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FormularioController {

    @GetMapping("/formulario")
    public String mostrarFormulario() {
        return "formulario";
    }

    @PostMapping("/submit-formulario")
    public String procesarFormulario(@RequestParam String nombre, @RequestParam String email, Model model) {
// Aquí puedes realizar alguna lógica con los datos del formulario
        model.addAttribute("nombre", nombre);
        model.addAttribute("email", email);
        return "submit-formulario";
    }
}