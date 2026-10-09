import java.util.Comparator;

public class Selecao<T extends Comparable<T>> implements IOrdenator<T> {

    private T[] dadosOrdenados;
    private Comparator<T> comparador;
    private long comparacoes;
    private long movimentacoes;
    private long inicio;
    private long termino;

    public Selecao() {
        setComparador(T::compareTo);
    }

    public Selecao(Comparator<T> comparador) {
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

        for (int i = 0; i < dadosOrdenados.length - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < dadosOrdenados.length; j++) {
                comparacoes++;
                if (comparador.compare(dadosOrdenados[j], dadosOrdenados[menor]) < 0) {
                    menor = j;
                }
            }
            if (menor != i) {
                T temp = dadosOrdenados[i];
                dadosOrdenados[i] = dadosOrdenados[menor];
                dadosOrdenados[menor] = temp;
                // Uma troca conta como uma movimentação.
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
