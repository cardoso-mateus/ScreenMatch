package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.service.ConsumoAPI;

import java.util.Scanner;

public class Main {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    public void exibeMenu() {
        System.out.println("Digite a série desejada.");
        var nomeSerie = leitura.nextLine();
        var URI = ENDERECO + nomeSerie.replace(" ", "+") + API_KEY;
        consumo.obterDados(URI);
    }
}
