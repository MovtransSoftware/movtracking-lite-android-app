<div style="display: flex; align-items: center;">
<img src="app\src\main\res\mipmap-xxxhdpi\ic_launcher_foreground.webp" width="60" height="60">

# Movtracking Lite
</div>

**Movtracking Lite** é um aplicativo Android nativo desenvolvido em Kotlin, projetado especificamente para profissionais de logística. O foco principal é a **performance extrema** em dispositivos de entrada (com 1GB de RAM), garantindo agilidade no registro de entregas e ocorrências em campo.

## 🚀 Funcionalidades

* **Romaneio Digital:** Lista dinâmica de entregas pendentes com status visual intuitivo.
* **Gestão de Status:** Diferenciação visual dinâmica
* **Confirmação de Entrega:** Registro de nome de recebedor, documento, data/hora automática e captura de foto do comprovante.
* **Registro de Ocorrências:** Menu completo para reportar problemas (Cliente Ausente, Recusado, etc.) com evidência fotográfica.
* **Segurança de Permissões:** Sistema inteligente de solicitação de permissões (Câmera/Telefone) que orienta o usuário em caso de negação.
* **UI/UX Otimizada:** Interface limpa, sem cabeçalhos desnecessários, focada na leitura rápida sob luz solar.

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** [Kotlin](https://kotlinlang.org/)
* **Arquitetura:** Android Nativo (Views)
* **UI:** XML Otimizado com Drawables leves (Shapes)
* **Persistência de Dados:** Modelos Serializáveis para tráfego entre telas.
* **Hardware:** Integração com Câmera e Telefone via Intents seguras.

## ⚙️ Instalação e Execução

1. Clone este repositório:
```bash
git clone https://github.com/MovtransSoftware/movtracking-lite-android-app
```
2. Abra o projeto no **Android Studio**.
3. Certifique-se de ter o SDK do Android instalado (API 21 ou superior).
4. Sincronize o Gradle (`Build > Rebuild Project`).
5. Execute no emulador ou dispositivo físico.

## 🛡️ Gestão de Permissões

O projeto utiliza um `CameraHelper` personalizado que garante que:

1. O acesso à câmera seja solicitado apenas sob demanda.
2. Caso o usuário negue, uma explicação clara seja exibida via `AlertDialog`.
3. Caso o acesso seja bloqueado permanentemente, o app oferece um atalho direto para as configurações do sistema.
