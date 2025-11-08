import java.util.ArrayList;
import java.util.List;

// AVLTree.java
public class AVLTree {
    private class Node {
        Resultado dado;
        Node esquerda;
        Node direita;
        int altura;
        int fatorBalanceamento;

        Node(Resultado dado) {
            this.dado = dado;
            this.altura = 1;
        }
    }

    private Node raiz;

    // Contadores de rotações
    private int rotacoesSimplesEsquerda = 0;
    private int rotacoesSimplesDireita = 0;
    private int rotacoesDuplasEsquerda = 0;
    private int rotacoesDuplasDireita = 0;

    // === Inserção ===
    public void inserir(Resultado resultado) {
        raiz = inserir(raiz, resultado);
    }

    private Node inserir(Node node, Resultado resultado) {
        if (node == null)
            return new Node(resultado);

        if (resultado.compareTo(node.dado) < 0) {
            node.esquerda = inserir(node.esquerda, resultado);
        } else if (resultado.compareTo(node.dado) > 0) {
            node.direita = inserir(node.direita, resultado);
        } else {
            // evita duplicatas
            return node;
        }

        atualizarAlturaEFator(node);
        return balancear(node);
    }

    // === Balanceamento e rotações ===
    private Node balancear(Node node) {
        int fb = node.fatorBalanceamento;

        // Caso 1
        if (fb > 1) {
            if(node.esquerda!= null){
              //Caso 1.1
                if (fatorBalanceamento(node.esquerda) >= 0) {
                    rotacoesSimplesDireita++;
                    return rotacaoDireita(node);
                } else {// Caso 1.2
                    rotacoesDuplasDireita++;
                    node.esquerda = rotacaoEsquerda(node.esquerda);
                    return rotacaoDireita(node);
                }  
            }
            
        }
        // Caso 2
        if (fb < -1) {
            if(node.direita!=null){
                // Caso 2.1
                if (fatorBalanceamento(node.direita) <= 0) {
                    rotacoesSimplesEsquerda++;
                    return rotacaoEsquerda(node);
                }else {//Caso 2.2
                    rotacoesDuplasEsquerda++;
                    node.direita = rotacaoDireita(node.direita);
                    return rotacaoEsquerda(node);
                }
            }
        }

        return node;
    }

    private Node rotacaoDireita(Node y) {
        Node x = y.esquerda;
        Node t2 = x.direita;

        x.direita = y;
        y.esquerda = t2;

        atualizarAlturaEFator(y);
        atualizarAlturaEFator(x);

        return x;
    }

    private Node rotacaoEsquerda(Node x) {
        Node y = x.direita;
        Node t2 = y.esquerda;

        y.esquerda = x;
        x.direita = t2;

        atualizarAlturaEFator(x);
        atualizarAlturaEFator(y);

        return y;
    }

    private void atualizarAlturaEFator(Node node) {
        node.altura = 1 + Math.max(altura(node.esquerda), altura(node.direita));
        node.fatorBalanceamento = fatorBalanceamento(node);
    }

    private int altura(Node node) {
        return node == null ? 0 : node.altura;
    }

    private int fatorBalanceamento(Node node) {
        return node == null ? 0 : altura(node.esquerda) - altura(node.direita);
    }

    // ================================================================
    // === Métodos para consulta diretamente na árvore ===
    // ================================================================

    /**
     * Imprime todos os pares com similaridade >= limiar
     * Percorre a árvore em-ordem (em ordem crescente de similaridade)
     */
    public void listarAcimaDoLimiar(float limiar) {
        System.out.println("Pares com similaridade >= " + limiar + ":");
        listarAcimaDoLimiar(raiz, limiar);
    }

    private void listarAcimaDoLimiar(Node node, float limiar) {
        if (node == null) return;

        // visita subárvore esquerda
        listarAcimaDoLimiar(node.esquerda, limiar);

        // processa o nó atual
        if (node.dado.getSimilaridade() >= limiar) {
            System.out.println(node.dado);
        }

        // visita subárvore direita
        listarAcimaDoLimiar(node.direita, limiar);
    }

    /**
     * Imprime os K pares mais semelhantes (ordem decrescente)
     */
    public void topK(int k) {
        System.out.println("Top " + k + " pares mais semelhantes:");
        List<Resultado> lista = new ArrayList<>();
        percorrerEmOrdemDecrescente(raiz, lista, k);
        for (Resultado r : lista) {
            System.out.println(r);
        }
    }

