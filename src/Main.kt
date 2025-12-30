import java.io.File

fun criaMenu(): String {
    return ("\nBem vindo ao Campo DEISIado\n\n") +
            ("1 - Novo Jogo\n") +
            ("2 - Ler Jogo\n") +
            ("0 - Sair\n")
}

fun validaNome(nome: String, tamanhoMinimo: Int = 3): Boolean {
    val texto = nome.trim()

    if (texto.length < tamanhoMinimo){
        return false
    }

    var indice = 0
    var nome1 = ""
    var nome2 = ""

    //fazer nome 1
    while (indice < texto.length && texto[indice] != ' ') {
        nome1 += texto[indice]
        indice++
    }

    // se não encontrou espaço
    if (indice == texto.length) {
        return false
    }

    //verifica se tem varios
    while (indice < texto.length && texto[indice] == ' ') {
        indice++
    }

    if (indice == texto.length){
        return false
    }

    //fazer nome 2
    while (indice < texto.length && texto[indice] != ' ') {
        nome2 += texto[indice]
        indice++
    }

    //verifica se tem 3 caracteres
    if (nome1.length < tamanhoMinimo) return false
    if (nome2.length < tamanhoMinimo) return false

    // verificar maiúsculas
    if (!nome1[0].isUpperCase()) return false
    if (!nome2[0].isUpperCase()) return false

    return true
}

fun validaNumeroDeMinas (linhas: Int, colunas: Int, numMinas: Int): Boolean {

    if (linhas <= 0 || colunas <= 0) {
        return false
    }

    return numMinas <= linhas * colunas - 2 && numMinas >= 1
}

fun calculaNumeroDeMinas(linhas: Int, colunas: Int): Int {
    val espacosDisponiveis = linhas * colunas - 2

    return when (espacosDisponiveis) {
        1 -> 1
        in 2..5 -> 2
        in 6..10 -> 3
        in 11..20 -> 6
        in 21..50 -> 10
        else -> 15
    }
}

fun criaLegenda(colunas: Int): String {
    var count = 0
    var legenda= ""
    while (count < colunas) {
        legenda += "${'A' + count}"
        if (count < colunas - 1) {
            legenda += "   "
        }
        count ++
    }
    return legenda
}

fun obtemCoordenadas(coordenadas: String?): Pair<Int, Int>? {
    when {
        coordenadas == null -> return null
        coordenadas.length != 2 -> return null
    }

    val linha = coordenadas[0]
    val coluna = coordenadas[1]


    if ((coluna !in 'A'..'Z' && coluna !in 'a'..'z') || !linha.isDigit()) return null

    val letraColuna = if (coluna in 'A'..'Z') {
        coluna - 'A'
    } else {
        coluna - 'a'
    }

    return Pair(linha.digitToInt() - 1, letraColuna)
}

fun validaCoordenadasDentroTerreno(pair: Pair<Int, Int>, numLinhas: Int, numColunas: Int): Boolean {
    val (linha, coluna) = pair

    return !(linha !in 0..<numLinhas || coluna !in 0..<numColunas)
}

fun validaMovimentoJogador(pairOrigem: Pair<Int, Int>, pairDestino: Pair<Int, Int>) : Boolean {
    var linha = pairDestino.first - pairOrigem.first
    var coluna = pairDestino.second - pairOrigem.second

    if (linha < 0) {
        linha *= -1
    }
    if (coluna < 0) {
        coluna *= -1
    }
    return (linha <= 2 && coluna <= 2)
}

fun quadradoAVoltaDoPonto(linha: Int, coluna: Int, numLinhas: Int, numColunas: Int) : Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val linhaInicio = if (linha - 1 >= 0) {
        linha - 1
    } else {
        0
    }

    val colunaInicio = if (coluna - 1 >= 0) {
        coluna - 1
    } else{
        0
    }

    val linhaFim = if (linha + 1 <= numLinhas - 1){
        linha + 1
    } else{
        numLinhas - 1
    }

    val colunaFim = if (coluna + 1 <= numColunas - 1) {
        coluna + 1
    } else {
        numColunas - 1
    }

    return Pair(Pair(linhaInicio, colunaInicio), Pair(linhaFim, colunaFim))
}

