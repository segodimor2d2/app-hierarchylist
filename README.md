# App Método de Borda Modificado

## Tela 1 Home: 
1. Botão Procurar Selecionar a pasta de trabalho
    Ver a possibilidade de Salvar a pasta depois da primeira vez
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


## Tela 3 HierchyProccess:
1. Mostra duas colunas
    ColumA | ColumB
     ItemA | ItemB

2. comparação de itens com 3 botões:
    `[A-B] [A=B] [A+B]`

    - -1(A mais importante que B),
    -  0(A e B são iguais),
    -  1(A é mais importante que B)

3. Botões de para navegar entre Items
    seguinte Item >>
    anterior Item <<

4. Botão Finalizar => 
    salva a lista ordenada com prefix identificando no nome do arquivo

5. Text Feedback da opção selecionada
    `[ - ] ó [A-B] ó [A=B] ó [A+B]`

## Tela 4 Hierchy:
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


