package cetam.projeto02grupo02.controller;

import cetam.projeto02grupo02.model.Produto;
import cetam.projeto02grupo02.service.CategoriaService;
import cetam.projeto02grupo02.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        return "produtos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "produtos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        Produto produto = produtoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado ID: " + id));
        model.addAttribute("produto", produto);
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "produtos/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Produto produto, BindingResult result, @RequestParam(value = "estoqueInicial", required = false) Integer estoqueInicial, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            return "produtos/formulario";
        }
        produtoService.salvar(produto, estoqueInicial);
        return "redirect:/produtos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, org.springframework.web.servlet.mvc.support.RedirectAttributes attributes) {
        try {
            produtoService.excluir(id);
            attributes.addFlashAttribute("mensagemSucesso", "Produto excluído com sucesso.");
        } catch (IllegalStateException e) {
            attributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/produtos";
    }
}