import java.io.*;
import java.util.*;

/**
 * Representa um documento de texto (arquivo .txt)
 * Responsável por ler, limpar e inserir palavras na tabela hash.
 */
public class Documento {
    private String nome;
    private HashTable tabela;
    private static final Set<String> STOP_WORDS = Set.of(
        "a","o","e","de","do","da","em","um","uma","que","com","para","por",
        "na","no","as","os","se","ao","à","dos","das","é","são","foi"
    );

    public Documento(String caminhoArquivo) throws IOException {
        this.nome = caminhoArquivo;
        this.tabela = new HashTable(1009);
        processarArquivo(caminhoArquivo);
    }

    private void processarArquivo(String caminho) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] palavras = linha
                        .toLowerCase()
                        .replaceAll("[^a-zà-ú0-9\\s]", "") // remove pontuação
                        .split("\\s+");

                for (String p : palavras) {
                    if (!p.isEmpty() && !STOP_WORDS.contains(p)) {
                        tabela.inserir(p);
                    }
                }
            }
        }
    }

    public String getNome() {
        return nome;
    }

    public HashTable getTabela() {
        return tabela;
    }
}
