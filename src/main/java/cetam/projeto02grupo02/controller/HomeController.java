package cetam.projeto02grupo02.controller;

import cetam.projeto02grupo02.service.ClienteService;
import cetam.projeto02grupo02.service.EstoqueService;
import cetam.projeto02grupo02.service.PedidoService;
import cetam.projeto02grupo02.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalClientes", clienteService.listarTodos().size());
        model.addAttribute("totalProdutos", produtoService.listarTodos().size());
        model.addAttribute("pedidosRecentes", pedidoService.listarTodos());
        model.addAttribute("estoqueLista", estoqueService.listarTodos());
        return "dashboard";
    }
}