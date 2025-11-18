//Cria o menu
fun criaMenu(): String {
    return ("\nBem vindo ao Campo DEISIado\n\n") +
            ("1 - Novo Jogo\n") +
            ("0 - Sair\n")
}

//Nome com duas palavras, c
fun validaNome(nome: String, tamanhoMinimo : Int = 2): Boolean {
    return true
}
fun validaNumeroDeMinas (linhas: Int, colunas: Int, numMinas: Int): Boolean {
    if (numMinas <= linhas * colunas - 2) {
        return true
    }
    else {
        return false
    }
}
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
   return "a"
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