import java.util.Comparator;

public class Quicksort<T extends Comparable<T>> implements IOrdenator<T> {

    private T[] dadosOrdenados;
    private Comparator<T> comparador;
    private long comparacoes;
    private long movimentacoes;
    private long inicio;
    private long termino;

    public Quicksort() {
        comparacoes = 0;
        movimentacoes = 0;
        setComparador(T::compareTo);
    }

    public Quicksort(Comparator<T> comparador) {
        comparacoes = 0;
        movimentacoes = 0;
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
        iniciar();
        quicksort(0, dadosOrdenados.length - 1);
        terminar();
        return dadosOrdenados;
    }

    private void quicksort(int esq, int dir) {
        if (esq < dir) {
            int pivo = particionar(esq, dir);
            quicksort(esq, pivo);
            quicksort(pivo + 1, dir);
        }
    }

    private int particionar(int esq, int dir) {
        T pivo = dadosOrdenados[esq + (dir - esq) / 2];
        int i = esq - 1;
        int j = dir + 1;

        while (true) {
            do {
                i++;
                comparacoes++;
            } while (comparador.compare(dadosOrdenados[i], pivo) < 0);

            do {
                j--;
                comparacoes++;
            } while (comparador.compare(dadosOrdenados[j], pivo) > 0);

            if (i >= j) {
                return j;
            }

            T temp = dadosOrdenados[i];
            dadosOrdenados[i] = dadosOrdenados[j];
            dadosOrdenados[j] = temp;
            // Uma troca conta como uma movimentação, assim como no Bubblesort.
            movimentacoes++;
        }
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

    private void iniciar() {
        inicio = System.nanoTime();
    }

    private void terminar() {
        termino = System.nanoTime();
    }
}
