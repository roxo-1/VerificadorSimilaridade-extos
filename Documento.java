import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class Documento {

    // === Stopwords em português ===
    private static final Set<String> STOP_WORDS = Set.of(
        "a","o","e","de","do","da","em","um","uma","que","com","para","por",
        "na","no","as","os","se","ao","à","dos","das","é","são","foi"
    );

    public static void main(String[] args) {
        // === Texto de exemplo (pode ser substituído por qualquer entrada) ===
        String texto = "ChatGPT é muito útil. ChatGPT ajuda a aprender Java e estruturas de dados. "
                     + "Java é uma linguagem poderosa e útil.";

        // Cria a tabela hash
        HashTable tabela = new HashTable(50);

        // === Pré-processamento ===
        String[] palavras = texto
                .toLowerCase()
                .replaceAll("[^a-zà-ú0-9 ]", "") // remove pontuação
                .split("\\s+"); // separa por espaços

        for (String p : palavras) {
            if (!p.isEmpty() && !STOP_WORDS.contains(p)) {
                tabela.put(p, 1); // insere palavra na tabela
            }
        }

        // === Escrever as palavras e frequências no arquivo ===
        try (FileWriter writer = new FileWriter("documento.txt")) {
            String[] chaves = tabela.getKeys();
            
            for (String chave : chaves) {
                int freq = tabela.getString(chave);
                writer.write("(" + chave + ", " + freq + ")\n");
            }

            System.out.println("Arquivo 'documento.txt' criado com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo: " + e.getMessage());
        }
    }
}
