package cetam.projeto02grupo02.controller;

import cetam.projeto02grupo02.model.Cliente;
import cetam.projeto02grupo02.model.ItemPedido;
import cetam.projeto02grupo02.model.Pedido;
import cetam.projeto02grupo02.model.Produto;
import cetam.projeto02grupo02.service.ClienteService;
import cetam.projeto02grupo02.service.PedidoService;
import cetam.projeto02grupo02.service.ProdutoService;
import cetam.projeto02grupo02.service.RelatorioPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private RelatorioPdfService relatorioPdfService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "pedidos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("produtos", produtoService.listarTodos());
        return "pedidos/novo";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam("idCliente") Long idCliente,
                         @RequestParam("produtosIds") List<Long> produtosIds,
                         @RequestParam("quantidades") List<Integer> quantidades) {

        Cliente cliente = clienteService.buscarPorId(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente inválido"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        List<ItemPedido> itens = new ArrayList<>();
        for (int i = 0; i < produtosIds.size(); i++) {
            Produto produto = produtoService.buscarPorId(produtosIds.get(i))
                    .orElseThrow(() -> new IllegalArgumentException("Produto inválido"));

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(quantidades.get(i));
            item.setValorUnitario(produto.getPrecoVenda());
            itens.add(item);
        }

        pedidoService.criarPedido(pedido, itens);
        return "redirect:/pedidos";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable("id") Long id, Model model) {
        Pedido pedido = pedidoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        model.addAttribute("pedido", pedido);
        return "pedidos/detalhes";
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> baixarPdf(@PathVariable("id") Long id) {
        Pedido pedido = pedidoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        byte[] pdfBytes = relatorioPdfService.gerarReciboPedidoPdf(pedido);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "pedido_" + id + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}