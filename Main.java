import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * @author V.Camargo
 * 
 * @Date 13 de mai de 2017
 */

class Main {

    private static class SolucaoPossivel implements Comparable<SolucaoPossivel> {

	private short[][] matrizResolucao = new short[4][4];
	private int funcaoHLinha;
	private long funcaoF;
	private int passos = 0;
	private String hash = null;

	public short[][] getMatrizResolucao() {
	    return matrizResolucao;
	}

	public int getFuncaoHLinha() {
	    return funcaoHLinha;
	}

	public void setFuncaoHLinha(int funcaoHLinha) {
	    this.funcaoHLinha = funcaoHLinha;
	}

	public Long getFuncaoF() {
	    return funcaoF;
	}

	public int getPassos() {
	    return passos;
	}

	public void setPassos(int passos) {
	    this.passos = passos;
	}

	public void setFuncaoF(short funcaoF) {
	    this.funcaoF = funcaoF;
	}

	public int compareTo(SolucaoPossivel o) {
	    if (this.hashCode() == o.hashCode()) {
		return 0;
	    }
	    if (funcaoF == o.getFuncaoF()) {
		return -1;
	    }
	    return (int) (funcaoF - o.getFuncaoF());
	}

	void setHash() {
	    String key = "";
	    for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
		    key += String.valueOf(matrizResolucao[i][j]);
		}
	    }
	    this.hash = key;
	}

	public String getHash() {
	    return hash;
	}

    }

    private static final short[][] SOLUCAO_FINAL = new short[][] { { 1, 5, 9, 13 }, { 2, 6, 10, 14 }, { 3, 7, 11, 15 },
	    { 4, 8, 12, 0 } };
    private static TreeSet<SolucaoPossivel> estadosAbertos = new TreeSet<SolucaoPossivel>();
    private static HashMap<String, SolucaoPossivel> estadosFechados = new HashMap<String, SolucaoPossivel>();
    private final static short HEURISTICA = 3;
    private static final String HASH_SOLUCAO = "1591326101437111548120";

    public static void main(String[] args) throws FileNotFoundException {
	// Scanner scan = new Scanner(new
	// FileReader(Main.class.getResource("r7.in").getPath()));
	Scanner scan = new Scanner(System.in);

	// retirar comentario para testar tempo em milissegundos
	long start = System.currentTimeMillis();

	try {
	    SolucaoPossivel solucaoPossivel = new SolucaoPossivel();
	    for (short x = 0; x < 4; x++) {
		for (short y = 0; y < 4; y++) {
		    short numeroScaneado = scan.nextShort();
		    solucaoPossivel.getMatrizResolucao()[x][y] = numeroScaneado;
		}
	    }
	    solucaoPossivel.setFuncaoHLinha(calculaHlinha(solucaoPossivel, HEURISTICA));
	    solucaoPossivel.setHash();
	    scan.close();
	    estadosAbertos.add(solucaoPossivel);

	} catch (Exception e) {
	    e.printStackTrace();
	}

	 System.out.println(aEstrela());

	/*System.out.println("Passos: " + aEstrela());
	System.out.println("Millisegundos: " + (System.currentTimeMillis() - start));
	System.out.println("Memoria Usada: "
		+ new DecimalFormat("#.##").format(((double) Runtime.getRuntime().totalMemory() / 1073741824)) + "Gb");*/
    }

    private static long aEstrela() {
	SolucaoPossivel solucaoPossivel = null;
	ArrayList<SolucaoPossivel> sucessores = new ArrayList<>();
	while (!estadosAbertos.isEmpty()) {

	    solucaoPossivel = estadosAbertos.first();
	    estadosAbertos.remove(solucaoPossivel);

	    if (solucaoPossivel.getHash().equals(HASH_SOLUCAO)) {
		return solucaoPossivel.getPassos();
	    }
	    estadosFechados.put(solucaoPossivel.getHash(), solucaoPossivel);

	    sucessores = geraSucessores(solucaoPossivel);

	    while (!sucessores.isEmpty()) {
		SolucaoPossivel suc = sucessores.get(0);
		sucessores.remove(suc);

		SolucaoPossivel aberto = getNoAberto(suc.getHash(), suc.getPassos());
		SolucaoPossivel fechado = estadosFechados.get(suc.getHash());

		if (fechado != null) {
		    if (suc.getPassos() < fechado.getPassos()) {
			estadosFechados.remove(fechado);
			suc.setFuncaoHLinha(calculaHlinha(suc, HEURISTICA));
			suc.setFuncaoF((short) (suc.getPassos() + suc.getFuncaoHLinha()));
			estadosAbertos.add(suc);
		    }
		} else if (aberto != null) {
		    if (suc.getPassos() < aberto.getPassos()) {
			estadosAbertos.remove(aberto);
			suc.setFuncaoHLinha(calculaHlinha(suc, HEURISTICA));
			suc.setFuncaoF((short) (suc.getPassos() + suc.getFuncaoHLinha()));
			estadosAbertos.add(suc);
		    }
		} else {
		    suc.setFuncaoHLinha(calculaHlinha(suc, HEURISTICA));
		    suc.setFuncaoF((short) (suc.getPassos() + suc.getFuncaoHLinha()));
		    estadosAbertos.add(suc);
		}
	    }
	}
	return 0;
    }

    private static SolucaoPossivel getNoAberto(String hash, long passos) {
	Iterator<SolucaoPossivel> iter = estadosAbertos.iterator();
	while (iter.hasNext()) {
	    SolucaoPossivel current = iter.next();
	    if (current.getHash().equals(hash)) {
		return current;
	    } else if (current.getPassos() >= passos) {
		break;
	    }
	}
	return null;
    }

    private static ArrayList<SolucaoPossivel> geraSucessores(SolucaoPossivel solucaoPossivel) {
	ArrayList<SolucaoPossivel> sucessores = new ArrayList<SolucaoPossivel>();
	for (int i = 0; i < 4; i++) {
	    for (int j = 0; j < 4; j++) {
		if (solucaoPossivel.getMatrizResolucao()[i][j] == 0) {
		    if (i != 0) { // trocar com a peca de cima
			SolucaoPossivel up = new SolucaoPossivel();
			up.setPassos((short) (solucaoPossivel.getPassos() + 1));
			for (int k = 0; k < 4; k++) {
			    System.arraycopy(solucaoPossivel.getMatrizResolucao()[k], 0, up.getMatrizResolucao()[k], 0,
				    4);
			}

			short aux = up.getMatrizResolucao()[i][j];
			up.getMatrizResolucao()[i][j] = up.getMatrizResolucao()[i - 1][j];
			up.getMatrizResolucao()[i - 1][j] = aux;

			up.setHash();
			sucessores.add(up);
		    }
		    if (i != 3) { // trocar com a peca de baixo
			SolucaoPossivel down = new SolucaoPossivel();
			down.setPassos((short) (solucaoPossivel.getPassos() + 1));
			for (int k = 0; k < 4; k++) {
			    System.arraycopy(solucaoPossivel.getMatrizResolucao()[k], 0, down.getMatrizResolucao()[k],
				    0, 4);
			}

			short aux = down.getMatrizResolucao()[i][j];
			down.getMatrizResolucao()[i][j] = down.getMatrizResolucao()[i + 1][j];
			down.getMatrizResolucao()[i + 1][j] = aux;
			down.setHash();
			sucessores.add(down);
		    }
		    if (j != 0) { // trocar com a peca da esquerda
			SolucaoPossivel left = new SolucaoPossivel();
			left.setPassos((short) (solucaoPossivel.getPassos() + 1));
			for (int k = 0; k < 4; k++) {
			    System.arraycopy(solucaoPossivel.getMatrizResolucao()[k], 0, left.getMatrizResolucao()[k],
				    0, 4);
			}

			short aux = left.getMatrizResolucao()[i][j];
			left.getMatrizResolucao()[i][j] = left.getMatrizResolucao()[i][j - 1];
			left.getMatrizResolucao()[i][j - 1] = aux;
			left.setHash();
			sucessores.add(left);
		    }
		    if (j != 3) { // trocar com a peca da direita
			SolucaoPossivel right = new SolucaoPossivel();
			right.setPassos((short) (solucaoPossivel.getPassos() + 1));
			for (int k = 0; k < 4; k++) {
			    System.arraycopy(solucaoPossivel.getMatrizResolucao()[k], 0, right.getMatrizResolucao()[k],
				    0, 4);
			}

			short aux = right.getMatrizResolucao()[i][j];
			right.getMatrizResolucao()[i][j] = right.getMatrizResolucao()[i][j + 1];
			right.getMatrizResolucao()[i][j + 1] = aux;
			right.setHash();
			sucessores.add(right);
		    }
		}
	    }
	}
	return sucessores;
    }

    private static int calculaHlinha(SolucaoPossivel solucaoPossivel, short codigoHeuristica) {
	switch (codigoHeuristica) {
	case 1: // caso heuristica 1
	    short naPosicaoErrada = 0;
	    for (short i = 0; i < 4; i++) {
		for (short j = 0; j < 4; j++) {
		    if (solucaoPossivel.getMatrizResolucao()[i][j] != SOLUCAO_FINAL[i][j])
			naPosicaoErrada++;
		}
	    }
	    return naPosicaoErrada;
	case 2: // caso heuristica 2
	    naPosicaoErrada = 0;
	    for (short i = 0; i < 4; i++) {
		for (short j = 0; j < 4; j++) {
		    if (j == 3) {
			if (i != 3) {
			    if (solucaoPossivel.getMatrizResolucao()[0][i
				    + 1] != solucaoPossivel.getMatrizResolucao()[j][i] + 1
				    && solucaoPossivel.getMatrizResolucao()[j][i] != 0)
				naPosicaoErrada++;
			}
		    } else if (solucaoPossivel.getMatrizResolucao()[j
			    + 1][i] != solucaoPossivel.getMatrizResolucao()[j][i] + 1
			    && solucaoPossivel.getMatrizResolucao()[j][i] != 0) {
			naPosicaoErrada++;
		    }
		}
	    }
	    return naPosicaoErrada;
	case 3: // caso heuristica 3
	    naPosicaoErrada = 0;
	    for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
		    double valor = solucaoPossivel.getMatrizResolucao()[i][j];
		    if (valor != 0 && valor != SOLUCAO_FINAL[i][j]) {
			naPosicaoErrada += Math.abs(i - (short) Math.abs((4 * (valor / 4 - Math.floor(valor / 4)) - 1)))
				+ Math.abs(j - (short) (valor / 4.1));
		    }
		}
	    }
	    return naPosicaoErrada;
	case 4: // caso heuristica 4
	    return (int) (0.2D * calculaHlinha(solucaoPossivel, (short) 1)
		    + 0.1D * calculaHlinha(solucaoPossivel, (short) 2)
		    + 0.8D * calculaHlinha(solucaoPossivel, (short) 3));
	case 5: // caso heuristica 5
	    return Math.max(calculaHlinha(solucaoPossivel, (short) 1),
		    Math.max(calculaHlinha(solucaoPossivel, (short) 2), calculaHlinha(solucaoPossivel, (short) 3)));
	case 6: // caso heuristica 6
	    return Math.max(calculaHlinha(solucaoPossivel, (short) 1), calculaHlinha(solucaoPossivel, (short) 3));
	default:
	    return Integer.MAX_VALUE;
	}
    }

}