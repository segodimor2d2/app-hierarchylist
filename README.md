# App Matriz de Condorcet com pesos simplificados

https://github.com/segodimor2d2/app-hierarchylist

## Tela 1 Home: 
1. Botão 
    - Criar uma lista
        - Escolha uma pasta e de permissões para salvar a lista
    - Abrir uma lista
        - Escolha  uma pasta e de permissões para abrir a lista

2. 


1. Botão Procurar Selecionar a pasta de trabalho
    - Ver a possibilidade de Salvar a pasta depois da primeira vez
    - ter um botão para esquecer a pasta e poder usar outra
2. Imprime lista de arquivos (dentro da pasta)
    - Cada Arquivo poder ter um ícone sé o arquivo/lista foi processada identificada pelo prefix do nome
3. Se clicar num arquivo, => abre o arquivo na Tela 2
4. Botão para criar um arquivo
    - Pergunta para o nome do arquivo num TextField
    - Abre a Tela 2

## Tela 2 Edit:
1. Abre um TextField como o conteúdo do arquivo
2. Botão para salvar o arquivo
3. Botão para deletar o arquivo
4. Botão para fechar a tela => retorna para a Tela 1
5. Botão para iniciar => Tela 3, inicia processo de hierarquia


## Tela 3 HierchyProcess:
a HierchyScreen é uma tela que vai mostrar repetida uma comparação de 2 itens e vai me deixar selecionar qual é a mais importante com 3 botões,
cada comparação vai os botões e apos de apresentar uma comparação o programa vai salvar a opção selecionada e vai para a próxima comparação,
eu vou ter mais 2 botões de seguinte e anterior para navegar entre as comparações.

para comparar estou pensando em:
1. Mostra duas colunas
    ColumA | ColumB
     ItemA | ItemB

2. comparação de itens com 3 botões:
    `[A>B] [A=B] [A<B]`

    - -1(A mais importante que B),
    -  0(A e B são iguais),
    -  1(A é mais importante que B)

3. Botões de para navegar entre Items
    seguinte Item >>
    anterior Item <<

4. Botão Finalizar => 
    salva a lista ordenada com prefix identificando no nome do arquivo

5. Text Feedback da opção selecionada
    `[ - ] ó [A>B] ó [A=B] ó [A<B]`





## Tela 4 Ranking:
1. mostra uma lista com os itens ordenados
2. Possibilidade de mudar a ordem dos itens
3. Botão Salvar Nova ordem
4. Botão Cancelar Nova ordem
5. Botão Voltar para a Home


---


# **Método de Análise Hierárquica (Analytic Hierarchy Process - AHP)**.

### Características do Método AHP:
1. **Comparação Par a Par (Pairwise Comparison)**:  
   - Os itens são comparados dois a dois em termos de importância, preferência ou outro critério relevante.  
   - Exemplo: Se você está escolhendo entre três carros (A, B, C), compara A vs. B, A vs. C e B vs. C.

2. **Matriz de Comparação**:  
   - Os resultados das comparações são organizados em uma matriz quadrada, onde cada célula representa a importância relativa de um item em relação ao outro
   (usando uma escala, como a escala de Saaty: 1 a 9).

3. **Cálculo de Prioridades**:  
   - A matriz é usada para calcular pesos (autovetores) que hierarquizam os itens.  
   - Também é possível verificar a consistência das comparações (Índice de Consistência).

4. **Aplicações**:  
   - Tomada de decisão multicritério (ex.: seleção de projetos, escolha de fornecedores).  
   - Priorização de requisitos em gestão de produtos.

### Exemplo Simplificado:
Suponha que você queira hierarquizar 3 itens (A, B, C) com base em preferências:
- Matriz de comparação (exemplo):
  ```
  A vs. A = 1   | A vs. B = 3   | A vs. C = 5
  B vs. A = 1/3 | B vs. B = 1   | B vs. C = 2
  C vs. A = 1/5 | C vs. B = 1/2 | C vs. C = 1
  ```
- A partir disso, calculam-se os pesos relativos (autovetores) para ordenar A, B, C.

---
qual é a diferença entre Método de Borda e Método de Borda Modificado

# **Método de Borda Modificado** ou **Matriz de Condorcet com pesos simplificados**.


Sim! O método que você está descrevendo, que usa os valores **-1, 0 e 1** para comparação par a par, é conhecido como
**Método de Borda Modificado** ou **Matriz de Condorcet com pesos simplificados**.
Ele é uma variação de técnicas de votação ou hierarquização baseadas em comparações binárias ou ternárias.  

