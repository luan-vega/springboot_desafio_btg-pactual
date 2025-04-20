# Desafio Engenheiro de software - BTG Pactual


<p align="center" width="100%">
    <img width="50%" src="https://github.com/buildrun-tech/buildrun-desafio-backend-btg-pactual/blob/main/images/btg-logo.jpg"> 
</p>

<h3 align="center">
  Desafio Backend do BTG Pactual
</h3>

<p align="center">

  <img alt="License: MIT" src="https://img.shields.io/badge/license-MIT-%2304D361">
  <img alt="Language: Java" src="https://img.shields.io/badge/language-java-green">
  <img alt="Version: 1.0" src="https://img.shields.io/badge/version-1.0-yellowgreen">

</p>

[Se inscreva em nosso canal no Youtube!](https://www.youtube.com/@buildrun-tech?sub_confirmation=1)

Para um maior entendimento do código deste repositório, [assista nosso vídeo no Youtube, clique aqui.](https://www.youtube.com/watch?v=e_WgAB0Th_I)

## Desafio
- Confira o enunciado completo, [clicando aqui](./problem.md).

## Como interagir com o banco de dados?
- Utilizamos o [MongoDB Compass](https://www.mongodb.com/products/tools/compass)

## Como interagir com a API?
- Utilizamos o [Bruno](https://github.com/usebruno/bruno)

## :rocket: Tecnologias utilizadas

* Java 21
* Spring Boot
* Spring Data MongoDB
* RabbitMQ
* Docker

:mag: Baixe o projeto e teste você mesmo na prática.

[Conheça mais sobre o nosso trabalho 😀](https://www.instagram.com/buildrun.tech/)

Developed by Build & Run



## Instruções

1. Leia esse documento com atenção antes de iniciar as atividades.
2. Você tem 1 dia, para entregar o plano de trabalho (item 1).
3. Você tem até 7 dias corridos para concluir as atividades aqui solicitadas.
   Caso não consiga concluir todas as atividades, por favor, entregue o que foi feito até a data solicitada.
4. Crie um repositório no Github para seu projeto e mantenha o seu projeto como público.
5. Ao concluir as etapas de entrega, envie um e-mail, com o assunto "[DESAFIO BTG] - SEU NOME COMPLETO", para: ****@btgpactual.com"
6. Fique à vontade para utilizar tecnologias, frameworks e técnicas não citadas nas atividades ou substituir as que julgar necessário. Informe em seu relatório as modificações e os motivos.
7. A aplicação deve ser entregue “rodando”, com instruções para interagir com ela.
8. Recomendamos a utilização do Docker (http://www.docker.com) para montagem do ambiente (MongoDb, RabbitMQ, Web Application, etc.)
   Caso opte pela utilização do Docker, crie uma única imagem com todos os containers e compartilhe em seu relatório final.

## Escopo
Processar pedidos e gerar relatório.

## Atividades
1. Elabore e entregue um plano de trabalho.
    - Crie suas atividades em tasks
    - Estime horas
2. Crie uma aplicação, na tecnologia de sua preferência (JAVA, DOTNET, NODEJS)
3. Modele e implemente uma base de dados (PostgreSQL, MySQL, MongoDB).
4. Crie um micro serviço que consuma dados de uma fila RabbitMQ e grave os dados para conseguir listar as informações:
    - Valor total do pedido
    - Quantidade de Pedidos por Cliente
    - Lista de pedidos realizados por cliente

Exemplo da mensagem que deve ser consumida:

```
   {
       "codigoPedido": 1001,
       "codigoCliente":1,
       "itens": [
           {
               "produto": "lápis",
               "quantidade": 100,
               "preco": 1.10
           },
           {
               "produto": "caderno",
               "quantidade": 10,
               "preco": 1.00
           }
       ]
   }
```


5. Crie uma API REST, em que permita o consultar as seguintes informações:
    - Valor total do pedido
    - Quantidade de Pedidos por Cliente
    - Lista de pedidos realizados por cliente


6. Relatório Técnico explicando de forma sumarizada, considerando:
    - Plano de Trabalho (previsto vs realizado)
    - Caso haja algum desvio entre o planejamento original e a execução, explique o que houve.
    - Caso o plano de trabalho foi seguido sem desvio, comente os motivos para esse resultado.
    - Tecnologias utilizadas
    - Linguagens, Versões, IDE's, SO's
    - Diagrama de arquitetura
    - Modelagem da base de dados
    - Diagrama de implantação da solução
    - Diagrama de infra com os recursos de cloud utilizados (máquina, SO, produtos específicos, etc.)
    - Evidência de Testes funcionais da aplicação
    - Publique os códigos gerados, em seu perfil do https://github.com/
    - Cite no relatório:
        - O seu perfil gitHub e a(s) URL(s) onde se encontram os códigos gerados
        - Referências utilizadas
        - Demais itens que você julgar relevante (Framework ou técnicas de testes, metodologias, etc.)
        - Se foi utilizado o Docker, para montagem do Ambiente, publique em seu perfil do http://hub.docker.com as imagens finais
        - Cite no relatório: O seu perfil dockerHub e a(s) URL(s) onde se encontram as imagens geradas