public class Resultado implements Comparable<Resultado> {
    private String arquivo1;
    private String arquivo2;
    private float similaridade;

    public Resultado(String arquivo1, String arquivo2, float similaridade) {
        this.arquivo1 = arquivo1;
        this.arquivo2 = arquivo2;
        this.similaridade = similaridade;
    }

    public String getArquivo1() { return arquivo1; }
    public String getArquivo2() { return arquivo2; }
    public float getSimilaridade() { return similaridade; }

    
    @Override
    public int compareTo(Resultado outro) {
        return Float.compare(this.similaridade, outro.similaridade);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s) -> %.4f", arquivo1, arquivo2, similaridade);
    }
}