### **Como Funciona?**  
Nesse método, cada item é comparado com os outros usando uma escala simples:  
- **1**: Item A é **melhor/mais importante** que B.  
- **0**: Itens A e B são **iguais/indiferentes** em importância.  
- **-1**: Item A é **pior/menos importante** que B.  

Essas comparações são registradas em uma **matriz de dominância**, e a pontuação final de cada item é calculada somando os resultados de suas comparações.  

---

### **Passo a Passo**  
1. **Liste os Itens**:  
   Ex.: A, B, C.  

2. **Compare Par a Par**:  
   - A vs. B → Se A > B, escreva **1** para A e **-1** para B.  
   - A vs. C → Se A = C, escreva **0** para ambos.  
   - B vs. C → Se B < C, escreva **-1** para B e **1** para C.  

3. **Construa a Matriz de Comparação**:  
   |   | A | B | C | **Total** |  
   |---|---|---|---|--------|  
   | A | - | 1 | 0 | **1**  |  
   | B | -1 | - | -1 | **-2** |  
   | C | 0 | 1 | - | **1**  |  

4. **Calcule a Pontuação Final**:  
   - **A**: 1 (A vs. B) + 0 (A vs. C) = **1**  
   - **B**: -1 (B vs. A) + -1 (B vs. C) = **-2**  
   - **C**: 0 (C vs. A) + 1 (C vs. B) = **1**  

5. **Hierarquize**:  
   - **1º lugar**: A e C (empatados com **1** ponto).  
   - **2º lugar**: B (**-2** pontos).  

---

### **Vantagens e Aplicações**  
✔ **Simplicidade**: Mais fácil que o AHP (não requer cálculos complexos).  
✔ **Útil para rankings rápidos**: Priorização de ideias, seleção de alternativas.  
✔ **Evita inconsistências**: Como no AHP, onde comparações excessivas podem gerar contradições.  

### **Limitações**  
✖ **Pouco granular**: Só diferencia em "melhor", "igual" ou "pior".  
✖ **Empates frequentes**: Se muitos itens forem considerados iguais (0).  

---

### **Alternativas Parecidas**  
- **Método de Condorcet**: Usa comparações par a par para definir um "vencedor" que domina todos.  
- **Método de Copeland**: Soma vitórias e derrotas nas comparações (similar, mas conta vitórias totais).  
- **Método Borda Clássico**: Rankeia itens por posição, não só por comparação binária.  
# **Método de Borda Modificado** ou **Matriz de Condorcet com pesos simplificados**.
# **Método de Análise Hierárquica (Analytic Hierarchy Process - AHP)**.


---

# Estrutura

Você está usando uma **estrutura modularizada por função (ou "por recurso")**, muito comum e **recomendada em projetos Android modernos com Jetpack Compose**.

### 📁 Estrutura atual e seus significados:

```
├── MainActivity.kt                    <-- Ponto de entrada principal da aplicação
├── navigation/                        <-- Gerencia a navegação entre telas (NavGraph)
├── ui/
│   ├── screen/                        <-- Contém as telas (composables) da UI
│   └── theme/                         <-- Define o tema visual (cores, fontes, etc.)
├── util/                              <-- Funções utilitárias (e.g., leitura de arquivos, prefs)
├── viewmodel/                         <-- Contém os ViewModels (estado e lógica de UI)
```

---

### ✅ Isso se alinha com a arquitetura recomendada pela Google:

* **UI Layer (Compose Screens):** `ui/screen`
* **State Management:** `viewmodel/`
* **Navigation:** `navigation/`
* **Utilities:** `util/` (às vezes movido para `core/` ou `common/`)
* **Theme:** `ui/theme/`

---

### 📦 Nome técnico:

Essa abordagem é chamada de **"modularização por feature (ou por camada funcional)"** dentro de um único módulo. Ela é um primeiro passo antes de um projeto escalonar para **modularização real entre módulos Gradle**, como `:feature:home`, `:core:ui`, etc.

---

Se quiser, podemos continuar com melhorias incrementais, como:

* Adicionar suporte a `Hilt` ou `Koin` para injeção de dependência.
* Criar um `model/` para tipos como `MarkdownFile`.
* Separar mais logicamente `data/` e `domain/`.

---


