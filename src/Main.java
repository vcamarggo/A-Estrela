
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * @author V.Camargo
 * 
 * @Date 13 de mai de 2017
 */

class Main {

    static class No implements Comparable<No> {

	private short[][] matrizResolucao = new short[4][4];
	private No pai;
	private int funcaoHLinha;
	private int funcaoG;
	private int funcaoF;
	private short passos = 0;

	public short[][] getMatrizResolucao() {
	    return matrizResolucao;
	}

	public No getPai() {
	    return pai;
	}

	public void setPai(No pai) {
	    this.pai = pai;
	}

	public int getFuncaoHLinha() {
	    return funcaoHLinha;
	}

	public void setFuncaoHLinha(int funcaoHLinha) {
	    this.funcaoHLinha = funcaoHLinha;
	}

	public int getFuncaoG() {
	    return funcaoG;
	}

	public void setFuncaoG(int funcaoG) {
	    this.funcaoG = funcaoG;
	}

	public Integer getFuncaoF() {
	    return funcaoF;
	}

	public short getPassos() {
	    return passos;
	}

	public void setPassos(short passos) {
	    this.passos = passos;
	}

	public void setFuncaoF(short funcaoF) {
	    this.funcaoF = funcaoF;
	}

	public int compareToMatrix(No o) {
	    return (Arrays.deepEquals(getMatrizResolucao(), o.getMatrizResolucao()) ? 0 : 1);
	}

	@Override
	public int compareTo(No o) {
	    return this.funcaoF - o.getFuncaoF();
	}

    }

    private static short[][] solucao = new short[][] { { 1, 5, 9, 13 }, { 2, 6, 10, 14 }, { 3, 7, 11, 15 },
	    { 4, 8, 12, 0 } };
    private static PriorityQueue<No> estadosAbertos = new PriorityQueue<No>();
    private static HashSet<No> estadosFechados = new HashSet<>();
    // static short heuristica;
    private final static short HEURISTICA = 3;

    public static void main(String[] args) throws FileNotFoundException {
	Scanner scan = new Scanner(new FileReader(Main.class.getResource("4.in").getPath()));
	// Scanner scan = new Scanner(System.in);

	// retirar comentario para testar variacoes heuristicas
	// heuristica = scan.nextShort();

	// retirar comentario para testar tempo em milissegundos
	long start = System.currentTimeMillis();

	try {
	    No no = new No();
	    for (short x = 0; x < 4; x++) {
		for (short y = 0; y < 4; y++) {
		    short numeroScaneado = scan.nextShort();
		    no.getMatrizResolucao()[x][y] = numeroScaneado;
		}
	    }
	    no.setFuncaoHLinha(calculaHlinha(no, HEURISTICA));
	    scan.close();
	    estadosAbertos.add(no);

	} catch (Exception e) {
	    e.printStackTrace();
	}

	System.out.println(aEstrela());
	System.out.println(System.currentTimeMillis() - start);
    }

    private static short aEstrela() {
	No no = estadosAbertos.peek();
	Queue<No> sucessores = new PriorityQueue<>();
	while (!estadosAbertos.isEmpty()) {

	    no = estadosAbertos.poll();
	    estadosAbertos.remove(no);
	    estadosFechados.add(no);

	    if (Arrays.deepEquals(no.getMatrizResolucao(), solucao)) {
		return no.getPassos();
	    }
	    sucessores = geraSucessores(no);
	    while (!sucessores.isEmpty()) {
		No suc = sucessores.poll();

		if (estadosFechados.contains(suc)) {
		    if (suc.getPassos() < suc.getPai().getPassos()) {
			estadosFechados.remove(suc.getPai());
			suc.setFuncaoHLinha(calculaHlinha(suc, HEURISTICA));
			suc.setFuncaoF((short) (suc.getPassos() + suc.getFuncaoHLinha()));
			estadosAbertos.add(suc);
		    }
		} else if (estadosAbertos.contains(suc)) {
		    if (suc.getPassos() < suc.getPai().getPassos()) {
			estadosAbertos.remove(suc.getPai());
			suc.setFuncaoHLinha(calculaHlinha(suc, HEURISTICA));
			suc.setFuncaoF((short) (suc.getPassos() + suc.getFuncaoHLinha()));
			estadosAbertos.add(suc);
		    }
		} else {
		    suc.setFuncaoHLinha(calculaHlinha(suc, HEURISTICA));
		    suc.setFuncaoF((short) (suc.getPassos() + suc.getFuncaoHLinha()));
		    estadosAbertos.add(suc);
		}
		System.out.println(no.getFuncaoHLinha());
	    }
	}
	return 0;
    }

