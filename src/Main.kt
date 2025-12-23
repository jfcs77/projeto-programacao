import java.io.File
import kotlin.collections.get

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

fun preencheNumMinasNoTerreno(terreno: Array<Array<Pair<String, Boolean>>>) : Array<Array<Pair<String, Boolean>>>{
    for (linha in 0..<terreno.size) {
        for (coluna in 0..<terreno[linha].size) {
            if (terreno[linha][coluna].first != "J" && terreno[linha][coluna].first != "*" && terreno[linha][coluna].first != "f") {
                val numMinas = contaMinasPerto(terreno, linha, coluna)
                if (numMinas > 0) {
                    terreno[linha][coluna] = Pair(numMinas.toString(), false)
                }
                else {
                    terreno[linha][coluna] = Pair(" ",true)
                }
            }
        }
    }
    return terreno
}

fun revelaMatriz(terreno: Array<Array<Pair<String, Boolean>>>, linha: Int, coluna: Int) : Array<Array<Pair<String, Boolean>>> {
    val (inicio, fim) = quadradoAVoltaDoPonto(linha, coluna, terreno.size, terreno[0].size)

    val (linhaInicio, colunaInicio) = inicio
    val (linhaFim, colunaFim) = fim

    // Revela o quadrado ao redor
    for (row in linhaInicio..linhaFim) {
        for (column in colunaInicio..colunaFim) {

            val numMinas = terreno[row][column].first

            if (numMinas != "*" && numMinas != "f") {
                terreno[row][column] = Pair(numMinas, true)
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
    }
    return celula.second
}

fun escondeMatriz(terreno: Array<Array<Pair<String, Boolean>>>): Array<Array<Pair<String, Boolean>>> {

    for (linha in 0..<terreno.size) {
        for (coluna in 0..<terreno[linha].size) {
            val valor = terreno[linha][coluna].first
            val bool = terreno[linha][coluna].second
            if (valor != "J" && valor != "f") {
                if (bool) {
                     terreno[linha][coluna] = Pair(valor,false)
                }
            }
        }
    }
    return terreno
}




fun criaTerreno(terreno: Array<Array<Pair<String,Boolean>>>, mostraLegenda: Boolean, mostraTudo: Boolean): String {
    if (terreno.isEmpty()) return ""
    val numLinhas = terreno.size
    val numColunas = terreno[0].size
    val numDigitosLinha = numLinhas.toString().length
    val resultado = StringBuilder()
    if (mostraLegenda) {
        repeat(4) { resultado.append(' ') }
        var c = 0
        while (c < numColunas) {
            resultado.append(('A' + c))
            if (c < numColunas - 1) resultado.append("   ")
            c++
        }
        repeat(4) { resultado.append(' ') }
        resultado.append('\n')
    }
    var linha = 0
    while (linha < numLinhas) {
        if (mostraLegenda) {
            val numeroLinha = (linha + 1).toString()
            val need = numDigitosLinha - numeroLinha.length
            resultado.append(' ')
            var p = 0
            while (p < need) {
                resultado.append(' ')
                p++
            }
            resultado.append(numeroLinha).append("  ")
        } else {
            resultado.append(' ')
        }
        var coluna = 0
        while (coluna < numColunas) {
            val celula = terreno[linha][coluna]
            val conteudo = if (mostraTudo || celula.second) celula.first else " "
            resultado.append(conteudo)
            if (coluna < numColunas - 1) {
                resultado.append(" | ")
            } else {
                // acrescenta um espaço após a última célula apenas quando há legenda
                if (mostraLegenda) resultado.append(' ')
            }
            coluna++
        }
        if (mostraLegenda) {
            repeat(3) { resultado.append(' ') }
        } else {
            resultado.append(' ')
        }
        if (mostraLegenda || linha < numLinhas - 1) {
            resultado.append('\n')
        }
        if (linha < numLinhas - 1) {
            val indent = if (mostraLegenda) numDigitosLinha + 2 else 0
            var sp = 0
            while (sp < indent) {
                resultado.append(' ')
                sp++
            }
            var cc = 0
            while (cc < numColunas) {
                resultado.append("---")
                if (cc < numColunas - 1) resultado.append("+")
                cc++
            }
            if (mostraLegenda) {
                repeat(3) { resultado.append(' ') }
            }
            resultado.append('\n')
        }
        linha++
    }
    return resultado.toString() }





fun lerFicheiroJogo(nomeFicheiro: String, numLinhas: Int, numColunas: Int): Array<Array<Pair<String, Boolean>>> {
    if (!nomeFicheiro.endsWith(".txt")) return Array(0) { Array(0) { Pair("", false) } }

    val ficheiro = File(nomeFicheiro)
    if (!ficheiro.exists()) return Array(0) { Array(0) { Pair("", false) } }

    val linhasFicheiro = ficheiro.readLines()
    if (linhasFicheiro.size != numLinhas) return Array(0) { Array(0) { Pair("", false) } }

    val matrizTerreno = Array(numLinhas) { Array(numColunas) { Pair(" ", false) } }

    var contadorJ = 0
    var contadorF = 0
    var contadorMinas = 0

    for (linha in 0 until numLinhas) {
        val linhaTexto = linhasFicheiro[linha]
        val celulasTexto = linhaTexto.split(',')
        if (celulasTexto.size != numColunas) return Array(0) { Array(0) { Pair("", false) } }

        for (coluna in 0 until numColunas) {
            var conteudoCelula = celulasTexto[coluna].trim()
            if (conteudoCelula == "") conteudoCelula = " "

            if (conteudoCelula.length != 1) return Array(0) { Array(0) { Pair("", false) } }
            val ch = conteudoCelula[0]

            when (ch) {
                'J' -> {
                    matrizTerreno[linha][coluna] = Pair("J", true)
                    contadorJ++
                }
                'f' -> {
                    matrizTerreno[linha][coluna] = Pair("f", true)
                    contadorF++
                }
                '*' -> {
                    matrizTerreno[linha][coluna] = Pair("*", false)
                    contadorMinas++
                }
                in '1'..'8' -> {
                    matrizTerreno[linha][coluna] = Pair(ch.toString(), false)
                }
                ' ' -> {
                    matrizTerreno[linha][coluna] = Pair(" ", false)
                }
                else -> return Array(0) { Array(0) { Pair("", false) } }
            }
        }
    }

    if (contadorJ != 1) return Array(0) { Array(0) { Pair("", false) } }
    if (contadorF != 1) return Array(0) { Array(0) { Pair("", false) } }

    val maxMinasPermitidas = calculaNumeroDeMinas(numLinhas, numColunas)
    if (contadorMinas < 1 || contadorMinas > maxMinasPermitidas) return Array(0) { Array(0) { Pair("", false) } }

    // valida números (cada célula '1'..'8' corresponde ao número de minas à volta)
    for (linha in 0 until numLinhas) {
        for (coluna in 0 until numColunas) {
            val valor = matrizTerreno[linha][coluna].first
            if (valor.length == 1 && valor[0] in '1'..'8') {
                val colocado = valor.toInt()
                val esperado = contaMinasPerto(matrizTerreno, linha, coluna)
                if (colocado != esperado) return Array(0) { Array(0) { Pair("", false) } }
            }
        }
    }

    return matrizTerreno
}



fun validaTerreno(terreno: Array<Array<Pair<String, Boolean>>>): Boolean {

    //Se não estiver vazia
    if (terreno.isEmpty()) {
        return false
    }

    val numLinhas = terreno.size
    val numColunas = terreno[0].size

    for (linha in terreno) {
        if (linha.size != numColunas) return false
    }
    var numJ = 0
    var numf = 0
    var numMinas = 0

    for (linha in 0..<numLinhas) {
        for (coluna in 0..<numColunas) {

            val parte1 = terreno[linha][coluna].first

            if (parte1 !in arrayOf("J","f","*"," ","1","2","3","4","5","6","7","8")) return false

            when (parte1) {
                "J" -> numJ ++
                "f" -> numf ++
                "*" -> numMinas++
            }
        }
    }
    if (numJ != 1) return false
    if (numf != 1) return false
    if (numMinas !in 1..(calculaNumeroDeMinas(numLinhas, numColunas))) return false

    //Valida num de minas ao redor da célula
    for (linha in 0..<numLinhas) {
        for (coluna in 0..<numColunas) {

            val num = terreno[linha][coluna].first

            if (num in "12345678") {
                val numColocado = num.toInt()
                val numEsperado = contaMinasPerto(terreno,linha,coluna)

                if (numColocado != numEsperado) return false
            }
        }
    }

    return true
}

fun main() {
    var opcao: Int?
    var nomeJogador: String
    var legenda : String
    var mostraLegenda: Boolean
    var linha: Int?
    var coluna: Int?
    var mina: Int?
    var validaNumMina : Boolean
    var nomeFicheiro : String
    val invalido = "Resposta invalida."

    //Menu
    do {
        println(criaMenu())
        opcao = readln().toIntOrNull()
        when (opcao) {
            null -> println(invalido)
            !in 0..2 -> println(invalido)
            0 -> return
        }
    } while (opcao != 1 && opcao != 2)

    //Nome do Jogador
    do {
        println("Introduz o nome do jogador")
        nomeJogador = readln()
        if (!validaNome(nomeJogador)) {
            println(invalido)
        }
    } while (!validaNome(nomeJogador))

    //Verifica se mostra legenda
    do {
        println("Mostrar legenda (s/n)?")
        legenda = readln().trim().lowercase()
        mostraLegenda = legenda == "s"
        if (legenda != "s" && legenda != "n") {
            println(invalido)
        }
    } while (legenda != "s" && legenda != "n")

    //Recebe o numero de linhas
    do {
        println("Quantas linhas?")
        linha = readln().toIntOrNull()
        when {
            linha == null -> println(invalido)
            linha < 1 -> println(invalido)
        }
    } while (linha == null || linha < 1)

    //Recebe o numero de colunas
    do {
        println("Quantas colunas?")
        coluna = readln().toIntOrNull()
        when (coluna) {
            null -> println(invalido)
            !in 1..26 -> println(invalido)
        }
    } while (coluna == null || coluna !in 1..26)
    //Recebe um ficheiro e cria o terren
    if (opcao == 2) {
        var terrenoValido: Boolean
        do {
            do {
                println("Qual o ficheiro de jogo a carregar?")
                nomeFicheiro = readln().trim()
                if (!nomeFicheiro.endsWith(".txt")) {
                    println("Ficheiro invalido")
                }
            } while (!nomeFicheiro.endsWith(".txt"))

            val terreno = lerFicheiroJogo(nomeFicheiro, linha, coluna)
            terrenoValido = validaTerreno(terreno)
            if (!terrenoValido) {
                println("Ficheiro invalido")
            }
        } while (!terrenoValido)
    }
    //Recebe, verifica o número de minas e cria o terreno
    if (opcao == 1){
        do {
            println("Quantas minas (ou enter para o valor por omissao)?")
            val resposta = readln().trim()
            mina = if (resposta.isEmpty()) {
                calculaNumeroDeMinas(linha, coluna)
            } else {
                resposta.toIntOrNull()
            }
            validaNumMina = (mina != null && validaNumeroDeMinas(linha, coluna, mina))
            if (!validaNumMina) {
                println(invalido)
            }
        } while (mina == null || !validaNumMina)


        //Cria o terreno
        val terreno = geraMatrizTerreno(linha, coluna, mina)
        println(criaTerreno(terreno,mostraLegenda,true))
    }
}