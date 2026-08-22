-- Criação do banco de dados
CREATE DATABASE prototech_db;
USE prototech_db;

-- ==========================================
-- 1. Criação das Tabelas
-- ==========================================

-- Tabela de Categorias
CREATE TABLE Categoria (
    id_categoria BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_categoria VARCHAR(50) NOT NULL,
    descricao VARCHAR(255)
);

-- Tabela de Clientes
CREATE TABLE Cliente (
    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_cliente VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(100),
    telefone VARCHAR(15),
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela de Produtos
CREATE TABLE Produto (
    id_produto BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_produto VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    preco_custo DECIMAL(10,2) NOT NULL,
    preco_venda DECIMAL(10,2) NOT NULL,
    estoque_minimo INT DEFAULT 5,
    ativo BOOLEAN DEFAULT TRUE,
    id_categoria BIGINT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria)
);

-- Tabela de Estoque
CREATE TABLE Estoque (
    id_estoque BIGINT AUTO_INCREMENT PRIMARY KEY,
    version BIGINT DEFAULT 0,
    id_produto BIGINT NOT NULL UNIQUE,
    quantidade_atual INT DEFAULT 0,
    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto) ON DELETE CASCADE
);

-- Tabela de Usuário Admin
CREATE TABLE UsuarioAdmin (
    id_admin BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

-- Tabela de Pedidos
CREATE TABLE Pedido (
    id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status_pedido VARCHAR(20) DEFAULT 'PENDENTE',
    valor_total DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

-- Tabela de Itens do Pedido
CREATE TABLE ItemPedido (
    id_item BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    id_produto BIGINT NOT NULL,
    quantidade INT NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES Pedido(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto)
);

-- Tabela de Alertas de Estoque
CREATE TABLE AlertaEstoque (
    id_alerta BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_produto BIGINT NOT NULL,
    mensagem VARCHAR(255) NOT NULL,
    data_alerta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status_alerta VARCHAR(20) DEFAULT 'PENDENTE',
    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto) ON DELETE CASCADE
);


-- ==========================================
-- 2. Inserção de Dados
-- ==========================================

-- Inserindo Categorias
INSERT INTO Categoria (nome_categoria, descricao) VALUES
('Hardware', 'Peças e componentes de computador'),
('Periféricos', 'Teclados, mouses, monitores'),
('Acessórios', 'Cabos, adaptadores, etc');

-- Inserindo Clientes (ativo = 1 (true))
INSERT INTO Cliente (nome_cliente, cpf, email, telefone, ativo) VALUES
('João Silva', '111.111.111-11', 'joao@email.com', '(11) 99999-1111', 1),
('Maria Oliveira', '222.222.222-22', 'maria@email.com', '(21) 98888-2222', 1),
('Carlos Mendes', '333.333.333-33', 'carlos@email.com', '(31) 97777-3333', 1);

-- Inserindo Produtos (ativo = 1 (true))
INSERT INTO Produto (nome_produto, descricao, preco_custo, preco_venda, estoque_minimo, ativo, id_categoria) VALUES
('Placa Mãe Asus', 'Placa Mãe Asus B450', 400.00, 650.00, 5, 1, 1),
('Processador Ryzen 5', 'AMD Ryzen 5 3600', 700.00, 1100.00, 3, 1, 1),
('Teclado Mecânico', 'Teclado Mecânico RGB', 150.00, 250.00, 10, 1, 2),
('Mouse Gamer', 'Mouse Gamer 3200 DPI', 80.00, 150.00, 10, 1, 2);

-- Inserindo Estoques OBRIGATORIAMENTE para os produtos criados acima
-- A quantidade_atual pode ser ajustada. A version deve começar em 0.
INSERT INTO Estoque (version, id_produto, quantidade_atual) VALUES
(0, 1, 12), 
(0, 2, 5),
(0, 3, 20),
(0, 4, 15);

-- Inserindo um Usuário Admin
INSERT INTO UsuarioAdmin (nome, login, senha) VALUES
('Administrador', 'admin', 'admin123');