    private static Queue<No> geraSucessores(No no) {
	Queue<No> sucessores = new PriorityQueue<>();
	for (int i = 0; i < 4; i++) {
	    for (int j = 0; j < 4; j++) {
		if (no.getMatrizResolucao()[i][j] == 0) {
		    if (i != 0) { // trocar com a peca de cima
			No up = new No();
			up.setPassos((short) (no.getPassos() + 1));
			for (int k = 0; k < 4; k++) {
			    System.arraycopy(no.getMatrizResolucao()[k], 0, up.getMatrizResolucao()[k], 0, 4);
			}

			short aux = up.getMatrizResolucao()[i][j];
			up.getMatrizResolucao()[i][j] = up.getMatrizResolucao()[i - 1][j];
			up.getMatrizResolucao()[i - 1][j] = aux;

			up.setPai(no);
			sucessores.add(up);
		    }
		    if (i != 3) { // trocar com a peca de baixo
			No down = new No();
			down.setPassos((short) (no.getPassos() + 1));
			for (int k = 0; k < 4; k++) {
			    System.arraycopy(no.getMatrizResolucao()[k], 0, down.getMatrizResolucao()[k], 0, 4);
			}

			short aux = down.getMatrizResolucao()[i][j];
			down.getMatrizResolucao()[i][j] = down.getMatrizResolucao()[i + 1][j];
			down.getMatrizResolucao()[i + 1][j] = aux;
			down.setPai(no);
			sucessores.add(down);
		    }
		    if (j != 0) { // trocar com a peca da esquerda
			No left = new No();
			left.setPassos((short) (no.getPassos() + 1));
			for (int k = 0; k < 4; k++) {
			    System.arraycopy(no.getMatrizResolucao()[k], 0, left.getMatrizResolucao()[k], 0, 4);
			}

			short aux = left.getMatrizResolucao()[i][j];
			left.getMatrizResolucao()[i][j] = left.getMatrizResolucao()[i][j - 1];
			left.getMatrizResolucao()[i][j - 1] = aux;
			left.setPai(no);
			sucessores.add(left);
		    }
		    if (j != 3) { // trocar com a peca da direita
			No right = new No();
			right.setPassos((short) (no.getPassos() + 1));
			for (int k = 0; k < 4; k++) {
			    System.arraycopy(no.getMatrizResolucao()[k], 0, right.getMatrizResolucao()[k], 0, 4);
			}

			short aux = right.getMatrizResolucao()[i][j];
			right.getMatrizResolucao()[i][j] = right.getMatrizResolucao()[i][j + 1];
			right.getMatrizResolucao()[i][j + 1] = aux;
			right.setPai(no);
			sucessores.add(right);
		    }
		}
	    }
	}
	return sucessores;
    }

    private static int calculaHlinha(No no, short codigoHeuristica) {
	switch (codigoHeuristica) {
	case 1: // caso heuristica 1
	    short naPosicaoErrada = 0;
	    for (short i = 0; i < 4; i++) {
		for (short j = 0; j < 4; j++) {
		    if (no.getMatrizResolucao()[i][j] != solucao[i][j])
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
			    if (no.getMatrizResolucao()[0][i + 1] != no.getMatrizResolucao()[j][i] + 1
				    && no.getMatrizResolucao()[j][i] != 0)
				naPosicaoErrada++;
			}
		    } else if (no.getMatrizResolucao()[j + 1][i] != no.getMatrizResolucao()[j][i] + 1
			    && no.getMatrizResolucao()[j][i] != 0) {
			naPosicaoErrada++;
		    }
		}
	    }
	    return naPosicaoErrada;
	case 3: // caso heuristica 3
	    naPosicaoErrada = 0;
	    for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
		    double valor = no.getMatrizResolucao()[i][j];
		    if (valor != 0 && valor != solucao[i][j]) {
			short objetivoI = (short) Math.abs(((4 * getFractionalPart(valor / 4)) - 1));
			short objetivoJ = (short) (valor / 4.1);
			float deslocamentoX = i - objetivoI;
			float deslocamentoY = j - objetivoJ;
			naPosicaoErrada += Math.abs(deslocamentoX) + Math.abs(deslocamentoY);
		    }
		}
	    }
	    return naPosicaoErrada;
	case 5: // caso heuristica 3
	    return Math.max(calculaHlinha(no, (short) 1),
		    Math.max(calculaHlinha(no, (short) 2), calculaHlinha(no, (short) 3)));
	default:
	    return Integer.MAX_VALUE;
	}
    }

    private static float getFractionalPart(double n) {
	return (float) (n - Math.floor(n));
    }

    public static void printaMatrizAdjacencia(No no) {
	for (short i = 0; i < 4; i++) {
	    for (short j = 0; j < 4; j++) {
		System.out.print(no.getMatrizResolucao()[i][j] + " ");
	    }
	    System.out.println("");
	}
    }

}