fun contaMinasPerto(terreno: Array<Array<Pair<String, Boolean>>>, linha: Int, coluna: Int) : Int {

    val limites = quadradoAVoltaDoPonto(linha, coluna, terreno.size, terreno[0].size)
    val cantoSuperiorEsquerdo = limites.first
    val cantoInferiorDireito = limites.second

    val linhaInicio = cantoSuperiorEsquerdo.first
    val colunaInicio = cantoSuperiorEsquerdo.second
    val linhaFim = cantoInferiorDireito.first
    val colunaFim = cantoInferiorDireito.second

    var contadorMinas = 0

    var linhaAtual = linhaInicio
    while (linhaAtual <= linhaFim) {
        var colunaAtual = colunaInicio
        while (colunaAtual <= colunaFim) {
            // não contar a própria célula
            if (!(linhaAtual == linha && colunaAtual == coluna)) {
                val celula = terreno[linhaAtual][colunaAtual]
                val conteudo = celula.first
                if (conteudo == "*") {
                    contadorMinas += 1
                }
            }
            colunaAtual += 1
        }
        linhaAtual += 1
    }

    return contadorMinas
}

fun geraMatrizTerreno(numLinhas: Int,numColunas: Int,numMinas: Int) : Array<Array<Pair<String, Boolean>>>{
    // se dimensoes invalidas retorna uma matriz vazia
    if (numLinhas <= 0 || numColunas <= 0) {
        return Array(0) { Array(0) { Pair("", false) } }
    }


    val terreno = Array(numLinhas) { Array(numColunas) { Pair(" ", false) } }


    terreno[0][0] = Pair("J", true)


    terreno[numLinhas - 1][numColunas - 1] = Pair("f", true)


    var minasColocadas = 0


    val maxDisponiveis = numLinhas * numColunas - 2 // retira J e f
    val minasAlvo = if (numMinas > maxDisponiveis) {
        maxDisponiveis
    } else {
        numMinas
    }

    // loop até colocarmos todas as minas
    while (minasColocadas < minasAlvo) {

        val linhaRand = (0 until numLinhas).random()
        val colunaRand = (0 until numColunas).random()


        if ((linhaRand == 0 && colunaRand == 0) || (linhaRand == numLinhas - 1 && colunaRand == numColunas - 1)) {

        } else {
            val conteudoAtual = terreno[linhaRand][colunaRand].first

            if (conteudoAtual != "*") {
                terreno[linhaRand][colunaRand] = Pair("*", false)
                minasColocadas += 1
            }
        }
    }

    return terreno
}

fun preencheNumMinasNoTerreno(terreno: Array<Array<Pair<String, Boolean>>>): Array<Array<Pair<String, Boolean>>> {
    for (linha in 0..<terreno.size) {
        for (coluna in 0..<terreno[linha].size) {
            val celula = terreno[linha][coluna].first
            if (celula != "J" && celula != "*" && celula != "f") {
                val numMinas = contaMinasPerto(terreno, linha, coluna)
                terreno[linha][coluna] = if (numMinas > 0) {
                    Pair(numMinas.toString(), false)
                } else {
                    Pair(" ", true)
                }
            }
        }
    }
    return terreno
}


fun revelaMatriz(terreno: Array<Array<Pair<String, Boolean>>>, linha: Int, coluna: Int): Array<Array<Pair<String, Boolean>>> {
    val (inicio, fim) = quadradoAVoltaDoPonto(linha, coluna, terreno.size, terreno[0].size)
    val (linhaInicio, colunaInicio) = inicio
    val (linhaFim, colunaFim) = fim

    for (row in linhaInicio..linhaFim) {
        for (col in colunaInicio..colunaFim) {
            val celula = terreno[row][col]
            // só revela se não for mina ou final
            if (celula.first != "*" && celula.first != "f") {
                terreno[row][col] = Pair(celula.first, true)
            }
        }
    }

    return terreno
}

