import java.util.*;

public class AFND {
    private Set<String> estados;
    private Set<String> alfabeto;
    private Map<String, Map<String, Set<String>>> transicoes;
    private String estadoInicial;
    private Set<String> estadosFinais;

    public AFND(Set<String> estados, Set<String> alfabeto, Map<String, Map<String, Set<String>>> transicoes, String estadoInicial, Set<String> estadosFinais) {
        this.estados = estados;
        this.alfabeto = alfabeto;
        this.transicoes = transicoes;
        this.estadoInicial = estadoInicial;
        this.estadosFinais = estadosFinais;
    }

    public String processarCadeia(String cadeia) {
        Set<String> estadosAtuais = epsilonFecho(Collections.singleton(estadoInicial));

        for (char simbolo : cadeia.toCharArray()) {
            estadosAtuais = transicao(estadosAtuais, Character.toString(simbolo));
            estadosAtuais = epsilonFecho(estadosAtuais);
        }

        for (String estado : estadosAtuais) {
            if (estadosFinais.contains(estado)) {
                return "ACEITA";
            }
        }

        return "RECUSA";
    }

    private Set<String> epsilonFecho(Set<String> estados) {
        Stack<String> pilha = new Stack<>();
        Set<String> visitados = new HashSet<>(estados);

        pilha.addAll(estados);

        while (!pilha.isEmpty()) {
            String estado = pilha.pop();

            if (transicoes.containsKey(estado) && transicoes.get(estado).containsKey("ε")) {
                Set<String> vizinhos = transicoes.get(estado).get("ε");

                for (String vizinho : vizinhos) {
                    if (!visitados.contains(vizinho)) {
                        visitados.add(vizinho);
                        pilha.push(vizinho);
                    }
                }
            }
        }

        return visitados;
    }

    private Set<String> transicao(Set<String> estados, String simbolo) {
        Set<String> proximosEstados = new HashSet<>();

        for (String estado : estados) {
            if (transicoes.containsKey(estado) && transicoes.get(estado).containsKey(simbolo)) {
                proximosEstados.addAll(transicoes.get(estado).get(simbolo));
            }
        }

        return proximosEstados;
    }

    public static void main(String[] args) {
        // Configuração do AFND
        Set<String> estados = new HashSet<>(Arrays.asList("q0", "q1", "q2"));
        Set<String> alfabeto = new HashSet<>(Arrays.asList("0", "1"));

        Map<String, Map<String, Set<String>>> transicoes = new HashMap<>();
        transicoes.put("q0", new HashMap<>());
        transicoes.get("q0").put("0", new HashSet<>(Arrays.asList("q0")));
        transicoes.get("q0").put("1", new HashSet<>(Arrays.asList("q0", "q1")));
        transicoes.get("q0").put("ε", new HashSet<>(Arrays.asList("q2")));
        transicoes.put("q1", new HashMap<>());
        transicoes.get("q1").put("1", new HashSet<>(Arrays.asList("q2")));

        transicoes.put("q2", new HashMap<>());
        transicoes.get("q2").put("0", new HashSet<>(Arrays.asList("q2")));

        String estadoInicial = "q0";
        Set<String> estadosFinais = new HashSet<>(Collections.singletonList("q2"));

        // Criação do AFND
        AFND afnd = new AFND(estados, alfabeto, transicoes, estadoInicial, estadosFinais);

        // Execução do AFND para uma cadeia de entrada
        String cadeia = "0110";
        String resultado = afnd.processarCadeia(cadeia);
        System.out.println("A cadeia '" + cadeia + "' é " + resultado + ".");

        // Execução do AFND para outra cadeia de entrada
        cadeia = "0101";
        resultado = afnd.processarCadeia(cadeia);
        System.out.println("A cadeia '" + cadeia + "' é " + resultado + ".");
    }
}
