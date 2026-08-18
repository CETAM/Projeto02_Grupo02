package cetam.projeto02grupo02.controller;

import cetam.projeto02grupo02.model.Estoque;
import cetam.projeto02grupo02.service.ClienteService;
import cetam.projeto02grupo02.service.EstoqueService;
import cetam.projeto02grupo02.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "relatorios/index";
    }

    @GetMapping("/cliente")
    public String relatorioPorCliente(@RequestParam("idCliente") Long idCliente, Model model) {
        model.addAttribute("clienteSelecionado", clienteService.buscarPorId(idCliente).orElse(null));
        model.addAttribute("pedidos", pedidoService.listarPorCliente(idCliente));
        model.addAttribute("clientes", clienteService.listarTodos());
        return "relatorios/cliente";
    }

    @GetMapping("/periodo")
    public String relatorioPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            Model model) {

        LocalDateTime dtInicio = inicio.atStartOfDay();
        LocalDateTime dtFim = fim.atTime(23, 59, 59);

        model.addAttribute("pedidos", pedidoService.listarPorPeriodo(dtInicio, dtFim));
        model.addAttribute("dataInicio", inicio);
        model.addAttribute("dataFim", fim);
        return "relatorios/periodo";
    }

    @GetMapping("/estoque-falta")
    public String relatorioEstoqueFalta(Model model) {
        List<Estoque> estoqueCritico = estoqueService.listarTodos().stream()
                .filter(e -> e.getQuantidadeAtual() <= e.getProduto().getEstoqueMinimo())
                .collect(Collectors.toList());

        model.addAttribute("estoques", estoqueCritico);
        return "relatorios/estoque_falta";
    }
}