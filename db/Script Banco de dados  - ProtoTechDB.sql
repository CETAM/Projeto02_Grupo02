create database prototech_db;
use prototech_db;

CREATE TABLE Cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nome_cliente VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(100),
    telefone VARCHAR(15),
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome_categoria VARCHAR(50) NOT NULL,
    descricao VARCHAR(255)
);

CREATE TABLE Produto (
    id_produto INT AUTO_INCREMENT PRIMARY KEY,
    nome_produto VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    preco_custo DECIMAL(10,2) NOT NULL,
    preco_venda DECIMAL(10,2) NOT NULL,
    estoque_minimo INT DEFAULT 5,
    id_categoria INT,
    FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria)
);

CREATE TABLE Estoque (
    id_estoque INT AUTO_INCREMENT PRIMARY KEY,
    id_produto INT UNIQUE NOT NULL,
    quantidade_atual INT DEFAULT 0,
    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto) ON DELETE CASCADE
);

CREATE TABLE Pedido (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    data_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    status_pedido VARCHAR(20) DEFAULT 'PENDENTE',
    valor_total decimal(10, 2),
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente)
);

CREATE TABLE ItemPedido (
    id_item INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_produto INT NOT NULL,
    quantidade INT NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES Pedido(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto)
);

CREATE TABLE AlertaEstoque (
    id_alerta INT AUTO_INCREMENT PRIMARY KEY,
    id_produto INT NOT NULL,
    mensagem VARCHAR(255) NOT NULL,
    data_alerta DATETIME DEFAULT CURRENT_TIMESTAMP,
    status_alerta ENUM('PENDENTE', 'RESOLVIDO') DEFAULT 'PENDENTE',
    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto)
);

create table UsuarioAdmin (
	id_admin INT AUTO_INCREMENT PRIMARY KEY,
    nome varchar(100) not null,
	login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL
);

-- ==========================================
-- INSERTS DE TESTE PARA O NOVO SISTEMA
-- ==========================================

-- 1. Inserindo o Usuário Administrador
INSERT INTO UsuarioAdmin (nome, login, senha) 
VALUES ('Administrador Geral', 'admin', 'admin123');

-- 2. Inserindo Categorias
INSERT INTO Categoria (nome_categoria, descricao) VALUES 
('Informática', 'Computadores, notebooks e componentes'),
('Periféricos', 'Teclados, mouses, monitores e headsets'),
('Eletrônicos', 'Smartphones, tablets e smartwatches');

-- 3. Inserindo Clientes
INSERT INTO Cliente (nome_cliente, cpf, email, telefone) VALUES 
('João Silva', '111.222.333-44', 'joao.silva@email.com', '(11) 98888-7777'),
('Maria Oliveira', '555.666.777-88', 'maria.oliveira@email.com', '(21) 97777-6666'),
('Carlos Pereira', '999.888.777-66', 'carlos.pereira@email.com', '(31) 96666-5555');

-- 4. Inserindo Produtos (Note as referencias ao id_categoria gerado acima 1, 2 e 3)
INSERT INTO Produto (nome_produto, descricao, preco_custo, preco_venda, estoque_minimo, id_categoria) VALUES 
('Notebook Dell XPS 13', 'Notebook ultrafino 16GB RAM, 512GB SSD', 4500.00, 6500.00, 3, 1),
('Mouse Sem Fio Logitech', 'Mouse ergonômico bluetooth', 80.00, 150.00, 10, 2),
('Teclado Mecânico Redragon', 'Teclado switch azul RGB', 120.00, 250.00, 5, 2),
('Smartphone Samsung Galaxy S23', 'Celular 256GB 5G', 3200.00, 4800.00, 4, 3);

-- 5. Inserindo Estoque (Relacionado aos IDs dos Produtos)
-- Assumindo que os IDs dos produtos foram gerados de 1 a 4 sequencialmente
INSERT INTO Estoque (id_produto, quantidade_atual) VALUES 
(1, 10), -- 10 Notebooks
(2, 25), -- 25 Mouses
(3, 15), -- 15 Teclados
(4, 8);  -- 8 Smartphones

-- 6. Inserindo Pedidos (Alguns concluídos, outros pendentes)
INSERT INTO Pedido (id_cliente, status_pedido, valor_total) VALUES 
(1, 'CONCLUIDA', 6650.00), -- Pedido do João
(2, 'PENDENTE', 250.00),   -- Pedido da Maria
(3, 'CONCLUIDA', 4800.00); -- Pedido do Carlos

-- 7. Inserindo Itens dos Pedidos 
-- Pedido 1 (João): Levou 1 Notebook e 1 Mouse
INSERT INTO ItemPedido (id_pedido, id_produto, quantidade, valor_unitario) VALUES 
(1, 1, 1, 6500.00), -- Notebook
(1, 2, 1, 150.00);  -- Mouse

-- Pedido 2 (Maria): Levou 1 Teclado Mecânico
INSERT INTO ItemPedido (id_pedido, id_produto, quantidade, valor_unitario) VALUES 
(2, 3, 1, 250.00);

-- Pedido 3 (Carlos): Levou 1 Smartphone
INSERT INTO ItemPedido (id_pedido, id_produto, quantidade, valor_unitario) VALUES 
(3, 4, 1, 4800.00);

-- 8. Inserindo um Alerta de Estoque simulado
INSERT INTO AlertaEstoque (id_produto, mensagem, status_alerta) VALUES 
(1, 'Estoque próximo do mínimo para: Notebook Dell XPS 13', 'PENDENTE');

update cliente set nome_cliente = "Ana Kelley" where id_cliente = 1;
update cliente set nome_cliente = "Elano Barros" where id_cliente = 2;
update cliente set nome_cliente = "Izhac Nylton" where id_cliente = 3;
update cliente set email = "ana.kelley@email.com" where id_cliente = 1;
update cliente set email = "elano.barros@email.com" where id_cliente = 2;
update cliente set email = "izhac.nylton@email.com" where id_cliente = 3;