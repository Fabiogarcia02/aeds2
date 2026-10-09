import java.util.Comparator;

/** Compara produtos pelo valor de venda. */
public class ComparadorPorPreco implements Comparator<Produto> {
    @Override
    public int compare(Produto p1, Produto p2) {
        return Double.compare(p1.valorDeVenda(), p2.valorDeVenda());
    }
}
