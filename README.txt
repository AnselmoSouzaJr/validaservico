# Valida Servico
Este é um projeto desenvolvido utilizando **Spring Boot**, que implementa um serviço para validação de JWT com regras de segurança específicas. Abaixo você encontrará informações detalhadas sobre o funcionamento do Controller, Serviço, configuração de segurança (**SecurityConfig**), como realizar o build da aplicação e como testá-la no **Postman** ou utilizando **CURL**.
## Descrição Geral
A aplicação oferece uma API que valida JWTs seguindo as especificidades configuradas. Foram implementadas regras de segurança para liberar certas rotas, de forma que não seja necessário realizar a validação completa do token ou do signature key ao tratar algumas especificidades do JWT.
## Funcionalidade e Regras Implementadas
### Controller
- O **Controller** na aplicação é responsável por receber as requisições, processar os dados de entrada e encaminhá-los para o **Service**.
- As rotas são organizadas conforme os endpoints de validação do JWT. Exemplo de rota liberada configurada na aplicação:
    - `/valida`: realiza a validação básica de um JWT.

### Service
- O **Service** contém a lógica de validação do JWT.
- Regras principais aplicadas:
    - Decodificação do JWT para extrair as informações do payload sem a necessidade de validar a **Assinatura** (_Signed Key_).
    - Verificação do formato correto do token e de campos obrigatórios no payload (como emissor, data de expiração, etc.).
    - Não realiza a validação da assinatura (`signature key`) para permitir o processamento de tokens sem possuir a chave original.

## Configuração de Segurança (SecurityConfig)
Para que determinadas rotas do serviço sejam acessadas sem autenticação, foi necessário criar a classe **SecurityConfig**.
### Principais Motivos e Configurações:
1. **Liberação de Rota**: Configuramos manualmente a liberação de rotas específicas (como `/valida`) na configuração de segurança para que não seja exigido um **JWT** nem autenticado pelo Spring Security.
2. **Flexibilidade no Tratamento do Token**: Como a aplicação não possui a chave original da assinatura JWT, optou-se por dar flexibilidade ao sistema e não validar a assinatura do token em certas operações.
    - Isso foi implementado para evitar falhas desnecessárias em uma aplicação demo ou quando não se conhece previamente a chave secreta.

Um trecho configurado no `SecurityConfig` inclui a utilização do `HttpSecurity` para liberar rotas específicas da autenticação.
## Como Fazer o Build da Aplicação
1. Certifique-se de ter o **Maven** instalado em sua máquina.
2. Navegue até o diretório raiz da aplicação.
3. Execute o comando abaixo para construir o JAR:
``` bash
   mvn clean install
```
1. O arquivo gerado estará no diretório `target` com o nome semelhante a `validaservico-0.0.1-SNAPSHOT.jar`.

### Build com Docker
1. Certifique-se de ter o Docker instalado.
2. No diretório raiz, crie a imagem Docker utilizando o Dockerfile fornecido no projeto:
``` bash
   docker build -t valida-servico .
```
1. Execute o container Docker:
``` bash
   docker run -p 8080:8080 valida-servico
```
## Como Testar a API
A API pode ser testada de duas formas: utilizando **Postman** ou **CURL**.
### Exemplos de Testes
#### 1. Com **Postman**
Abaixo seguem os passos para realizar as chamadas:
- Defina o método HTTP como **POST**.
- Defina a URL como `http://localhost:8080/valida`.
- No **body**, use o formato **JSON** e insira o token JWT:
``` json
  {
      "jwt": "seu_token_aqui"
  }
```
- Envie a requisição. O retorno será um JSON indicando a validade do token ou detalhes de erro.

#### 2. Com **CURL**
Exemplo de requisição utilizando cURL:
``` bash
curl -X POST http://localhost:8080/valida \
-H "Content-Type: application/json" \
-d '{"jwt": "seu_token_aqui"}'
```
## FAQ
### Por que foi necessário liberar a validação da assinatura do JWT?
A assinatura do JWT é gerada com uma **Key** (chave secreta ou pública). Neste cenário, a aplicação não possui acesso à chave original para realizar essa validação. Por esse motivo, foi feita a configuração para validar apenas o formato e os campos do payload do JWT, sem verificar a assinatura.
### Por que foi criada a configuração de segurança?
A configuração de segurança foi essencial para que as rotas públicas (como `/valida`) fossem acessíveis sem exigir autenticação. Isso foi realizado para facilitar o desenvolvimento e permitir maior flexibilidade para processar o JWT, mesmo que sem validação completa.
## Considerações Finais
Este projeto é voltado para demonstrações e validação básica de JWTs. Para um uso em produção, é altamente recomendável validar a assinatura do token, utilizando a chave original, para garantir a segurança e a autenticidade dos dados trocados através do sistema.
Se precisar de mais ajuda ou tiver dúvidas, sinta-se à vontade para entrar em contato!
