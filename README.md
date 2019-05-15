# Trabalho Final Sistemas Distribuidos

### Resumo
Este trabalho consiste no desenvolvimento de uma aplicação
cliente/servidor para um jogo da velha onde o clinte enfrentará
o computador.

Aqui se encontra o codigo referente ao cliente java.

### Como executar

Primeiramente deve-se incluir na configurações do projeto as 
bibliotecas _pyrolite-4.23.jar_ e _serpent-1.23.jar_ (https://search.maven.org/search?q=razorvine). 

Feita a inclusão utilize o comando "pyro4-ns -n ip_servidor", onde ip_servidor é o ip da
máquina que irá executar o servidor, para iniciar o servidor de nomes do pyro.

Após iniciar o servidor de nomes execute o arquivo _servidor.py_ 
(https://github.com/Arthur1511/Trab-Distribuidos) e execute a classe _Main.java_


### Caracteristicas
* As escolhas do computador serão feitas pelo algoritmo Min-max 
* O servidor será implementado em Python
* O cliente será implementado em Java
* Para a comunicação será utlizada a biblioteca Pyro4 (Python) e 
Pyrolite (Java)

### Referências

* https://pyro4.readthedocs.io/en/stable/index.html
* https://pythonhosted.org/Pyro4/pyrolite.html
