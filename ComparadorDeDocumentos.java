import java.io.*;
import java.util.Set;

public class ComparadorDeDocumentos {

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

    // Calcula o índice de Jaccard entre dois textos
    public static double jaccard(HashTable t1, HashTable t2) {
        String[] chaves1 = t1.getKeys();
        String[] chaves2 = t2.getKeys();

        int intersecao = 0;
        for (String palavra : chaves1) {
            if (t2.contains(palavra)) {
                intersecao++;
            }
        }

        HashTable uniaoTable = new HashTable(2003);
        for (String palavra : chaves1) uniaoTable.put(palavra, 1);
        for (String palavra : chaves2) uniaoTable.put(palavra, 1);
        int uniao = uniaoTable.getKeys().length;

        if (uniao == 0) return 0.0;
        return (double) intersecao / uniao;
    }

    // Lista todos os arquivos .txt em um diretório
    public static File[] listarArquivos(String diretorio) {
        File pasta = new File(diretorio);
        return pasta.listFiles((dir, nome) -> nome.toLowerCase().endsWith(".txt"));
    }
}
