package com.listaai.recipe.adapter.outbound.ai;

import org.springframework.stereotype.Component;

@Component
public class RecipePromptBuilder {

    public String build(String recipe) {
        return """
                Extraia todos os ingredientes necessarios para preparar a receita informada e retorne apenas JSON valido.

                Formato exato de saida:
                {
                  "items": [
                    {
                      "name": "Arroz",
                      "quantity": 1,
                      "unit": "KG",
                      "purchased": false
                    }
                  ]
                }

                Regras obrigatorias:
                - Retorne somente o JSON.
                - Nao use markdown.
                - Nao escreva explicacoes, observacoes ou comentarios.
                - O JSON deve conter apenas a chave "items".
                - Cada item deve conter exatamente os campos: "name", "quantity", "unit", "purchased".
                - "purchased" deve ser sempre false.
                - "quantity" deve ser um numero inteiro.
                - "unit" deve ser apenas um destes valores: UN, KG, G, L, ML, BOX, PACK.
                - Extraia todos os ingredientes necessarios para a receita, nao apenas o ingrediente principal.
                - Se a receita listar ingredientes explicitamente, todos devem aparecer no array.
                - Se o usuario informar apenas o nome de um prato, gere os ingredientes tipicos necessarios para preparar uma versao classica dessa receita.
                - Se o usuario informar apenas o nome de um prato, nao retorne somente o nome do prato ou seu ingrediente principal.
                - Nao inclua utensilios, modo de preparo, tempo de cozimento, marcas ou campos extras.
                - Normalize os nomes dos ingredientes para nomes simples de lista de compras.

                Regras para quantidade e unidade:
                - Se a receita informar quantidade e unidade, use exatamente essa informacao convertida para os enums permitidos.
                - "1 kg de arroz" -> quantity: 1, unit: "KG"
                - "500 g de queijo" -> quantity: 500, unit: "G"
                - "2 litros de leite" -> quantity: 2, unit: "L"
                - "300 ml de oleo" -> quantity: 300, unit: "ML"
                - "1 caixa de creme de leite" -> quantity: 1, unit: "BOX"
                - "2 pacotes de macarrao" -> quantity: 2, unit: "PACK"
                - "3 ovos" -> quantity: 3, unit: "UN"

                Regras de inferencia:
                - Se a quantidade estiver explicita, preserve a quantidade.
                - Se a unidade estiver explicita, preserve a unidade usando apenas os enums permitidos.
                - Se houver ingrediente identificado sem quantidade explicita, use uma quantidade inteira plausivel para uma receita domestica comum.
                - Se houver ingrediente identificado sem unidade explicita, escolha a unidade mais adequada usando apenas os enums permitidos.
                - Se a entrada for apenas o nome de um prato, deduza os ingredientes essenciais de uma receita classica e objetiva.
                - Para entradas como "strogonoff de frango", inclua varios ingredientes necessarios, como frango, creme de leite, molho de tomate, cebola, alho e outros ingredientes basicos plausiveis.
                - Nao invente ingredientes raros, gourmet ou incomuns.
                - Se nao houver ingredientes identificaveis, retorne {"items":[]}.

                Exemplo:
                Entrada:
                <<<RECIPE>>>
                strogonoff de frango
                <<<END_RECIPE>>>

                Saida esperada:
                {
                  "items": [
                    {
                      "name": "Frango",
                      "quantity": 1,
                      "unit": "KG",
                      "purchased": false
                    },
                    {
                      "name": "Creme de leite",
                      "quantity": 2,
                      "unit": "BOX",
                      "purchased": false
                    },
                    {
                      "name": "Molho de tomate",
                      "quantity": 1,
                      "unit": "BOX",
                      "purchased": false
                    },
                    {
                      "name": "Cebola",
                      "quantity": 1,
                      "unit": "UN",
                      "purchased": false
                    },
                    {
                      "name": "Alho",
                      "quantity": 3,
                      "unit": "UN",
                      "purchased": false
                    }
                  ]
                }

                Interprete apenas o conteudo entre <<<RECIPE>>> e <<<END_RECIPE>>>.

                <<<RECIPE>>>
                %s
                <<<END_RECIPE>>>
                """.formatted(recipe == null ? "" : recipe.trim());
    }
}
