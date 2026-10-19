# Módulo de Produtos — Modelagem e Regras Comerciais

## 1. Objetivo

O módulo de produtos é responsável pelo cadastro e pela manutenção dos produtos comercializados pela **Personalité Creative**.

Cada produto possui um **código comercial único de três dígitos** e suas próprias regras comerciais, incluindo:

- Categoria;
- Nome e descrição;
- Quantidade mínima de compra;
- Preço unitário fixo ou variável por quantidade;
- Adicionais e taxas;
- Prazo de produção/entrega, quando aplicável;
- Validade, quando aplicável;
- Situação ativa ou inativa.

**Princípio fundamental:** cada código comercial representa um produto independente, com regras de precificação próprias.

O cadastro é realizado por uma única requisição:

`POST /products`

Essa requisição cadastra **um único produto por vez**, incluindo suas faixas de preço e taxas.

---

## 2. Padrão dos códigos comerciais

O código comercial é informado manualmente e possui exatamente três dígitos.

| Posição | Significado | Exemplo |
|---|---|---|
| 1º e 2º dígitos | Categoria principal | `01` — Gourmet |
| 3º dígito | Identificação do produto na categoria | `1` — Biscoito amanteigado |

Exemplos:

| Código | Categoria | Produto |
|---|---|---|
| `011` | Gourmet | Biscoito amanteigado |
| `012` | Gourmet | Chocolate gourmet tradicional |
| `013` | Gourmet | Chocolate gourmet especial |
| `021` | Águas | Mar Doce sem gás |
| `031` | Garrafas | Garrafa térmica com alça de silicone |
| `041` | Gloss | Gloss Kiko |
| `081` | Papelaria | Caderneta Moleskine |
| `101` | Kits | Kit Wellness |

**Regras:**

1. O código deve ser único.
2. Deve possuir exatamente três dígitos.
3. Deve ser armazenado como `String`, nunca como número inteiro, para preservar zeros à esquerda.
4. O backend não gera automaticamente o código.
5. Cada código comercial possui suas próprias regras de preço e pedido mínimo.

**Atenção:** o padrão atual permite nove códigos por categoria (`011` a `019`, por exemplo). Se uma categoria ultrapassar nove produtos, será necessário revisar a convenção antes de atribuir novos códigos.

### Produtos com acabamentos diferentes

Se o fornecedor comercializar o mesmo produto com códigos distintos para acabamentos diferentes, cada código será cadastrado separadamente.

Exemplo ilustrativo:

- `011` — Biscoito amanteigado sem laço;
- `014` — Biscoito amanteigado com laço.

O código `014` é apenas uma sugestão e precisa ser confirmado antes do cadastro.

Ambos são produtos independentes e possuem regras próprias.

---

## 3. Categorias

A categoria será representada por um `ENUM` no Kotlin.

| Prefixo | Categoria | Valor sugerido do ENUM |
|---|---|---|
| `01` | Gourmet | `GOURMET` |
| `02` | Águas | `AGUAS` |
| `03` | Garrafas | `GARRAFAS` |
| `04` | Gloss | `GLOSS` |
| `05` | Bolsas | `BOLSAS` |
| `06` | Necessaires | `NECESSAIRES` |
| `07` | Lenços | `LENCOS` |
| `08` | Papelaria | `PAPELARIA` |
| `09` | Caixas | `CAIXAS` |
| `10` | Kits | `KITS` |

Novas categorias poderão ser adicionadas posteriormente.

---

## 4. Estrutura do banco de dados

O módulo utiliza três tabelas:

```text
products
   |
   |--- product_prices
   |
   |--- product_fees
```

Relacionamentos:

- `products` → `product_prices`: **1:N**
- `products` → `product_fees`: **1:N**

Um produto pode possuir várias faixas de preço e várias taxas comerciais.

### 4.1. Tabela `products`

Armazena a identificação do produto e suas configurações comerciais gerais.

