//Cria o menu
fun criaMenu(): String {
    return ("\nBem vindo ao Campo DEISIado\n\n") +
            ("1 - Novo Jogo\n") +
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

    if (numMinas <= linhas * colunas - 2 && numMinas >= 1) {
        return true
    }
    else {
        return false
    }
}
//feito
fun calculaNumeroDeMinas(linhas: Int, colunas: Int): Int {
    val espacosDisponiveis = linhas * colunas - 2

    return when {
        espacosDisponiveis == 1 -> 1
        espacosDisponiveis in 2..5 -> 2
        espacosDisponiveis in 6..10 -> 3
        espacosDisponiveis in 11..20 -> 6
        espacosDisponiveis in 21..50 -> 10
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
   return ""
}

fun main() {
//
//    var opcao : Int?
//    do {
//        println(criaMenu())
//        opcao = readln().toIntOrNull()
//        when (opcao){
//            null -> println("Resposta invalida")
//            !in 0..2 -> println("Resposta invalida")
//            1 -> {
//                println("Introduza o nome do jogador")
//                var nomeJogador = readln().trim()
//                while (!validaNome(nomeJogador, 3)) {
//                    println("Resposta invalida.")
//                    nomeJogador = readln()
//                }
//            }
//            2 -> println("NAO IMPLEMENTADO")
//            0 -> return
//        }
//    } while (opcao != 1 || opcao != 0)
}