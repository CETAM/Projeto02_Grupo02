package cetam.projeto02grupo02.controller;

import cetam.projeto02grupo02.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("estoques", estoqueService.listarTodos());
        return "estoque/lista";
    }

    @PostMapping("/ajustar")
    public String ajustar(@RequestParam("idProduto") Long idProduto, @RequestParam("quantidade") Integer quantidade) {
        estoqueService.atualizarQuantidade(idProduto, quantidade);
        return "redirect:/estoque";
    }
}