| Campo | Tipo PostgreSQL | Descrição |
|---|---|---|
| `id` | UUID | Identificador interno, chave primária |
| `code` | VARCHAR(3) | Código comercial único |
| `name` | VARCHAR(150) | Nome do produto |
| `category` | VARCHAR(30) | Categoria correspondente ao ENUM |
| `description` | TEXT | Descrição do produto |
| `minimum_quantity` | INTEGER | Quantidade mínima de compra |
| `pricing_type` | VARCHAR(20) | Tipo de precificação |
| `shelf_life_days` | INTEGER | Validade em dias, quando aplicável |
| `production_lead_time_min_days` | INTEGER | Prazo mínimo estimado em dias |
| `production_lead_time_max_days` | INTEGER | Prazo máximo estimado em dias |
| `active` | BOOLEAN | Indica se o produto está ativo |
| `created_at` | TIMESTAMPTZ | Data de criação |
| `updated_at` | TIMESTAMPTZ | Última atualização |

**Regras:**

- `code` deve ser único e obrigatório.
- `name`, `category`, `pricing_type` e `active` são obrigatórios.
- `minimum_quantity` deve ser maior que zero para produtos com pedido mínimo definido.
- Validade e prazos são opcionais.
- O produto pode ser desativado sem precisar ser excluído.
- Os prazos precisam indicar claramente a unidade utilizada; neste modelo, são armazenados em dias.

### 4.2. Tabela `product_prices`

Armazena os valores unitários de cada produto.

| Campo | Tipo PostgreSQL | Descrição |
|---|---|---|
| `id` | UUID | Identificador da faixa de preço |
| `product_id` | UUID | FK para `products.id` |
| `minimum_quantity` | INTEGER | Quantidade a partir da qual o preço é aplicado |
| `unit_price` | NUMERIC(12,2) | Preço unitário em reais |
| `created_at` | TIMESTAMPTZ | Data de criação |
| `updated_at` | TIMESTAMPTZ | Última atualização |

**Regras:**

- Um produto pode possuir uma ou várias faixas.
- Não pode haver duas faixas com a mesma quantidade mínima para o mesmo produto.
- Os valores devem ser positivos.
- A primeira faixa deve começar na quantidade mínima permitida para o produto.
- Para produtos sob orçamento, a lista de preços pode estar vazia.

### 4.3. Tabela `product_fees`

Armazena os adicionais e taxas vinculados a um produto.

| Campo | Tipo PostgreSQL | Descrição |
|---|---|---|
| `id` | UUID | Identificador da taxa |
| `product_id` | UUID | FK para `products.id` |
| `name` | VARCHAR(100) | Nome da taxa |
| `amount` | NUMERIC(12,2) | Valor da taxa |
| `charge_type` | VARCHAR(30) | Forma de cobrança |
| `required` | BOOLEAN | Indica se a cobrança é obrigatória |
| `active` | BOOLEAN | Indica se a taxa está ativa |
| `created_at` | TIMESTAMPTZ | Data de criação |
| `updated_at` | TIMESTAMPTZ | Última atualização |

**Regras:**

- Cada taxa pertence a um único produto.
- Um produto pode não possuir taxas.
- Um produto pode possuir várias taxas.
- Taxas opcionais não devem ser aplicadas automaticamente.
- Taxas inativas não devem ser aplicadas.
- Valores não podem ser negativos.

---

## 5. Tipos de precificação

O campo `products.pricing_type` determina como o preço principal do produto é cadastrado.

### 5.1. `FIXED` — Preço fixo

O valor unitário não muda conforme a quantidade comprada.

**Exemplo: Garrafa térmica com alça de silicone**

- Código: `031`
- Quantidade mínima: 10 unidades
- Valor unitário: R$ 85,00

