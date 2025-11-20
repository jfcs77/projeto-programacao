//Cria o menu
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
//feito
fun validaNumeroDeMinas (linhas: Int, colunas: Int, numMinas: Int): Boolean {

    if (linhas <= 0 || colunas <= 0) {
        return false
    }

    return if (numMinas <= linhas * colunas - 2 && numMinas >= 1) {
        true
    }
    else {
        false
    }
}
//feito
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
//feito
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
fun criaTerreno(linhas: Int, colunas: Int, numMinas: Int, mostraLegenda: Boolean): String {
    var terreno = ""
    return terreno
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
    println(criaTerreno(linha, coluna!!, mina!!, mostraLegenda))
}