    // percorre a árvore em ordem decrescente até coletar K elementos
    private void percorrerEmOrdemDecrescente(Node node, List<Resultado> lista, int k) {
        if (node == null || lista.size() >= k) return;

        percorrerEmOrdemDecrescente(node.direita, lista, k);

        if (lista.size() < k) {
            lista.add(node.dado);
            percorrerEmOrdemDecrescente(node.esquerda, lista, k);
        }
    }

    /**
     * Busca e imprime a similaridade entre dois arquivos específicos.
     * Retorna true se encontrado.
     */
    public boolean buscarPar(String arquivo1, String arquivo2) {
        return buscarPar(raiz, arquivo1, arquivo2);
    }

    private boolean buscarPar(Node node, String arquivo1, String arquivo2) {
        if (node == null) return false;

        Resultado r = node.dado;
        if ((r.getArquivo1().equals(arquivo1) && r.getArquivo2().equals(arquivo2)) ||
            (r.getArquivo1().equals(arquivo2) && r.getArquivo2().equals(arquivo1))) {
            System.out.println("Resultado encontrado: " + r);
            return true;
        }

        // busca nos dois lados, pois não sabemos onde está o par
        return buscarPar(node.esquerda, arquivo1, arquivo2) ||
               buscarPar(node.direita, arquivo1, arquivo2);
    }

    // ================================================================
    // === Impressões auxiliares e debug ===
    // ================================================================

    public void imprimirEmOrdem() {
        imprimirEmOrdem(raiz);
    }

    private void imprimirEmOrdem(Node node) {
        if (node != null) {
            imprimirEmOrdem(node.esquerda);
            System.out.printf("%s | FB: %d%n", node.dado, node.fatorBalanceamento);
            imprimirEmOrdem(node.direita);
        }
    }
    // Dentro da classe AVLTree
    public void imprimirHierarquica() {
        System.out.println("=== Árvore AVL (hierárquica) ===");
        imprimirHierarquica(raiz, "", true);
    }

    private void imprimirHierarquica(Node node, String prefixo, boolean ehUltimo) {
        if (node != null) {
            System.out.println(prefixo + (ehUltimo ? "└── " : "├── ") + node.dado);
            imprimirHierarquica(node.esquerda, prefixo + (ehUltimo ? "    " : "│   "), false);
            imprimirHierarquica(node.direita, prefixo + (ehUltimo ? "    " : "│   "), true);
        }
    }


    public void imprimirContagemRotacoes() {
        System.out.println("Rot. simples à esquerda: " + rotacoesSimplesEsquerda);
        System.out.println("Rot. simples à direita:  " + rotacoesSimplesDireita);
        System.out.println("Rot. dupla à esquerda:   " + rotacoesDuplasEsquerda);
        System.out.println("Rot. dupla à direita:    " + rotacoesDuplasDireita);
    }
    //para testar, tirar depois
    public static void main(String[] args) {
        AVLTree arvore = new AVLTree();

        // Inserindo alguns exemplos de Resultado
        arvore.inserir(new Resultado("A.txt", "B.txt", 0.45f));
        arvore.inserir(new Resultado("A.txt", "C.txt", 0.67f));
        arvore.inserir(new Resultado("B.txt", "C.txt", 0.33f));
        arvore.inserir(new Resultado("D.txt", "E.txt", 0.75f));
        arvore.inserir(new Resultado("F.txt", "G.txt", 0.10f));
        arvore.inserir(new Resultado("G.txt", "H.txt", 0.92f));
        arvore.inserir(new Resultado("I.txt", "J.txt", 0.88f));

        System.out.println("=== Impressão em ordem (FBs) ===");
        arvore.imprimirEmOrdem();

        System.out.println("\n=== Top 3 mais semelhantes ===");
        arvore.topK(3);

        System.out.println("\n=== Busca por (A.txt, C.txt) ===");
        arvore.buscarPar("A.txt", "C.txt");

        System.out.println("\n=== Resultados acima de 0.5 ===");
        arvore.listarAcimaDoLimiar(0.5f);

        System.out.println("\n=== Contagem de rotações ===");
        arvore.imprimirContagemRotacoes();
    }
}