| Quantidade | Valor unitário | Total |
|---|---|---|
| 10 | R$ 85,00 | R$ 850,00 |
| 20 | R$ 85,00 | R$ 1.700,00 |
| 50 | R$ 85,00 | R$ 4.250,00 |

O produto possui apenas um registro em `product_prices`:

```json
{
  "minimumQuantity": 10,
  "unitPrice": 85.00
}
```

Comprar menos de 10 unidades não atende à regra comercial.

### 5.2. `TIERED` — Preço por quantidade

O preço unitário muda conforme a quantidade comprada.

**Exemplo: Chocolate gourmet tradicional**

Código: `012`

| Quantidade mínima | Preço unitário |
|---|---|
| 100 | R$ 3,70 |
| 200 | R$ 3,30 |
| 300 | R$ 3,00 |
| 500 | R$ 2,80 |

Exemplos de cálculo:

| Quantidade comprada | Faixa aplicada | Total |
|---|---|---|
| 100 | R$ 3,70 | R$ 370,00 |
| 150 | R$ 3,70 | R$ 555,00 |
| 200 | R$ 3,30 | R$ 660,00 |
| 250 | R$ 3,30 | R$ 825,00 |
| 300 | R$ 3,00 | R$ 900,00 |
| 500 | R$ 2,80 | R$ 1.400,00 |

**Regra de cálculo proposta:**

A faixa aplicável é aquela com a maior `minimum_quantity` que não ultrapassa a quantidade comprada.

O preço dessa faixa é aplicado a todas as unidades do produto.

Exemplo:

```text
Quantidade comprada: 250

Faixas disponíveis:
100 → R$ 3,70
200 → R$ 3,30
300 → R$ 3,00
500 → R$ 2,80

Faixa aplicável: 200

Cálculo:
250 × R$ 3,30 = R$ 825,00
```

Não se trata de uma cobrança progressiva por blocos.

### 5.3. `QUOTE` — Sob orçamento

O preço é definido individualmente após avaliação comercial.

**Exemplo: Projetos sob medida**

- Categoria: Caixas;
- Código comercial: a definir;
- Preço unitário: não definido;
- Quantidade mínima: a definir conforme o projeto.

Nesse caso:

```json
{
  "pricingType": "QUOTE",
  "prices": []
}
```

O produto pode ser cadastrado sem um preço predefinido.

O catálogo informa que valores, dimensões e prazos desses projetos são definidos por orçamento.

---

## 6. Tipos de taxas e adicionais

O campo `product_fees.charge_type` terá inicialmente três valores.

| Tipo | Significado | Exemplo |
|---|---|---|
| `PER_ORDER` | Valor cobrado uma vez por pedido aplicável | Laço: R$ 18,50 |
| `PER_UNIT` | Valor multiplicado pela quantidade de unidades | Caixa do gloss: R$ 3,00 por unidade |
| `FIRST_PURCHASE` | Valor cobrado apenas na primeira compra aplicável | Carimbo: R$ 55,00 |

### 6.1. `PER_ORDER` — Cobrança por pedido

**Exemplo: Biscoito amanteigado com laço**

Preço para 100 unidades: R$ 3,40 cada.

Adicional fixo pelo acabamento com laço: R$ 18,50.

```text
100 × R$ 3,40 = R$ 340,00

Adicional:
R$ 18,50

Total:
R$ 358,50
```

Para 200 unidades, considerando que o preço unitário aplicável continue R$ 3,40:

```text
200 × R$ 3,40 = R$ 680,00

Adicional:
R$ 18,50

Total:
R$ 698,50
```

**O adicional não é multiplicado pela quantidade.**

O valor de R$ 18,50 é acrescentado uma única vez ao subtotal do produto naquele pedido.

### 6.2. `PER_UNIT` — Cobrança por unidade

**Exemplo: Gloss Kiko com caixa personalizada**

O catálogo informa adicional de R$ 3,00 por unidade.

Para 20 unidades:

