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

fun obtemCoordenadas(coordenadas: String): Pair<Int, Int>? {
    if (coordenadas.length != 2) return null

    val coluna = coordenadas[0].uppercaseChar()
    val linha = coordenadas[1]

    if (coluna !in 'A'..'Z') return null
    if (!linha.isDigit()) return null

    return Pair(linha.digitToInt() - 1, coluna - 'A')
}

fun validaCoordenadasDentroTerreno(pair: Pair<Int, Int>, numLinhas: Int, numColunas: Int): Boolean {
    val (linha, coluna) = pair

    if (linha < 0 || linha >= numLinhas) return false
    if (coluna < 0 || coluna >= numColunas) return false

    return true
}

fun validaMovimentoJogador(pairOrigem: Pair<Int, Int>, pairDestino: Pair<Int, Int>) : Boolean {
    return true
}

fun quadradoAVoltaDoPonto(linha: Int, coluna: Int, numLinhas: Int, numColunas: Int) : Pair<Pair<Int, Int>, Pair<Int, Int>> {
    return Pair(Pair(1,0), Pair(3,2))
}

fun contaMinasPerto(terreno: Array<Array<Pair<String, Boolean>>>, linhas: Int, coluna: Int) : Int {
    return 3
}

fun geraMatrizTerreno(numLinhas: Int,numColunas: Int,numMinas: Int) : Array<Array<Pair<String, Boolean>>>{
    val terreno = Array(numLinhas){ Array(numColunas) { Pair("",true) } }
    return terreno
}

fun preencheNumMinasNoTerreno(terreno: Array<Array<Pair<String, Boolean>>>) : Array<Array<Pair<String, Boolean>>>{
    return terreno
}

fun revelaMatriz(terreno: Array<Array<Pair<String, Boolean>>>, linha: Int, coluna: Int) : Array<Array<Pair<String, Boolean>>> {
    return terreno
}

fun celulaTemNumeroMinasVisivel(terreno: Array<Array<Pair<String, Boolean>>>, linha: Int, coluna: Int) : Boolean {
    return false
}

fun escondeMatriz(terreno: Array<Array<Pair<String, Boolean>>>): Array<Array<Pair<String, Boolean>>> {
    return terreno
}

fun criaTerreno(terreno: Array<Array<Pair<String,Boolean>>>, mostraLegenda: Boolean, mostraTudo: Boolean): String {

//    if (linhas < 1 || colunas < 1) {
//        return ""
//    }
//
//
//    var colunaAtual = 1
//    var minasColocadas = 0
//    var textLinha = " "
//
//    while (colunaAtual <= colunas) {
//        val conteudoPosicao: String
//        if (colunaAtual == 1) {
//            conteudoPosicao = "J"
//        } else if (colunaAtual == colunas) {
//            conteudoPosicao = "f"
//        } else if (minasColocadas < numMinas) {
//            minasColocadas += 1
//            conteudoPosicao = "*"
//        } else {
//            conteudoPosicao = " "
//        }
//
//        //constroi linha
//        textLinha += conteudoPosicao
//        if (colunaAtual < colunas) {
//            textLinha += " | "
//        }
//        colunaAtual += 1
//    }
//
//    return if (mostraLegenda) {
//        "    " + criaLegenda(colunas)+ "    " + "\n" + " 1 " + textLinha + "    "
//    } else {
//        "$textLinha "
//    }
    return ""
}

fun lerFicheiroJogo(nomeFicheiro: String, numLinhas: Int, numColunas: Int) : Array<Array<Pair<String, Boolean>>> {
    val terreno = Array(numLinhas){ Array(numColunas) { Pair("",true) } }
    return terreno
}
fun validaTerreno(terreno: Array<Array<Pair<String, Boolean>>>): Boolean {
    return false
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
    val invalido = "Resposta invalida."

    //Menu
    do {
        println(criaMenu())
        opcao = readln().toIntOrNull()
        when (opcao) {
            null -> println(invalido)
            !in 0..2 -> println(invalido)
            2 -> println("NAO IMPLEMENTADO")
            0 -> return
        }
    } while (opcao != 1)

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
            linha != 1 -> println(invalido)
        }
    } while (linha != 1)

    //Recebe o numero de colunas
    do {
        println("Quantas colunas?")
        coluna = readln().toIntOrNull()
        when (coluna) {
            null -> println(invalido)
            !in 1..26 -> println(invalido)
        }
    } while (coluna !in 1..26)

    //Recebe e verifica o número de linhas
    do {
        println("Quantas minas (ou enter para o valor por omissao)?")
        val resposta = readln().trim()
        mina = if (resposta.isEmpty()) {
            calculaNumeroDeMinas(linha,coluna!!)
        }
        else {
            resposta.toIntOrNull()
        }
        validaNumMina = (mina != null && validaNumeroDeMinas(linha, coluna!!, mina))
        if (!validaNumMina) {
            println(invalido)
        }
    } while (!validaNumMina)

    //Cria o terreno
    val terreno = geraMatrizTerreno(linha, coluna!!, mina!!)
    println(criaTerreno(terreno, mostraLegenda, false))
}