select*from cliente;
select*from produto;
select*from estoque;
select*from categoria;
select*from pedido;
select*from alertaestoque;
select*from itempedido;
select*from usuarioadmin;

/*1. Relatórios Financeiros e Gerenciais
Lucro Real e Margem por Categoria e Produto
Calcula a receita bruta, o custo total dos produtos vendidos (CMV), o lucro operacional e a margem de lucro percentual.*/

SELECT 
    c.nome_categoria,
    p.nome_produto,
    SUM(ip.quantidade) AS total_unidades_vendidas,
    SUM(ip.quantidade * ip.valor_unitario) AS faturamento_bruto,
    SUM(ip.quantidade * p.preco_custo) AS custo_total,
    SUM(ip.quantidade * (ip.valor_unitario - p.preco_custo)) AS lucro_bruto,
    ROUND(
        (SUM(ip.quantidade * (ip.valor_unitario - p.preco_custo)) / 
        SUM(ip.quantidade * ip.valor_unitario)) * 100, 2
    ) AS margem_lucro_pct
FROM ItemPedido ip
INNER JOIN Produto p ON ip.id_produto = p.id_produto
LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria
INNER JOIN Pedido ped ON ip.id_pedido = ped.id_pedido
WHERE ped.status_pedido NOT IN ('CANCELADO', 'PENDENTE')
GROUP BY c.nome_categoria, p.id_produto, p.nome_produto
ORDER BY lucro_bruto DESC;

/*2. Consultas Operacionais e Auditoria de Estoque
A. Diagnóstico de Ruptura e Reposição de Estoque
Cruza a quantidade atual com o estoque mínimo, apontando o status de urgência e a quantidade recomendada para compra.*/

SELECT 
    p.id_produto,
    p.nome_produto,
    c.nome_categoria,
    p.estoque_minimo,
    COALESCE(e.quantidade_atual, 0) AS estoque_atual,
    (p.estoque_minimo * 2) - COALESCE(e.quantidade_atual, 0) AS sugestao_compra,
    CASE 
        WHEN COALESCE(e.quantidade_atual, 0) = 0 THEN 'CRÍTICO: ZERADO'
        WHEN COALESCE(e.quantidade_atual, 0) < p.estoque_minimo THEN 'ALERTA: ABAIXO DO MÍNIMO'
        WHEN COALESCE(e.quantidade_atual, 0) = p.estoque_minimo THEN 'ATENÇÃO: NO LIMITE'
        ELSE 'ESTÁVEL'
    END AS status_abastecimento
FROM Produto p
LEFT JOIN Estoque e ON p.id_produto = e.id_produto
LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria
WHERE COALESCE(e.quantidade_atual, 0) <= p.estoque_minimo
ORDER BY COALESCE(e.quantidade_atual, 0) ASC;

/*3. Visualizações e Automação (Views e Triggers)
A. View para BI/Dashboards: Detalhamento Consolidado de Vendas
Centraliza os relacionamentos para simplificar a alimentação de ferramentas de relatórios (Power BI, Metabase, etc.).*/

CREATE OR REPLACE VIEW vw_detalhes_vendas AS
SELECT 
    ped.id_pedido,
    ped.data_pedido,
    ped.status_pedido,
    cl.id_cliente,
    cl.nome_cliente,
    cl.cpf,
    p.id_produto,
    p.nome_produto,
    c.nome_categoria,
    ip.quantidade,
    ip.valor_unitario,
    (ip.quantidade * ip.valor_unitario) AS subtotal_item,
    (ip.quantidade * (ip.valor_unitario - p.preco_custo)) AS lucro_item
FROM Pedido ped
INNER JOIN Cliente cl ON ped.id_cliente = cl.id_cliente
INNER JOIN ItemPedido ip ON ped.id_pedido = ip.id_pedido
INNER JOIN Produto p ON ip.id_produto = p.id_produto
LEFT JOIN Categoria c ON p.id_categoria = c.id_categoria;
