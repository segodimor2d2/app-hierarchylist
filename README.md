# APP LISTA DE PRIORIDADES

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
