package cetam.projeto02grupo02.service;

import cetam.projeto02grupo02.model.ItemPedido;
import cetam.projeto02grupo02.model.Pedido;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class RelatorioPdfService {

    public byte[] gerarReciboPedidoPdf(Pedido pedido) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            Paragraph titulo = new Paragraph("Recibo do Pedido #" + pedido.getIdPedido(), fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);

            // Informações do Pedido e Cliente
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            document.add(new Paragraph("Data do Pedido: " + pedido.getDataPedido().format(formatter)));
            document.add(new Paragraph("Status: " + pedido.getStatusPedido()));
            document.add(new Paragraph("Cliente: " + pedido.getCliente().getNomeCliente()));
            document.add(new Paragraph("CPF: " + pedido.getCliente().getCpf()));
            document.add(Chunk.NEWLINE);

            // Tabela de Itens
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 2, 2, 2});

            PdfPCell h1 = new PdfPCell(new Phrase("Produto"));
            h1.setHorizontalAlignment(Element.ALIGN_CENTER);
            h1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(h1);

            PdfPCell h2 = new PdfPCell(new Phrase("Qtd"));
            h2.setHorizontalAlignment(Element.ALIGN_CENTER);
            h2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(h2);

            PdfPCell h3 = new PdfPCell(new Phrase("Preço Unit."));
            h3.setHorizontalAlignment(Element.ALIGN_CENTER);
            h3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(h3);

            PdfPCell h4 = new PdfPCell(new Phrase("Subtotal"));
            h4.setHorizontalAlignment(Element.ALIGN_CENTER);
            h4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(h4);

            for (ItemPedido item : pedido.getItens()) {
                table.addCell(item.getProduto().getNomeProduto());

                PdfPCell cellQtd = new PdfPCell(new Phrase(String.valueOf(item.getQuantidade())));
                cellQtd.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellQtd);

                PdfPCell cellPreco = new PdfPCell(new Phrase("R$ " + item.getValorUnitario()));
                cellPreco.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cellPreco);

                java.math.BigDecimal subtotal = item.getValorUnitario().multiply(new java.math.BigDecimal(item.getQuantidade()));
                PdfPCell cellSub = new PdfPCell(new Phrase("R$ " + subtotal));
                cellSub.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cellSub);
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            // Total
            Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
            Paragraph total = new Paragraph("Valor Total: R$ " + pedido.getValorTotal(), fontTotal);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}