```text
Preço unitário do gloss:
R$ 29,00

20 × R$ 29,00 = R$ 580,00

Adicional das caixas:
20 × R$ 3,00 = R$ 60,00

Total:
R$ 640,00
```

Para 50 unidades:

```text
50 × R$ 27,00 = R$ 1.350,00

Adicional das caixas:
50 × R$ 3,00 = R$ 150,00

Total:
R$ 1.500,00
```

**O adicional é multiplicado pela quantidade comprada.**

### 6.3. `FIRST_PURCHASE` — Cobrança na primeira compra

**Exemplo: Carimbo exclusivo dos biscoitos**

O catálogo informa taxa única de R$ 55,00 no primeiro pedido.

Para 100 biscoitos com laço, na primeira compra aplicável:

```text
Biscoitos:
100 × R$ 3,40 = R$ 340,00

Laço:
R$ 18,50

Carimbo:
R$ 55,00

Total:
R$ 413,50
```

Na compra seguinte, considerando que o carimbo já tenha sido pago e possa ser reutilizado:

```text
Biscoitos:
100 × R$ 3,40 = R$ 340,00

Laço:
R$ 18,50

Carimbo:
R$ 0,00

Total:
R$ 358,50
```

**Importante:** o cadastro do produto registra a existência e o tipo da taxa. A verificação de cobrança anterior será responsabilidade futura do fluxo de vendas.

A identificação exata do carimbo reutilizável e a abrangência da primeira compra ainda precisarão ser definidas.

---

## 7. Pedido mínimo por código comercial

Cada produto possui seu próprio pedido mínimo.

**Não é permitido somar quantidades de códigos diferentes para atingir o mínimo de um produto.**

Exemplo:

| Código | Produto | Pedido mínimo | Quantidade solicitada | Situação |
|---|---|---|---|---|
| `011` | Biscoito sem laço | 50 | 60 | Válido |
| `014` | Biscoito com laço | 50 | 40 | Inválido |

Embora o total seja de 100 biscoitos, o código `014` não atingiu seu mínimo individual.

Para que ambos sejam válidos, seria necessário respeitar a quantidade mínima de cada código.

**Essa regra é independente do tipo de precificação.**

Produtos com preço fixo e produtos com preço variável podem possuir quantidades mínimas diferentes.

---

## 8. Cadastro completo em uma requisição

O endpoint será:

`POST /products`

Cada chamada cadastra somente um produto comercial.

O cadastro inclui:

1. Informações gerais do produto;
2. Faixas de preço;
3. Taxas e adicionais vinculados.

### Exemplo A — Produto com preço fixo

**Garrafa térmica com alça de silicone**

```json
{
  "code": "031",
  "name": "Garrafa térmica com alça de silicone",
  "category": "GARRAFAS",
  "description": "Garrafa térmica de 500 ml em aço inox.",
  "minimumQuantity": 10,
  "pricingType": "FIXED",
  "productionLeadTimeMinDays": 7,
  "productionLeadTimeMaxDays": 10,
  "active": true,
  "prices": [
    {
      "minimumQuantity": 10,
      "unitPrice": 85.00
    }
  ],
  "fees": []
}
```

### Exemplo B — Produto com preços por quantidade

**Chocolate gourmet tradicional**

```json
{
  "code": "012",
  "name": "Chocolate gourmet tradicional",
  "category": "GOURMET",
  "description": "Tablete de chocolate gourmet personalizado.",
  "minimumQuantity": 100,
  "pricingType": "TIERED",
  "shelfLifeDays": 180,
  "active": true,
  "prices": [
    {
      "minimumQuantity": 100,
      "unitPrice": 3.70
    },
    {
      "minimumQuantity": 200,
      "unitPrice": 3.30
    },
    {
      "minimumQuantity": 300,
      "unitPrice": 3.00
    },
    {
      "minimumQuantity": 500,
      "unitPrice": 2.80
    }
  ],
  "fees": []
}
```

