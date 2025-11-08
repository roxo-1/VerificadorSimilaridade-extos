import java.io.*;

// ========================
// Classe HashTable simples
// ========================
class HashTable {
    private static class Node {
        String key;
        int value;
        Node next;

        Node(String key, int value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private Node[] tabela;
    private int capacidade;

    public HashTable(int capacidade) {
        this.capacidade = capacidade;
        this.tabela = new Node[capacidade];
    }

    private int hash(String chave) {
        int h = 0;
        for (int i = 0; i < chave.length(); i++) {
            h = (31 * h + chave.charAt(i)) % capacidade;
        }
        return Math.abs(h);
    }

    public void put(String chave, int valor) {
        int indice = hash(chave);
        Node atual = tabela[indice];
        while (atual != null) {
            if (atual.key.equals(chave)) {
                atual.value += valor;
                return;
            }
            atual = atual.next;
        }
        Node novo = new Node(chave, valor);
        novo.next = tabela[indice];
        tabela[indice] = novo;
    }

    public boolean contains(String chave) {
        int indice = hash(chave);
        Node atual = tabela[indice];
        while (atual != null) {
            if (atual.key.equals(chave)) return true;
            atual = atual.next;
        }
        return false;
    }

    public String[] getKeys() {
        int count = 0;
        for (Node bucket : tabela) {
            Node atual = bucket;
            while (atual != null) {
                count++;
                atual = atual.next;
            }
        }

        String[] keys = new String[count];
        int i = 0;
        for (Node bucket : tabela) {
            Node atual = bucket;
            while (atual != null) {
                keys[i++] = atual.key;
                atual = atual.next;
            }
        }
        return keys;
    }
}

// ========================
// Classe principal
// ========================
public class Main {

    // Lê um arquivo e adiciona palavras à HashTable
    public static HashTable lerArquivo(String caminho) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(caminho));
        HashTable tabela = new HashTable(1009);

        String linha;
        while ((linha = leitor.readLine()) != null) {
            String[] palavras = linha.toLowerCase().replaceAll("[^a-záéíóúàâêôãõç]", " ").split("\\s+");
            for (String p : palavras) {
                if (!p.isEmpty()) tabela.put(p, 1);
            }
        }
        leitor.close();
        return tabela;
    }

    // Calcula o índice de Jaccard
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

    // ========================
    // MAIN
    // ========================
    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("Uso: java Main <diretorio_documentos> <limiar> <modo> [argumentos_opcionais]");
            return;
        }

        String diretorio = args[0];
        double limiar = Double.parseDouble(args[1]);
        String modo = args[2].toLowerCase();
        boolean detalhado = args.length > 3 && args[3].equalsIgnoreCase("detalhado");

        File[] arquivos = listarArquivos(diretorio);
        if (arquivos == null || arquivos.length < 2) {
            System.out.println("Nenhum arquivo .txt encontrado no diretório.");
            return;
        }

        // Lê todos os arquivos
        HashTable[] tabelas = new HashTable[arquivos.length];
        for (int i = 0; i < arquivos.length; i++) {
            tabelas[i] = lerArquivo(arquivos[i].getAbsolutePath());
        }

        // ======================
        // Modo SIMPLES
        // ======================
        if (modo.equals("lista")) {
            System.out.println("=== Comparações acima do limiar (" + limiar + ") ===");
            for (int i = 0; i < arquivos.length; i++) {
                for (int j = i + 1; j < arquivos.length; j++) {
                    double sim = jaccard(tabelas[i], tabelas[j]);
                    if (sim >= limiar) {
                        System.out.printf("%s ↔ %s = %.3f\n", arquivos[i].getName(), arquivos[j].getName(), sim);
                        if (detalhado) {
                            System.out.println(" → Arquivos com vocabulário semelhante detectados.");
                        }
                    }
                }
            }
        }

        // ======================
        // Modo TABELA
        // ======================
        else if (modo.equals("topk")) {
            int quantidade = Integer.parseInt(args[3]);
            double[] valoresJaccard = new double[arquivos.length * arquivos.length];
            String[] comparacoes = new String[arquivos.length * arquivos.length];
            int indice = 0;

            for (int i = 0; i < arquivos.length; i++) {
                for (int j = i + 1; j < arquivos.length; j++) {
                    double sim = jaccard(tabelas[i], tabelas[j]);
                    valoresJaccard[indice] = sim;
                    comparacoes[indice] = arquivos[i].getName() + " ↔ " + arquivos[j].getName();
                    indice++;
                }
            }

            // Ordenar e exibir os 'quantidade' maiores valores
            System.out.println("\n=== Os " + quantidade + " maiores valores de similaridade ===");

            // Cria um array de índices para ordenar sem perder as comparações
            Integer[] indices = new Integer[indice];
            for (int k = 0; k < indice; k++) indices[k] = k;

            // Ordena os índices com base nos valores de Jaccard (do maior para o menor)
            java.util.Arrays.sort(indices, (a, b) -> Double.compare(valoresJaccard[b], valoresJaccard[a]));

            // Exibe os 'quantidade' maiores
            for (int k = 0; k < Math.min(quantidade, indice); k++) {
                int idx = indices[k];
                System.out.printf("%s = %.3f\n", comparacoes[idx], valoresJaccard[idx]);
            }

        } else {
            System.out.println("Modo inválido! Use 'simples' ou 'tabela'.");
        }
    }
}
