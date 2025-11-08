import java.io.*;

public class Main {
    

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("Uso: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]");
            return;
        }

        String diretorio = args[0];
        double limiar = Double.parseDouble(args[1]);
        String modo = args[2].toLowerCase();

        File[] arquivos = ComparadorDeDocumentos.listarArquivos(diretorio);
        if (arquivos == null || arquivos.length < 2) {
            System.out.println("Nenhum arquivo .txt encontrado no diretório.");
            return;
        }

        // Lê todos os arquivos em tabelas
        HashTable[] tabelas = new HashTable[arquivos.length];
        for (int i = 0; i < arquivos.length; i++) {
            tabelas[i] = ComparadorDeDocumentos.lerArquivo(arquivos[i].getAbsolutePath());
            
        }
        // === Escrever as palavras e frequências no arquivo ===
        try (FileWriter writer = new FileWriter("documento.txt")) {
            for (int i = 0; i < tabelas.length; i++) {
                String[] chaves = tabelas[i].getKeys();
                writer.write("---------Tabela "+ i +"----------\n");
                
                for (String chave : chaves) {
                    int freq = tabelas[i].getString(chave);
                    writer.write("(" + chave + ", " + freq + ")\n");
                }
                
            }
                System.out.println("Arquivo 'documento.txt' criado com sucesso!");
            } catch (IOException e) {
                System.err.println("Erro ao criar o arquivo: " + e.getMessage());
            }

        // ======================
        // MODO: LISTA
        // ======================
        if (modo.equals("lista")) {
            System.out.println("=== Comparações acima do limiar (" + limiar + ") ===");
            for (int i = 0; i < arquivos.length; i++) {
                for (int j = i + 1; j < arquivos.length; j++) {
                    double sim = ComparadorDeDocumentos.jaccard(tabelas[i], tabelas[j]);
                    if (sim >= limiar) {
                        System.out.printf("%s ↔ %s = %.3f\n", arquivos[i].getName(), arquivos[j].getName(), sim);
                    }
                }
            }
        }

        // ======================
        // MODO: TOPK
        // ======================
        else if (modo.equals("topk")) {
            if (args.length < 4) {
                System.out.println("Erro: no modo 'topk' é necessário informar a quantidade (ex: java Main <dir> <limiar> topk 5)");
                return;
            }

            int quantidade = Integer.parseInt(args[3]);
            double[] valoresJaccard = new double[arquivos.length * arquivos.length];
            String[] comparacoes = new String[arquivos.length * arquivos.length];
            int indice = 0;

            for (int i = 0; i < arquivos.length; i++) {
                for (int j = i + 1; j < arquivos.length; j++) {
                    double sim = ComparadorDeDocumentos.jaccard(tabelas[i], tabelas[j]);
                    valoresJaccard[indice] = sim;
                    comparacoes[indice] = arquivos[i].getName() + " ↔ " + arquivos[j].getName();
                    indice++;
                }
            }

            System.out.println("\n=== Os " + quantidade + " maiores valores de similaridade ===");

            Integer[] indices = new Integer[indice];
            for (int k = 0; k < indice; k++) indices[k] = k;

            java.util.Arrays.sort(indices, (a, b) -> Double.compare(valoresJaccard[b], valoresJaccard[a]));

            for (int k = 0; k < Math.min(quantidade, indice); k++) {
                int idx = indices[k];
                System.out.printf("%s = %.3f\n", comparacoes[idx], valoresJaccard[idx]);
            }
        }

        else {
            System.out.println("Modo inválido! Use 'lista' ou 'topk'.");
        }
    }
}