fun celulaTemNumeroMinasVisivel(terreno: Array<Array<Pair<String, Boolean>>>, linha: Int, coluna: Int) : Boolean {
    val celula = terreno[linha][coluna]
    when (celula.first) {
        "J" -> return false
        "*" -> return false
        "f" -> return false
        " " -> return false
    }
    return celula.second
}

fun escondeMatriz(terreno: Array<Array<Pair<String, Boolean>>>): Array<Array<Pair<String, Boolean>>> {

    for (linha in 0..< terreno.size) {
        for (coluna in 0..<terreno[linha].size) {
            val valor = terreno[linha][coluna].first

            // esconder tudo exceto J (ou P) e f
            if (valor != "J" && valor != "f") {
                terreno[linha][coluna] = Pair(valor, false)
            }
        }
    }
    return terreno
}

fun criaTerreno(terreno: Array<Array<Pair<String, Boolean>>>, mostraLegenda: Boolean, mostraTudo: Boolean): String {
    if (terreno.isEmpty()) return ""

    val numeroLinhas = terreno.size
    val numeroColunas = terreno[0].size
    var resultado = ""

    // legenda no inicio
    if (mostraLegenda) {
        resultado += "    " + criaLegenda(numeroColunas) + "    \n"
    }

    for (linha in 0..<numeroLinhas) {
        // início da linha: espaço se sem legenda, número da linha se legenda
        if (mostraLegenda) {
            resultado += " " + (linha + 1) + "  "
        } else {
            resultado += " " // espaço no início
        }

        // células da linha
        for (coluna in 0..<numeroColunas) {
            val celula = terreno[linha][coluna]
            val conteudo = if (mostraTudo || celula.second) celula.first else " "
            resultado += conteudo
            if (coluna < numeroColunas - 1) resultado += " | "
        }

        resultado += " " // espaço no fim da linha

        if (linha < numeroLinhas - 1) {
            if (mostraLegenda) resultado += "   \n" else resultado += "\n"
            if (mostraLegenda) resultado += "   "
            for (coluna in 0..<numeroColunas) {
                resultado += "---"
                if (coluna < numeroColunas - 1) resultado += "+"
            }
            if (mostraLegenda) resultado += "   "
        }

        // nova linha, só entre linhas
        if (linha < numeroLinhas - 1) resultado += "\n"

        if (linha == numeroLinhas - 1 && mostraLegenda) resultado += "   \n"
    }

    return resultado
}


fun lerFicheiroJogo(nomeFicheiro: String, numLinhas: Int, numColunas: Int): Array<Array<Pair<String, Boolean>>> {
    val ficheiro = File(nomeFicheiro)

    val linhasFicheiro = ficheiro.readLines()
    if (linhasFicheiro.size != numLinhas) {
        return Array(0) { Array(0) { Pair("", false) } }
    }

    val matrizTerreno = Array(numLinhas) { Array(numColunas) { Pair(" ", false) } }

    for (linha in 0..<numLinhas) {
        val linhaTexto = linhasFicheiro[linha]
        val partes = linhaTexto.split(',')
        if (partes.size != numColunas){
            return Array(0) { Array(0) { Pair("", false) } }
        }

        for (coluna in 0..<numColunas) {
            val simbolo = partes[coluna]

            matrizTerreno[linha][coluna] = when (simbolo) {
                "J" -> Pair("J", true)
                "f" -> Pair("f", true)
                "*" -> Pair("*", false)
                " " -> Pair(" ", false)
                ""  -> Pair(" ", false)
                else -> Pair(" ", false)
            }
        }
    }

    return matrizTerreno
}

