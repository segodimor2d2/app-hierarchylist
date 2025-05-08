# App Matriz de Condorcet com pesos simplificados

https://github.com/segodimor2d2/app-hierarchylist

## Tela 1 Home: 
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
Eu subi de novo as atualisações no repositorio, Profavor analise o repositório no github:
https://github.com/segodimor2d2/app-hierarchylist

Eu ainda não estou consegindo ver os pontos no RankingScreen


eu fiz umas mudanças mas o programa quebrou ao precionar o botão Preocess na EditScreen, e indo para a tela CompareScreen
aqui mais dados sobre as mudanças que fiz:



➜  05hierarchylist git:(main) ✗ gs 
On branch main
Your branch is up to date with 'origin/main'.

Changes to be committed:
  (use "git restore --staged <file>..." to unstage)
        renamed:    app/src/main/java/com/testfiles/ui/screen/HierchyProcess.kt -> app/src/main/java/com/testfiles/ui/screen/CompareSc
reen.kt

Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git restore <file>..." to discard changes in working directory)
        modified:   README.md
        modified:   app/src/main/java/com/testfiles/navigation/AppNavGraph.kt
        modified:   app/src/main/java/com/testfiles/ui/screen/CompareScreen.kt

➜  05hierarchylist git:(main) ✗ git diff .                                                 
diff --git a/README.md b/README.md
index fbf4d9c..a2fa42f 100644
--- a/README.md
+++ b/README.md
@@ -51,7 +51,7 @@ Qual recurso do android com jetpack poderia me ajudar a fazer isso?
 que opões eu posso usar?
 Te alguma sugestão de como fazer isso?
 
-## Tela 4 Hierchy:
+## Tela 4 Ranking:
 1. mostra uma lista com os itens ordenados
 2. Possibilidade de mudar a ordem dos itens
 3. Botão Salvar Nova ordem
diff --git a/app/src/main/java/com/testfiles/navigation/AppNavGraph.kt b/app/src/main/java/com/testfiles/navigation/AppNavGraph.kt

index 9af1e5b..6c642f7 100644
--- a/app/src/main/java/com/testfiles/navigation/AppNavGraph.kt
+++ b/app/src/main/java/com/testfiles/navigation/AppNavGraph.kt
@@ -6,7 +6,7 @@ import androidx.navigation.compose.NavHost
 import androidx.navigation.compose.composable
 import com.testfiles.ui.screen.EditScreen
 import com.testfiles.ui.screen.HomeScreen
-import com.testfiles.ui.screen.HierarchyProcessScreen
+import com.testfiles.ui.screen.CompareScreen
 import com.testfiles.viewmodel.SharedViewModel
 
 @Composable
@@ -35,8 +35,8 @@ fun AppNavGraph(
         }
 
         // Nova tela de Processamento (adicionada)
-        composable("processScreen") {
-            HierarchyProcessScreen(
+        composable("compare") {
+            CompareScreen(
                 navController = navController,
                 viewModel = viewModel
             )
diff --git a/app/src/main/java/com/testfiles/ui/screen/CompareScreen.kt b/app/src/main/java/com/testfiles/ui/screen/CompareScreen.k
t
index c78a447..87aec21 100644
--- a/app/src/main/java/com/testfiles/ui/screen/CompareScreen.kt
+++ b/app/src/main/java/com/testfiles/ui/screen/CompareScreen.kt
@@ -12,7 +12,7 @@ import androidx.navigation.NavController
 import com.testfiles.viewmodel.SharedViewModel
 
 @Composable
-fun HierarchyProcessScreen(
+fun CompareScreen(
     navController: NavController,
     viewModel: SharedViewModel
 ) {
@@ -34,7 +34,7 @@ fun HierarchyProcessScreen(
                 .systemBarsPadding()
                 .padding(16.dp)
         ) {
-            CustomHeaderHiechyProcess(navController)
+            CustomHeaderCompare(navController)
 
             Spacer(modifier = Modifier.height(24.dp))
 
@@ -148,7 +148,7 @@ fun HierarchyProcessScreen(
 
 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
-fun CustomHeaderHiechyProcess(navController: NavController) {
+fun CustomHeaderCompare(navController: NavController) {
     Row(
         modifier = Modifier.padding(vertical = 16.dp),
         verticalAlignment = Alignment.CenterVertically
@@ -157,7 +157,7 @@ fun CustomHeaderHiechyProcess(navController: NavController) {
             Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
         }
         Text(
-            text = "Hierchy Proccess",
+            text = "Compare Process",
             style = MaterialTheme.typography.titleLarge,
             modifier = Modifier.padding(vertical = 16.dp)
         )




