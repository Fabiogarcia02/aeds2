import java.util.Comparator;

public class Insercao<T extends Comparable<T>> implements IOrdenator<T> {

    private T[] dadosOrdenados;
    private Comparator<T> comparador;
    private long comparacoes;
    private long movimentacoes;
    private long inicio;
    private long termino;

    public Insercao() {
        setComparador(T::compareTo);
    }

    public Insercao(Comparator<T> comparador) {
        setComparador(comparador);
    }

    @Override
    public void setComparador(Comparator<T> comparador) {
        this.comparador = comparador;
    }

    @Override
    public T[] ordenar(T[] dados) {
        dadosOrdenados = dados;
        comparacoes = 0;
        movimentacoes = 0;
        inicio = System.nanoTime();

        for (int i = 1; i < dadosOrdenados.length; i++) {
            T atual = dadosOrdenados[i];
            int j = i - 1;
            while (j >= 0) {
                comparacoes++;
                if (comparador.compare(dadosOrdenados[j], atual) <= 0) {
                    break;
                }
                dadosOrdenados[j + 1] = dadosOrdenados[j];
                // Cada deslocamento e a inserção final contam como movimentações.
                movimentacoes++;
                j--;
            }
            if (j + 1 != i) {
                dadosOrdenados[j + 1] = atual;
                movimentacoes++;
            }
        }

        termino = System.nanoTime();
        return dadosOrdenados;
    }

    @Override
    public long getComparacoes() {
        return comparacoes;
    }

    @Override
    public long getMovimentacoes() {
        return movimentacoes;
    }

    @Override
    public double getTempoOrdenacao() {
        return (termino - inicio) / 1_000_000.0;
    }
}
