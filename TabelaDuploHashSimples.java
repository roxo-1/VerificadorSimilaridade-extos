public class TabelaDuploHashSimples {
    private static final int TAMANHO = 101; // número primo
    private String[] chaves;
    private int[] valores;

    public TabelaDuploHashSimples() {
        chaves = new String[TAMANHO];
        valores = new int[TAMANHO];
    }

    // Primeira função hash (método da divisão)
    private int hash1(String chave) {
        int h = Math.abs(chave.hashCode());
        return h % TAMANHO;
    }

    // Segunda função hash
    private int hash2(String chave) {
        int h = Math.abs(chave.hashCode());
        return 1 + (h % (TAMANHO - 1));
    }

    // Inserir (sem verificar tabela cheia)
    public void inserir(String chave, int valor) {
        int h1 = hash1(chave);
        int h2 = hash2(chave);
        int i = 0;

        while (true) {
            int pos = (h1 + i * h2) % TAMANHO;
            if (chaves[pos] == null || chaves[pos].equals(chave)) {
                chaves[pos] = chave;
                valores[pos] = valor;
                return;
            }
            i++;
            if (i == TAMANHO) {
                System.out.println("Tabela cheia!");
                return;
            }
        }
    }

    public Integer buscar(String chave) {
        int h1 = hash1(chave);
        int h2 = hash2(chave);
        int i = 0;

        while (i < TAMANHO) {
            int pos = (h1 + i * h2) % TAMANHO;
            if (chaves[pos] == null) return null; // parou
            if (chaves[pos].equals(chave)) return valores[pos];
            i++;
        }
        return null;
    }

    public void mostrar() {
        System.out.println("Índice | Chave   | Valor");
        for (int i = 0; i < TAMANHO; i++) {
            System.out.printf("%6d | %-7s | %-5s\n",
                    i,
                    chaves[i] == null ? "—" : chaves[i],
                    chaves[i] == null ? "—" : valores[i]);
        }
        System.out.println();
    }

    public String[] getChaves() {
        return chaves;
    }

    public int[] getValores() {
        return valores;
    }

    public static void main(String[] args) {
        TabelaDuploHashSimples tabela = new TabelaDuploHashSimples();

        tabela.inserir("joao", 10);
        tabela.inserir("maria", 20);
        tabela.inserir("ana", 30);
        tabela.inserir("carlos", 40);
        tabela.inserir("bruno", 50);
        tabela.inserir("lara", 60);
        tabela.inserir("pedro", 70);

        tabela.mostrar();

        System.out.println("Buscar maria -> " + tabela.buscar("maria"));
        System.out.println("Buscar pedro -> " + tabela.buscar("pedro"));
    }
}