eu vou usar uma lista e aplicar o Matriz de Condorcet com pesos simplificadoso,
na primeira parte eu pegaria um texto e vou fazer um split para criar uma lista com cada uma das linhas, cada linha é um titem da lista;
agora eu gostaria de saber omo eu posso construir o método em  algoritmo paso a paso python



Claro, Segodimo! Vamos construir um algoritmo passo a passo em Python para aplicar a **Matriz de Condorcet com pesos simplificados**, assumindo que você já tem um **texto com múltiplas linhas**, onde **cada linha é um item** (ex: alternativas em uma eleição ou votação).

---

### 🔹 Etapa 1: Definir o problema

Você quer:

1. Receber um texto onde cada linha representa um item.
2. Comparar todos os itens entre si (votação par a par).
3. Aplicar a Matriz de Condorcet usando "pesos simplificados".

---

### 🔹 Etapa 2: Interpretação dos pesos simplificados

Vamos assumir que:

* Cada pessoa (ou linha) dá uma ordenação dos itens (ex: "A > B > C"), ou
* Cada item tem um peso numérico simples (ex: "A 3", "B 1", "C 2").

**Confirme como os pesos são representados**, mas para agora, vamos usar o seguinte formato de entrada:

```text
Item A
Item B
Item C
```

---

### 🔹 Etapa 3: Algoritmo passo a passo (Condorcet simplificado)

```python
def condorcet(texto):
    # 1. Criar lista de itens a partir do texto
    linhas = texto.strip().split('\n')
    itens = [linha.strip() for linha in linhas if linha.strip()]

    # 2. Inicializar matriz de vitórias (n x n)
    n = len(itens)
    matriz = [[0 for _ in range(n)] for _ in range(n)]

    # 3. Simular votações com pesos (simplificação: posição na lista = preferência)
    # Para cada par (i, j), se i < j na lista, item i ganha
    for i in range(n):
        for j in range(n):
            if i == j:
                continue
            # Suponha que quem aparece primeiro é mais preferido
            matriz[i][j] += 1  # i ganha de j

    # 4. Mostrar a matriz de vitórias
    print("Matriz de vitórias Condorcet:")
    print("     " + "  ".join(f"{itens[j][:6]:>6}" for j in range(n)))
    for i in range(n):
        linha = [f"{matriz[i][j]:>6}" for j in range(n)]
        print(f"{itens[i][:6]:>6} {'  '.join(linha)}")

    # 5. Contar número de vitórias para cada item
    vitorias = [0] * n
    for i in range(n):
        for j in range(n):
            if matriz[i][j] > matriz[j][i]:
                vitorias[i] += 1

    # 6. Exibir resultados
    ranking = sorted(zip(itens, vitorias), key=lambda x: -x[1])
    print("\nRanking final (Condorcet simplificado):")
    for i, (item, pontos) in enumerate(ranking):
        print(f"{i+1}. {item} - {pontos} vitórias")

# Exemplo de uso
texto = """Banana
Maçã
Laranja
Uva"""

condorcet(texto)
```

---

### 🔹 Explicação

* **Passo 1**: Lê o texto e transforma em lista (`split('\n')`).
* **Passo 2**: Cria uma matriz n x n.
* **Passo 3**: Compara todos os pares (i, j). Neste exemplo, assume que todos preferem os primeiros itens da lista.
* **Passo 4**: Imprime a matriz de vitórias.
* **Passo 5**: Conta quantas vitórias cada item teve.
* **Passo 6**: Exibe o ranking final.

---

Quer que eu adapte o algoritmo para um **formato de entrada diferente** (como votos ordenados, pesos explícitos, ou votos de várias pessoas)?


---

Agora tenho uma tela chamada HierchyScreen,
que recebe do viewmodel SharedViewModel os dados processData 
aqui esta o repositorio do projeto de android en para vc ver a estrutura en github: https://github.com/segodimor2d2/app-hierarchylist

por agora estou recebendo o dados do processData,
com esses dados eu quero fazer um split para cada salto de linha e assim obter uma lista.
eu quero aplicar com essa lista, a Matriz de Condorcet com pesos simplificadoso e assim poder obter cada par de itens que eu vou querer comparar

como eu poderia fazer isso?






















---

Profavor analise o seguinte repositório no github:

Profavor analise os arquivos dentro de testfiles no seguiente path /app/src/main/java/com/testfiles do seguinte repositório no github:
https://github.com/segodimor2d2/app-hierarchylist