### Exemplo C — Produto com adicional fixo e taxa de primeira compra

**Biscoito amanteigado com laço**

Código `014` utilizado apenas como exemplo.

```json
{
  "code": "014",
  "name": "Biscoito amanteigado com laço",
  "category": "GOURMET",
  "description": "Biscoito personalizado com acabamento em cetim.",
  "minimumQuantity": 50,
  "pricingType": "TIERED",
  "shelfLifeDays": 30,
  "active": true,
  "prices": [
    {
      "minimumQuantity": 50,
      "unitPrice": 3.55
    },
    {
      "minimumQuantity": 100,
      "unitPrice": 3.40
    }
  ],
  "fees": [
    {
      "name": "Acabamento com laço",
      "amount": 18.50,
      "chargeType": "PER_ORDER",
      "required": true,
      "active": true
    },
    {
      "name": "Carimbo exclusivo",
      "amount": 55.00,
      "chargeType": "FIRST_PURCHASE",
      "required": true,
      "active": true
    }
  ]
}
```

### Exemplo D — Produto com adicional opcional por unidade

**Gloss Kiko**

```json
{
  "code": "041",
  "name": "Gloss Kiko",
  "category": "GLOSS",
  "description": "Gloss personalizado com ácido hialurônico e bepantol.",
  "minimumQuantity": 10,
  "pricingType": "TIERED",
  "active": true,
  "prices": [
    {
      "minimumQuantity": 10,
      "unitPrice": 35.00
    },
    {
      "minimumQuantity": 20,
      "unitPrice": 29.00
    },
    {
      "minimumQuantity": 50,
      "unitPrice": 27.00
    }
  ],
  "fees": [
    {
      "name": "Caixa personalizada",
      "amount": 3.00,
      "chargeType": "PER_UNIT",
      "required": false,
      "active": true
    }
  ]
}
```

### Exemplo E — Produto sob orçamento

**Projeto de caixa sob medida**

```json
{
  "code": "094",
  "name": "Projeto de caixa sob medida",
  "category": "CAIXAS",
  "description": "Caixa personalizada conforme medidas e necessidades do cliente.",
  "minimumQuantity": null,
  "pricingType": "QUOTE",
  "active": true,
  "prices": [],
  "fees": []
}
```

O código `094` segue a numeração anteriormente proposta e deve ser confirmado caso sejam atribuídos códigos específicos por tamanho.

---

## 9. Regras de validação

O cadastro deverá rejeitar requisições com:

- Código comercial duplicado;
- Código fora do padrão de três dígitos;
- Nome vazio;
- Categoria inválida;
- Tipo de precificação inválido;
- Quantidade mínima igual ou inferior a zero para produtos que exigem mínimo;
- Preço negativo ou igual a zero;
- Faixas de preço duplicadas;
- Faixas abaixo do pedido mínimo;
- Produto `FIXED` com múltiplas faixas de preço;
- Produto `TIERED` sem faixas de preço;
- Produto `QUOTE` com preços predefinidos;
- Taxa com valor negativo;
- Tipo de cobrança inválido;
- Prazo máximo inferior ao mínimo.

Os campos de preço serão representados por `BigDecimal` no Kotlin, evitando erros de precisão monetária.

---

## 10. Persistência transacional

O cadastro será realizado em uma única transação.

Fluxo esperado:

```text
POST /products
      |
      v
Validar informações gerais
      |
      v
Validar código comercial
      |
      v
Validar preços e faixas
      |
      v
Validar taxas e adicionais
      |
      v
Salvar products
      |
      v
Salvar product_prices
      |
      v
Salvar product_fees
      |
      v
Confirmar transação
```

Se qualquer etapa falhar, a transação inteira será revertida.

**Não poderá existir produto parcialmente cadastrado.**

---

## 11. Decisões arquiteturais

### Por que usar três tabelas?

Porque representam responsabilidades distintas:

**`products`**