fun validaTerreno(terreno: Array<Array<Pair<String, Boolean>>>): Boolean {
    if (terreno.isEmpty()) return false
    if (terreno[0].isEmpty()) return false

    val numLinhas = terreno.size
    val numColunas = terreno[0].size

    for (linha in terreno) {
        if (linha.size != numColunas) return false
    }

    var contaJ = 0
    var contaF = 0

    for (linha in 0..<numLinhas) {
        for (coluna in 0..<numColunas) {

            val valor = terreno[linha][coluna].first

            if (valor == "J") {
                contaJ++
                if (linha != 0 || coluna != 0) return false
            } else if (valor == "f") {
                contaF++
                if (linha != numLinhas - 1 || coluna != numColunas - 1) return false
            } else if (valor == "*" || valor == " ") {

            } else if (valor.length == 1 && valor[0].isDigit()) {

            } else {
                return false
            }
        }
    }

    if (contaJ != 1) return false
    if (contaF != 1) return false

    return true
}

fun msgRespostaInvalida() : String { return "Resposta invalida." }

//Menu
fun opcaoMenu(): Int {
    var opcao: Int?
    do {
        println(criaMenu())
        opcao = readln().toIntOrNull()
        if (opcao == null || opcao !in 0..2) {
            println(msgRespostaInvalida())
        }
    } while (opcao == null || opcao !in 0..2)

    return opcao
}

//Nome do jogador
fun nomeJogador(): String {
    var nome: String

    do {
        println("Introduz o nome do jogador")
        nome = readln()
        if (!validaNome(nome)) {
            println(msgRespostaInvalida())
        }
    } while (!validaNome(nome))

    return nome
}

//Mostrar legenda
fun legenda(): Boolean {
    var resposta: String

    do {
        println("Mostrar legenda (s/n)?")
        resposta = readln().trim().lowercase()
        if (resposta != "s" && resposta != "n") {
            println(msgRespostaInvalida())
        }
    } while (resposta != "s" && resposta != "n")

    return resposta == "s"
}

//Pede linha ou coluna
fun pedeNumero(pergunta: String, min: Int, max: Int? = null): Int {
    var valor: Int?

    do {
        println(pergunta)
        valor = readln().toIntOrNull()

        if (valor == null || valor < min || (max != null && valor > max)) {
            println(msgRespostaInvalida())
            valor = null
        }

    } while (valor == null)

    return valor
}

//Numero de Minas
fun pedeNumeroDeMinas(linhas: Int, colunas: Int): Int {
    var minas: Int?

    do {
        println("Quantas minas (ou enter para o valor por omissao)?")
        val resposta = readln().trim()

        minas = if (resposta.isEmpty()) {
            calculaNumeroDeMinas(linhas, colunas)
        } else {
            resposta.toIntOrNull()
        }

        if (minas == null || !validaNumeroDeMinas(linhas, colunas, minas)) {
            println (msgRespostaInvalida())
            minas = null
        }

    } while (minas == null)

    return minas
}

//Terreno
fun terrenoDeFicheiro(linhas: Int, colunas: Int): Array<Array<Pair<String, Boolean>>> {
    var nomeFicheiro: String
    var terrenoValido: Boolean
    var terreno: Array<Array<Pair<String, Boolean>>>

    do {
        do {
            println("Qual o ficheiro de jogo a carregar?")
            nomeFicheiro = readln().trim()
            val partes = nomeFicheiro.split('.')
            if (partes.size < 2 || partes.last() != "txt") {
                println("Ficheiro invalido")
            }
        } while (partes.size < 2 || partes.last() != "txt")

        terreno = lerFicheiroJogo(nomeFicheiro, linhas, colunas)
        terrenoValido = validaTerreno(terreno)

        if (!terrenoValido) {
            println("Ficheiro invalido")
        }

    } while (!terrenoValido)

    return terreno
}