├── MainActivity.kt
├── navigation
│   └── AppNavGraph.kt
├── ui
│   ├── screen
│   │   ├── CompareScreen.kt
│   │   ├── EditScreen.kt
│   │   ├── HomeScreen.kt
│   │   └── RankingScreen.kt
│   └── theme
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── util
│   ├── FileUtils.kt
│   └── PreferencesUtil.kt
└── viewmodel
    └── SharedViewModel.kt

porfavor me diga como eu consegui criar um arquivo na tela HomeScreen e salvar na pasta que di permições na pasta?





---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomHeaderRanking(navController: NavController) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
        }
        Text(
            text = "Ranking",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

---

# Como Mostrar/Esconder Botões no Jetpack Compose

Para fazer com que o segundo botão apareça quando o primeiro for pressionado e o primeiro desapareça, você pode usar um estado para controlar a visibilidade. Aqui está como implementar:

```kotlin
@Composable
fun TwoButtonsScreen() {
    // Estado para controlar qual botão está visível
    var showFirstButton by remember { mutableStateOf(true) }

    Column {
        if (showFirstButton) {
            Button(
                onClick = {
                    // Quando clicado, esconde este botão e mostra o próximo
                    showFirstButton = false
                }
            ) {
                Text("Criar uma lista")
            }
        }

        if (!showFirstButton) {
            Button(
                onClick = { 
                    folderPicker.launch(null) 
                    // Se quiser voltar ao estado original depois:
                    // showFirstButton = true
                }
            ) {
                Text("Escolha uma pasta e de permissões para Criar a lista")
            }
        }
    }
}
```

## Explicação:

1. Criamos um estado `showFirstButton` que controla qual botão está visível
2. Usamos condicionais `if` para mostrar/esconder os botões com base no estado
3. Quando o primeiro botão é clicado, mudamos o estado para `false`
4. O segundo botão só aparece quando `showFirstButton` é `false`

## Alternativa mais compacta:

Se preferir um código mais conciso, pode usar `AnimatedVisibility` para transições suaves:

```kotlin
@Composable
fun TwoButtonsScreen() {
    var showFirstButton by remember { mutableStateOf(true) }

    Column {
        AnimatedVisibility(visible = showFirstButton) {
            Button(onClick = { showFirstButton = false }) {
                Text("Criar uma lista")
            }
        }

        AnimatedVisibility(visible = !showFirstButton) {
            Button(onClick = { folderPicker.launch(null) }) {
                Text("Escolha uma pasta e de permissões para Criar a lista")
            }
        }
    }
}
```

Lembre-se de importar `androidx.compose.animation.AnimatedVisibility` se usar esta versão.

---
$$$$

Sim. São métodos diferentes, mas relacionados. Todos podem ser utilizados para ordenar alternativas, estabelecer prioridades ou escolher a melhor opção. A principal diferença está em como cada método calcula a preferência e determina o vencedor.

Uma distinção importante: Condorcet e Copeland trabalham com comparações par a par; Borda utiliza posições em rankings; AHP utiliza comparações de intensidade de preferência.

## 1. Comparação dos métodos

| Método                                      | Como funciona                                                        | Resultado                                        |
| ------------------------------------------- | -------------------------------------------------------------------- | ------------------------------------------------ |
| Condorcet                                   | Compara cada alternativa contra todas as outras                      | Vencedor que derrota todas as demais, se existir |
| Copeland                                    | Conta vitórias e derrotas nas comparações par a par                  | Ranking por saldo de vitórias                    |
| Borda Clássico                              | Atribui pontos conforme a posição de cada alternativa                | Ranking por soma de pontos                       |
| Borda Modificado                            | Adapta a pontuação do Borda conforme regras específicas              | Ranking ponderado ou ajustado                    |
| Matriz de Condorcet com pesos simplificados | Atribui pontos ou pesos aos resultados das comparações               | Ranking por pontuação                            |
| AHP                                         | Compara alternativas ou critérios usando intensidades de preferência | Pesos relativos e ranking de prioridades         |

Observação: "Borda Modificado" e "Matriz de Condorcet com pesos simplificados" não identificam necessariamente um único método padronizado. É preciso definir a fórmula utilizada.

## 2. Exemplo prático

Imagine que precisamos escolher entre três projetos:

- A — Desenvolver aplicativo Android
- B — Desenvolver servidor backend
- C — Desenvolver interface web

Cinco pessoas classificam os projetos por preferência.

| Pessoa | 1º lugar | 2º lugar | 3º lugar |
| ------ | -------- | -------- | -------- |
| 1      | A        | B        | C        |
| 2      | A        | B        | C        |
| 3      | B        | C        | A        |
| 4      | B        | C        | A        |
| 5      | C        | A        | B        |

### Condorcet

Comparamos os projetos dois a dois.

| Comparação | Votos      | Vencedor |
| ---------- | ---------- | -------- |
| A × B      | A: 3, B: 2 | A        |
| A × C      | A: 2, C: 3 | C        |
| B × C      | B: 4, C: 1 | B        |

Temos um ciclo:

A vence B, B vence C, C vence A.

Resultado: não existe vencedor de Condorcet, porque nenhuma alternativa vence todas as outras.

### Copeland

Usamos as mesmas comparações, mas atribuímos +1 para cada vitória e −1 para cada derrota.

| Projeto | Vitórias | Derrotas | Pontuação |
| ------- | -------- | -------- | --------- |
| A       | 1        | 1        | 0         |
| B       | 1        | 1        | 0         |
| C       | 1        | 1        | 0         |

Resultado: empate triplo. Copeland produz uma pontuação, mas não necessariamente resolve todos os empates.

### Borda Clássico

Com três alternativas, usamos:

- 1º lugar = 2 pontos
- 2º lugar = 1 ponto
- 3º lugar = 0 pontos

| Projeto | Pontos | Posição     |
| ------- | ------ | ----------- |
| A       | 5      | 2º (empate) |
| B       | 6      | 1º          |
| C       | 4      | 3º          |

Correção da soma: A recebe 5, B recebe 6 e C recebe 4.

Resultado: B vence pelo método de Borda, mesmo sem existir vencedor de Condorcet.

## 3. E o AHP?

O Analytic Hierarchy Process (AHP) é diferente dos anteriores porque permite avaliar quanto uma alternativa é preferível à outra, em vez de apenas saber qual vence.

Por exemplo, uma matriz de comparação poderia ser:

|   | A   | B   | C |
| - | --- | --- | - |
| A | 1   | 3   | 5 |
| B | 1/3 | 1   | 2 |
| C | 1/5 | 1/2 | 1 |

Os números representam a intensidade das preferências, frequentemente usando a escala de Saaty de 1 a 9.

O AHP calcula prioridades relativas e permite verificar a consistência dos julgamentos. Também permite organizar critérios como custo, desempenho, segurança e facilidade de implementação.

## 4. Qual método escolher?

| Sua necessidade                                       | Minha recomendação                      |
| ----------------------------------------------------- | --------------------------------------- |
| Identificar uma alternativa que vence todas as outras | Condorcet                               |
| Criar um ranking a partir de comparações binárias     | Copeland                                |
| Criar ranking simples com votos ordenados             | Borda Clássico                          |
| Personalizar a pontuação das posições                 | Borda Modificado                        |
| Comparar alternativas por uma escala própria de pesos | Matriz ponderada, com fórmula explícita |
| Tomar decisões envolvendo vários critérios            | AHP                                     |

Minha recomendação: se você está desenvolvendo um sistema em que o usuário compara dois itens por vez para estabelecer prioridades, começaria com Copeland, por ser simples de implementar e interpretar.

Se o usuário precisa indicar também a intensidade da preferência, por exemplo, "A é muito mais importante que B", consideraria o AHP.

Uma pergunta importante para definir a arquitetura: você quer que o usuário compare itens dois a dois, escolhendo apenas o vencedor, ou quer permitir que ele indique também o grau de preferência entre os itens? Essa diferença muda bastante o método mais adequado.

---
$$$$

## Quadro comparativo dos métodos de decisão e classificação

Considerei nível de apuração como o grau de detalhamento e precisão que o método oferece para diferenciar alternativas, identificar preferências e justificar o resultado.

| Método                                      | Como funciona                                                                                                        | Vantagens                                                                                           | Desvantagens                                                                                                   | Resultado                                                     | Nível de apuração                                                                  |
| ------------------------------------------- | -------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------- | ---------------------------------------------------------------------------------- |
| Condorcet                                   | Compara todas as alternativas duas a duas, identificando quem vence cada disputa.                                    | Respeita o vencedor majoritário de todas as comparações, quando existe; fácil de justificar.        | Pode gerar ciclos sem vencedor; não mede intensidade das preferências.                                         | Vencedor de Condorcet ou ausência de vencedor.                | Alto para consenso majoritário; limitado para ranking completo.                    |
| Copeland                                    | Soma vitórias e derrotas das comparações par a par.                                                                  | Simples, transparente; produz pontuação para todas as alternativas.                                 | Pode gerar empates; normalmente ignora a margem das vitórias.                                                  | Ranking por pontuação de vitórias e derrotas.                 | Médio-alto — considera todos os confrontos, mas perde detalhes das margens.        |
| Borda Clássico                              | Cada posição no ranking recebe pontos; somam-se os pontos de todos os participantes.                                 | Simples; considera toda a ordenação; normalmente produz um ranking completo, com possíveis empates. | Depende da pontuação escolhida; pode favorecer alternativas bem posicionadas sem maioria nas disputas diretas. | Ranking por pontuação acumulada.                              | Médio-alto — considera posições, não intensidade das preferências.                 |
| Borda Modificado                            | Altera a pontuação do Borda conforme pesos ou regras definidos.                                                      | Flexível; permite privilegiar determinadas posições ou critérios.                                   | A escolha dos pesos pode introduzir subjetividade e alterar significativamente o resultado.                    | Ranking por pontuação ajustada.                               | Variável (médio a alto) — depende da fórmula adotada.                              |
| Matriz de Condorcet com pesos simplificados | Compara alternativas duas a duas e atribui pontuações ponderadas aos resultados.                                     | Permite incorporar margens ou intensidades, dependendo da implementação; pode produzir ranking.     | Não é um método único padronizado; pesos arbitrários podem distorcer resultados.                               | Matriz de comparações e ranking ponderado.                    | Variável (médio a alto) — depende dos pesos e da informação coletada.              |
| AHP (Analytic Hierarchy Process)            | Realiza comparações par a par usando intensidades de preferência; calcula pesos relativos e verifica a consistência. | Permite múltiplos critérios; quantifica prioridades; verifica coerência dos julgamentos.            | Exige mais comparações; torna-se trabalhoso com muitas alternativas; resultados dependem dos julgamentos.      | Ranking, prioridades relativas e indicadores de consistência. | Muito alto em detalhamento analítico, desde que os julgamentos sejam consistentes. |

## Comparação por características

✓ = oferece a característica; ◐ = depende da implementação ou do caso; — = não oferece diretamente.

| Característica                         | Condorcet | Copeland | Borda | Borda Mod. | Matriz ponderada | AHP |
| -------------------------------------- | --------- | -------- | ----- | ---------- | ---------------- | --- |
| Comparação par a par                   | ✓         | ✓        | —     | ◐          | ✓                | ✓   |
| Ranking completo garantido sem empates | —         | —        | —     | —          | —                | —   |
| Considera intensidade de preferência   | —         | —        | —     | ◐          | ◐                | ✓   |
| Permite múltiplos critérios            | ◐         | ◐        | ◐     | ◐          | ◐                | ✓   |
| Verifica consistência dos julgamentos  | —         | —        | —     | —          | —                | ✓   |
| Implementação simples                  | ✓         | ✓        | ✓     | ◐          | ◐                | —   |
| Justificativa detalhada da decisão     | ◐         | ◐        | ◐     | ◐          | ◐                | ✓   |

## Esforço de apuração

Para comparar os métodos, considere um cenário com 10 alternativas.

| Método           | Informações necessárias                                                                 | Esforço       |
| ---------------- | --------------------------------------------------------------------------------------- | ------------- |
| Condorcet        | Até 45 confrontos distintos por avaliador, derivados de rankings ou comparações diretas | Médio         |
| Copeland         | Os mesmos 45 confrontos                                                                 | Médio         |
| Borda Clássico   | Um ranking de 10 alternativas por avaliador                                             | Baixo         |
| Borda Modificado | Ranking e regras adicionais de pontuação                                                | Baixo a médio |
| Matriz ponderada | Até 45 comparações, com possíveis pesos                                                 | Médio a alto  |
| AHP              | 45 julgamentos par a par para um conjunto de 10 elementos em um nível da hierarquia     | Alto          |

Crescimento das comparações par a par

05511016522035101520

Quantidade de pares distintos: n × (n − 1) / 2. Não inclui comparações adicionais entre critérios no AHP.

Quanto mais alternativas, maior o esforço de coleta de preferências, especialmente em avaliações interativas.

## Avaliação geral

Esta é uma avaliação prática para escolha de arquitetura, não uma classificação científica absoluta de precisão.

| Objetivo                                           | Método mais indicado |
| -------------------------------------------------- | -------------------- |
| Encontrar uma escolha majoritariamente dominante   | Condorcet            |
| Ordenar alternativas por confrontos diretos        | Copeland             |
| Obter um ranking simples com poucos dados          | Borda Clássico       |
| Personalizar regras de pontuação                   | Borda Modificado     |
| Criar comparações ponderadas customizadas          | Matriz ponderada     |
| Analisar decisões complexas com diversos critérios | AHP                  |

Uma distinção importante: maior detalhamento de apuração não significa automaticamente maior precisão ou justiça. O AHP fornece informações mais ricas sobre prioridades e consistência, mas não é necessariamente superior ao Condorcet ou ao Copeland em uma votação coletiva.

Para um aplicativo em que o usuário escolhe entre duas alternativas por vez, Copeland é um bom ponto de partida. Se você quiser mostrar também por quanto uma alternativa é preferida e construir prioridades numéricas, vale considerar AHP ou uma matriz ponderada com regras bem definidas.

---
$$$$

O AHP (Analytic Hierarchy Process), ou Processo de Análise Hierárquica, é um método de tomada de decisão que compara alternativas duas a duas e calcula o peso ou a importância relativa de cada uma.

A principal diferença em relação ao Condorcet é:

- Condorcet: pergunta qual alternativa é preferida.
- AHP: pergunta qual alternativa é preferida e quanto ela é mais importante que a outra.

Por exemplo, em vez de apenas informar que A é melhor que B, no AHP você pode indicar que A é 3, 5 ou 7 vezes mais importante que B, segundo a escala de julgamento adotada.

## 1. A escala de comparação do AHP

O AHP tradicional utiliza a escala fundamental de Saaty:

| Valor      | Significado             |
| ---------- | ----------------------- |
| 1          | Importância igual       |
| 3          | Importância moderada    |
| 5          | Importância forte       |
| 7          | Importância muito forte |
| 9          | Importância extrema     |
| 2, 4, 6, 8 | Valores intermediários  |

Se A é considerado 5 vezes mais importante que B, a comparação inversa recebe o valor \\(1/5\\).

## 2. Exemplo prático

Imagine que precisamos escolher qual projeto desenvolver primeiro:

- A — Aplicativo Android
- B — Servidor Backend
- C — Interface Web

Vamos comparar os três projetos considerando um único critério: prioridade de desenvolvimento.

Matriz AHP de comparação

|   | A   | B   | C |
| - | --- | --- | - |
| A | 1   | 3   | 5 |
| B | 1/3 | 1   | 2 |
| C | 1/5 | 1/2 | 1 |

Esses valores significam que A tem preferência moderada sobre B, A tem preferência forte sobre C e B possui preferência intermediária entre igual e moderada sobre C.

## 3. Como o AHP calcula o resultado?

O método transforma essa matriz em um vetor de prioridades relativas, normalmente usando o autovetor principal da matriz.

Para nosso exemplo, o resultado aproximado é:

Ranking de prioridades

1º A — Aplicativo Android

64,8%

2º B — Servidor Backend

23,0%

3º C — Interface Web

12,2%

Valores aproximados. As prioridades somam 100% e indicam pesos relativos, não probabilidades de sucesso.

Resultado: o projeto A tem o maior peso relativo e, portanto, seria a primeira prioridade segundo os julgamentos informados.

## 4. Teste interativo do AHP

Você pode alterar as preferências abaixo e observar como os pesos e o ranking mudam automaticamente.

Comparações par a par

Comparação 1: A — Android × B — Backend

A — Android é mais importante

B — Backend é mais importante

Moderada (3)

Comparação 2: A — Android × C — Web

A — Android é mais importante

C — Web é mais importante

Forte (5)

Comparação 3: B — Backend × C — Web

B — Backend é mais importante

C — Web é mais importante

Leve (2)

Resultado calculado

1º A — Android

64.8%

2º B — Backend

23.0%

3º C — Web

12.2%

Razão de consistência (CR)

0.32%

Os julgamentos estão dentro do limite convencional de consistência (CR < 10%).

Restaurar valores iniciais

## 5. Uma vantagem importante: verificar a consistência

Imagine que você informou:

- A é 3 vezes mais importante que B.
- B é 3 vezes mais importante que C.
- A é 2 vezes mais importante que C.

Existe uma inconsistência: as duas primeiras comparações sugerem uma razão de 9 entre A e C, mas a última comparação indica apenas 2.

O AHP identifica essas divergências utilizando a Razão de Consistência (CR).

De modo geral, valores de CR inferiores a 0,10 são considerados aceitáveis, embora esse limite seja uma convenção e dependa do contexto.

## 6. AHP com múltiplos critérios

O AHP também pode ser hierárquico, isto é, avaliar alternativas usando vários critérios.

Objetivo: escolher o projeto prioritário

Custo

Impacto

Urgência

Android

Backend

Web

Hierarquia simplificada. Cada alternativa é avaliada em relação a cada critério.

Nesse cenário, o processo é:

1. Comparar os critérios entre si para calcular sua importância relativa.
2. Comparar os projetos dentro de cada critério.
3. Multiplicar os pesos locais das alternativas pelos pesos dos critérios.
4. Somar os resultados para obter a prioridade global de cada projeto.

## 7. AHP versus Condorcet

| Característica                  | Condorcet         | AHP                            |
| ------------------------------- | ----------------- | ------------------------------ |
| Compara duas alternativas       | Sim               | Sim                            |
| Mede intensidade da preferência | Não               | Sim                            |
| Calcula pesos percentuais       | Não diretamente   | Sim                            |
| Permite múltiplos critérios     | Com adaptações    | Nativamente                    |
| Detecta inconsistência          | Identifica ciclos | Calcula índice de consistência |
| Pode não ter vencedor           | Sim               | Normalmente gera prioridades   |
| Complexidade matemática         | Baixa             | Média a alta                   |

Para seu aplicativo, se a intenção for permitir que uma pessoa compare diversos itens e obtenha um ranking de prioridades, o AHP é interessante porque entrega três informações: posição no ranking, peso relativo e consistência das escolhas.

A principal desvantagem é o crescimento do número de comparações: com 20 itens seriam 190 comparações por conjunto de julgamentos, o que pode tornar o processo cansativo para o usuário.


---
$$$$


na tela de Edit list eu quero ter dois botões para escolher entre MBC e AHP, o botão MBC é o default, se cliclar em AHP o MBC fica desativado e AHP ativo
para isso quero uma variable varMetodo = "mbc" para guardar "mbc" ou "ahp"

na tela de Edit list quero por um "?" do lado direito dois botões MBC e AHP, ao clicar nele aparece um comentario com a explicação do método escolhido:

Borda Modificado (MBC):
Método de Borda Modificado ou Matriz de Condorcet com pesos simplificados,
Ele é uma variação de técnicas de votação ou hierarquização baseadas em comparações binárias ou ternárias,
O método que você está descrevendo, que usa os valores **-1, 0 e 1** para comparação par a par.

Analytic Hierarchy Process - AHP:
Processo de Análise Hierárquica, é um método de tomada de decisão que compara alternativas duas a duas e calcula o peso ou a importância relativa de cada uma.

eu quero duplicar ui/screen/CompareScreen.kt para ao escolher MBC ou AHP e eu possa entrar no processamento de Humeraquia usando o metodo Analytic Hierarchy Process - AHP


---
$$$$

para cada slide de perguntas,
eu quero criar uma area retangular que reconhece o touch,
a area horizontal vai ser dividida em 3 partes,
a ezquerda será a opçao A,
no meio seria igualmente importantes,
a direita será a opçao B,
ao deslizar na area,
um texto embaixo da area vai mostrar a que posiçao corresponde o lugar onde estou deslizando o dedo na area,
se eu soltar o dedo da area, o valor vai ser salvo na resposta
e vai pulata pro proximo slide.

seja alterado

---
$$$$

```bash
cd /home/segodimo/05android/05hierarchylist/ | term


adb shell cmd package resolve-activity --brief com.testfiles

com.testfiles/.MainActivity

testfiles
adb shell am force-stop com.rec.termuxsbs && adb shell am start -n com.rec.termuxsbs/.MainActivity
adb shell am start -n com.rec.termuxsbs/.MainActivity

e /home/segodimo/05android/05hierarchylist/app/src/main/java/com/testfiles/MainActivity.kt
e /home/segodimo/05android/05hierarchylist/app/src/main/AndroidManifest.xml
e app/src/main/java/com/testfiles/viewmodel/SharedViewModel.kt
e
app/src/main/java/com/testfiles/ui/screen/EditScreen.kt

```

---
$$$$