Identifica o produto e define suas características comerciais gerais.

**`product_prices`**

Define quanto custa cada unidade conforme a quantidade.

**`product_fees`**

Define valores adicionais que podem ser cobrados além do preço principal.

Essa separação evita repetir dados do produto em várias linhas e permite que cada código comercial tenha suas próprias condições.

### Por que não criar uma tabela de variações?

Porque a regra atual do negócio determina que produtos comercialmente distintos sejam cadastrados por códigos diferentes.

Exemplo:

- Biscoito sem laço: um código;
- Biscoito com laço: outro código.

Da mesma forma, se um tamanho de caixa tiver preço próprio e for tratado comercialmente como outro produto, receberá seu próprio código.

Cores e características meramente descritivas não precisam necessariamente gerar novos registros.

### Por que não colocar o preço diretamente em `products`?

Porque alguns produtos possuem várias faixas de preço.

Armazenar os preços em `product_prices` permite atender tanto produtos com valor fixo quanto produtos com valores variáveis.

### Por que não colocar as taxas diretamente em `products`?

Porque um produto pode possuir nenhuma, uma ou várias taxas, com formas de cobrança diferentes.

Exemplo:

```text
Biscoito com laço
    |
    |-- Laço: R$ 18,50 por pedido
    |
    |-- Carimbo: R$ 55,00 na primeira compra
```

---

## 12. Escopo atual

**Incluído nesta etapa:**

- Modelagem das três tabelas;
- Migrations Flyway;
- Entidades e enums Kotlin;
- Validações de cadastro;
- Persistência dos preços;
- Persistência das taxas;
- Endpoint `POST /products`;
- Testes unitários e de integração.

**Fora do escopo desta etapa:**

- Cadastro de vendas;
- Cálculo do valor de uma venda;
- Verificação de compras anteriores;
- Aplicação efetiva das taxas;
- Controle de pagamentos;
- Estoque;
- Composição dinâmica de kits.

Essas funcionalidades poderão utilizar futuramente as regras já cadastradas, sem exigir sua implementação agora.

---

## 13. Pontos pendentes de confirmação

Algumas decisões precisam ser confirmadas antes de consolidar as migrations:

1. **Códigos definitivos:** o código `014` para biscoito com laço é ilustrativo. Precisamos revisar a lista de códigos comerciais após separar os acabamentos.
2. **Limite de nove produtos por categoria:** o padrão de três dígitos precisa de uma estratégia de expansão caso uma categoria ultrapasse nove códigos.
3. **Carimbo:** definir como identificar o carimbo já pago e quando uma nova logomarca gera uma nova cobrança.
4. **Produtos sob orçamento:** confirmar se a quantidade mínima pode ficar indefinida (`null`).
5. **Prazo:** confirmar se todos os prazos serão armazenados em dias corridos ou se precisaremos distinguir dias úteis.
6. **Faixas intermediárias:** confirmar que a faixa anterior permanece válida até a próxima quantidade cadastrada. Esse comportamento foi adotado nos exemplos, mas não é explicitado para todas as quantidades intermediárias do catálogo.
7. **Validade de chocolates:** o catálogo informa seis meses. O exemplo usa 180 dias como aproximação; caso seja necessário preservar meses de calendário, o modelo deverá representar essa unidade explicitamente.

---

## 14. Resumo

O módulo seguirá estas regras centrais:

**Um código comercial = um produto independente.**

**Um produto = uma quantidade mínima própria.**

**Um produto = uma ou várias faixas de preço unitário.**

**Um produto = zero ou várias taxas comerciais.**

**Um cadastro = uma requisição `POST /products`.**

**Uma requisição = uma transação completa.**

A modelagem inicial será composta por:

```text
products
product_prices
product_fees
```

Essa estrutura cobre os modelos de precificação identificados no catálogo Personalité Creative 2026, respeitando o escopo atual de cadastro de produtos.