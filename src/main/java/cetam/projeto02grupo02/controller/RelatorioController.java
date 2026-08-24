package cetam.projeto02grupo02.controller;

import cetam.projeto02grupo02.model.Estoque;
import cetam.projeto02grupo02.service.ClienteService;
import cetam.projeto02grupo02.service.EstoqueService;
import cetam.projeto02grupo02.service.PedidoService;
import cetam.projeto02grupo02.service.PdfReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private PdfReportService pdfReportService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "relatorios/index";
    }

    @GetMapping("/cliente")
    public ResponseEntity<byte[]> relatorioPorCliente(@RequestParam("idCliente") Long idCliente) {
        try {
            Map<String, Object> dados = new HashMap<>();
            dados.put("clienteSelecionado", clienteService.buscarPorId(idCliente).orElse(null));
            dados.put("pedidos", pedidoService.listarPorCliente(idCliente));
            dados.put("clientes", clienteService.listarTodos());

            byte[] pdfBytes = pdfReportService.gerarPdf("relatorios/cliente", dados);
            return construirRespostaPdf(pdfBytes, "relatorio_cliente.pdf");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/periodo")
    public ResponseEntity<byte[]> relatorioPorPeriodo(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        try {
            LocalDateTime dtInicio = inicio.atStartOfDay();
            LocalDateTime dtFim = fim.atTime(23, 59, 59);

            Map<String, Object> dados = new HashMap<>();
            dados.put("pedidos", pedidoService.listarPorPeriodo(dtInicio, dtFim));
            dados.put("dataInicio", inicio);
            dados.put("dataFim", fim);

            byte[] pdfBytes = pdfReportService.gerarPdf("relatorios/periodo", dados);
            return construirRespostaPdf(pdfBytes, "relatorio_periodo.pdf");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/estoque-falta")
    public ResponseEntity<byte[]> relatorioEstoqueFalta() {
        try {
            List<Estoque> estoqueCritico = estoqueService.listarTodos().stream()
                    .filter(e -> e.getQuantidadeAtual() <= e.getProduto().getEstoqueMinimo())
                    .collect(Collectors.toList());

            Map<String, Object> dados = new HashMap<>();
            dados.put("estoques", estoqueCritico);

            byte[] pdfBytes = pdfReportService.gerarPdf("relatorios/estoque_falta", dados);
            return construirRespostaPdf(pdfBytes, "relatorio_estoque_falta.pdf");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private ResponseEntity<byte[]> construirRespostaPdf(byte[] pdfBytes, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // "inline" para abrir no navegador em vez de baixar automaticamente, conforme solicitado
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}