fun eNumero(celula: String): Boolean {
    return celula.length == 1 && celula[0].isDigit()
}

fun executaJogada(terreno: Array<Array<Pair<String, Boolean>>>, posicaoJogador: Pair<Int, Int>, coords: Pair<Int, Int>):
        Pair<Array<Array<Pair<String, Boolean>>>, Pair<Int, Int>> {

    // saber onde o jogador ESTAVA
    val estavaNumNumeroVisivel =
        eNumero(terreno[posicaoJogador.first][posicaoJogador.second].first)

    // guardar conteúdo original do destino
    val conteudoDestino = terreno[coords.first][coords.second].first

    // mover jogador
    terreno[posicaoJogador.first][posicaoJogador.second] = Pair(" ", false)
        terreno[coords.first][coords.second] = Pair("J", true)

    // entrou num número → esconder tudo
    if (eNumero(conteudoDestino)) {
        escondeMatriz(terreno)
    }
    // saiu de um número → revelar corretamente
    else if (estavaNumNumeroVisivel) {

        // revela tudo (comportamento obrigatório da função dada)
        revelaMatriz(terreno, coords.first, coords.second)

        // esconde tudo
        escondeMatriz(terreno)

        // mostra apenas números à volta da NOVA posição
        for (linha in coords.first - 1..coords.first + 1) {
            for (coluna in coords.second - 1..coords.second + 1) {
                if (linha in 0..<terreno.size && coluna in 0..<terreno[linha].size) {
                    val valorCelula = terreno[linha][coluna].first
                    if (eNumero(valorCelula)) {
                        terreno[linha][coluna] = Pair(valorCelula, true)
                    }
                }
            }
        }
    }

    return Pair(terreno, coords)
}


fun main() {
    do {
        val opcao = opcaoMenu()
        if (opcao == 0) return
        val nomeJogador = nomeJogador()
        val mostraLegenda = legenda()
        val linhas = pedeNumero("Quantas linhas?", 1)
        val colunas = pedeNumero("Quantas colunas?", 1, 26)
        var terreno = if (opcao == 2) {
            val terrenoComMinas = terrenoDeFicheiro(linhas, colunas)
            preencheNumMinasNoTerreno(terrenoComMinas)
        } else {
            val minas = pedeNumeroDeMinas(linhas, colunas)
            preencheNumMinasNoTerreno(geraMatrizTerreno(linhas, colunas, minas))
        }

        //Mostra o terreno
        terreno = revelaMatriz(terreno, 0, 0)
        println(criaTerreno(terreno, mostraLegenda, false))

        //Inicio do jogo
        var play = true
        var posicaoJogador = Pair(0, 0)
        val movInvalido = "Movimento invalido."
        do {
            println("Introduz a celula destino (ex: 2D)")
            val celula = readln().trim()
            when (celula) {
                "sair" -> return
                "abracadabra" -> println(criaTerreno(terreno, mostraLegenda, true))
                else -> {
                    val coords = obtemCoordenadas(celula)
                    if (coords == null || !validaCoordenadasDentroTerreno(
                            coords,
                            linhas,
                            colunas
                        ) || !validaMovimentoJogador(posicaoJogador, coords)
                    ) {
                        println(movInvalido)
                        println(criaTerreno(terreno, mostraLegenda, false))
                    } else {
                        val resultado = executaJogada(terreno, posicaoJogador, coords)
                        terreno = resultado.first
                        posicaoJogador = resultado.second

                        println(criaTerreno(terreno, mostraLegenda, false))

                        val conteudoDestino = terreno[posicaoJogador.first][posicaoJogador.second].first
                        if (conteudoDestino == "f") {
                            println("Ganhou o jogo!")
                            play = false
                        }
                        if (conteudoDestino == "*") {
                            println("Perdeste o jogo!")
                            play = false
                        }

                    }
                }
            }
        } while (play)
    } while (true)
}