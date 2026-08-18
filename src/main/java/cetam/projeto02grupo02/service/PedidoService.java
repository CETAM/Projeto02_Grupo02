package cetam.projeto02grupo02.service;

import cetam.projeto02grupo02.model.ItemPedido;
import cetam.projeto02grupo02.model.Pedido;
import cetam.projeto02grupo02.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EstoqueService estoqueService;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> listarPorCliente(Long idCliente) {
        return pedidoRepository.findByClienteIdCliente(idCliente);
    }

    public List<Pedido> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }

    @Transactional
    public Pedido criarPedido(Pedido pedido, List<ItemPedido> itens) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedido item : itens) {
            item.setPedido(pedido);
            BigDecimal subtotal = item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(subtotal);

            // Realiza a baixa do estoque e valida disponibilidade
            estoqueService.baixarEstoque(item.getProduto(), item.getQuantidade());
        }

        pedido.setItens(itens);
        pedido.setValorTotal(total);
        pedido.setStatusPedido("CONCLUIDA");

        return pedidoRepository.save(pedido);
    }
}