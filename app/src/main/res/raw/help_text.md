# Estabilidade-IO v0.1.0

## Apresentação do acesso antecipado
Olá! E obrigado por testar a versão `0.1.0` do Estabilidade-IO, um projeto Livre e de
Código Aberto feito principalmente por [Ícaro Cortês](https://github.com/icarob-eng).

Tenha em mente que o principal objetivo dessa versão é permitir um aceso antecipado ao programa,
nada aqui pode ser considerado parte do produto final, muitas coisas ainda vão mudar e muitos erros
serão econtrados (incluindo os já identidicados abaixo), só estou publicando esta versão para fins
de testes com o público. Enfatizo: **o program ainda é instável e seus resultados devem ser
verificados sempre que possível**. Além disso, como sou um desenvolvedor solo, novas versões devem
demorar a serem publicadas.

Dessa forma, se por algum motivo encontrar algum bug ou algo que acredita que está errado, por favor
relate o problema, enviando contexto e capturas de tela aplicáveis, numa issue no repositório Github
do projeto: [github.com/icarob-eng/Estabilidade-IO](https://github.com/icarob-eng/Estabilidade-IO),
ou, se preferir, via e-mail para `icaro.bruno@escolar.ifrn.edu.br`. Estas são as informações de
contato do desenvolvedor.

Mais uma vez obrigado por tetsar a versão alpha e vamos aos negócios.

### Limitações e erros diagnosticados
Os itens aqui listados já foram identificados e serão resolvidos assim que possível (exceto se
indicado contrário).
- A única forma de entrar com dados é editando um yaml, o que será explciado abaixo;
- No momento o sistema só considera vigas de Euler-Bernoulli simples, com um apoio de primerio e
outro de segundo gênero, ou um único apoio de terceiro gênero;
- Rótulas e qualquer outra forma de ligar duas vigas ou partir uma, ainda não foram implementadas, — 
de fato, fiz com que o programa só aceite uma única barra, para evitar comportamento inesperado;
- Apesar da estrutura de código bem evidente, o programa ainda não salva nem carrega dados da
memória do aparelho, de modo que qualquer forma de salvamento deve ser feito copiando e colando o
texto;
- O aplicativo tem uma paleta de cores estranha que não se comporta bem com as mudanças de modo
noturno e diurno;

Mais problemas do que os que estão aí eu provavelmente não estou ciente e gostaria de ser informado!

## Insersação de dados de estrutura
O aplicativo tem como método de entrada de dados uma notação bem popular chamado YAML (sigla para
YAML Ain't a Markup Language — sim, uma sigla recursiva), para isso, basta escrever seções com as
palavras-chave indicadas abaixo e dentro das seções, se usa um espaçamento duplo (dois espaços) para
adicionar elementos pertecentes.

> Letras maiúsculas *provavelmente* podem ser ignoradas, mas **os acentos são necessários**.

> O `#` indica comentário, ou seja, o que estiver a direita dele, na mesma linha, será ignorado. Só
estou usando isso para facilitar a compreensão mesmo. 
```yaml
estrutura: Estrutura exemplo 
# nome da estrutura (limitado pela linha)
nós:  # seção de nós
  A: [0, 0]  # nome do nó: posição
  B:
    x: 1  # esta notação também é válida
    y: 0
  C:
    - 2  # esta também
    - 0
# as seções podem ou não serem
#  separadas por quantos espaços quiser
apoios:
  A:  # nome do apoio que será aplicado
    gênero: 1  # apoio de primeiro gênero
    direção: vertical
  C:
    gênero: 2
    direção: [0, 1]
    # outra forma de indicar direção vertical
    
barras:
- [A, C]  # barra entre A e C
# esta notação é importante
cargas:
  F1: # esse F1 não significa absolutamente nada
    nó: B
    direção: vertical
    módulo: -20
  F2:
    nó: [B, C]  # dois nós = carga distribuída
    vetor: [3, 4]  # carga distirbuída
  F3:
    nó: B
    módulo: 10  # módulo
```
Lembre-se de que:
- Um nó deve ter um nome único que deve começar com uma letra;
- Se um nome de nó se repetir, só será considerado o último;
- A estrutura precisa ser isoestática;
- Uma barra só pode ser aplicada entre apenas dois nós;
- Para cargas, a notação de `vetor` ou a notação `módulo` + `direção` são válidas;
- As palavras `vertical` e `horizontal` substituem vetores;

### Notações de vetor válidas
As notações válidas de vetor são:
```yaml
vetor1: [3, 4]

vetor2:
  x: 3
  y: 4

vetor3:
  - 3
  - 4
  
vetor4: vertical

vetor5: horizontal
```
### Especificações de tipos aceitos
```yaml
estrutura: Só aceita texto

nós:
  nome_do_nó: vetor  # posição do nó

apoios:
  gênero: número
  direção: vetor
  # o módulo do vetor é desconsiderado, aqui

barras:
  - [nome_do_nó, outro_nó]

cargas:
  carga_pontual1:
    nó: nome_do_nó
    vetor: vetor
  carga_pontual2:
    nó: nome_do_nó
    módulo: número
    direção: vetor
  # o vetor é normalizado e multiplicado
  #  pelo módulo
  carga_distribuída1:
    nó: [nome_do_nó, outro_nó]
    vetor: vetor
  carga_distribuída2:
    nó: [nome_do_nó, outro_nó]
    módulo: número
    direção: vetor
  momento_fletor:
    nó: nome_do_nó
    módulo: intensidade_do_momento
```

## Exibição e tipos de diagrama
Todos os botões possuem dica de ferramenta quando pressionados por um longo período, mas aqui vai
uma explicação do que eles fazem:

### Mostrar estrutura
Este botão faz com que o aplicativo tente compilar os dados inseridos e mostrar a estrutura. Se
algum erro de digitação for feito (erros de sintaxe YAML), o aplicativo deve mostrar uma mensagem
apontando a linha do problema; se o programa não conseguir estabilizar a estrutura ou encontrar
algum outro erro, como campos faltando, também será indicado; da mesma forma, se algo
inesperado acontecer, deve aparecer uma mensagem explicando.

Isto pode ser uma boa ferramenta para identificar bugs.

### Tipos de diagrama
O aplicativo possui seis formas de vizualização, sendo elas:
- **Estrutrua**, onde se mostra apenas os apoios, barras e nós;
- **Cargas** aplicadas, sem mostrar as forças de reação;
- **Reações**, que também exibe as cargas, bem como o resto da estrutura;
- **DEN**: Diagrama de Esforço Normal;
- **DEC**: Diagrama de Esforço Cortante;
- **DMF**: Diagrama de Momento Fletor;

Quando clicado nas últimas 3 opções, o sistema tenta cálcular os pontos dos gráficos, o que pode
gerar erros (não sei como).

Nas vizualizações de diagramas, o programa também dá o polinômio associado a cada seção da
estrutura.

## Conclusão
Espero que faça proveito do aplicativo ao qual investi tanto tempo! Em caso de dúvidas, comentários,
requisições ou problemas, entre em contato pelos meios especificados acima.

> Vejo você na próxima versão!
> 
> — Ícaro.