public class HashTable {
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

    public int get(String chave) {
        int indice = hash(chave);
        Node atual = tabela[indice];
        while (atual != null) {
            if (atual.key.equals(chave)) return atual.value;
            atual = atual.next;
        }
        return 0;
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
