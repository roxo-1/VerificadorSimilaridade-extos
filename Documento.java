import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;


public class Documento {

    // === Stopwords em português ===
    private static final Set<String> STOP_WORDS = Set.of(
        "a","o","e","de","do","da","em","um","uma","que","com","para","por",
        "na","no","as","os","se","ao","à","dos","das","é","são","foi"
    );
    // Lê um arquivo e cria uma HashTable com as palavras
    public static HashTable lerArquivo(String caminho) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(caminho));
        HashTable tabela = new HashTable(1009);

        String linha;
        while ((linha = leitor.readLine()) != null) {
            String[] palavras = linha.toLowerCase()
                .replaceAll("[^a-záéíóúàâêôãõç]", " ")
                .split("\\s+");
            // for (String p : palavras) {
            //     if (!p.isEmpty()) tabela.put(p, 1);
            // }
             for (String p : palavras) {
            if (!p.isEmpty() && !STOP_WORDS.contains(p)) {
                tabela.put(p, 1); // insere palavra na tabela
            }
        }
        }
        
        
        leitor.close();
        return tabela;
    }
}
