package cetam.projeto02grupo02.controller;

import cetam.projeto02grupo02.repository.AlertaEstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alertas")
public class AlertaController {

    @Autowired
    private AlertaEstoqueRepository alertaEstoqueRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alertas", alertaEstoqueRepository.findAll());
        return "alertas/lista";
    }
}
