package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private Scanner input = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados converteDados = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    public void exibeMenu() {
        System.out.println("Digite a série desejada.");
        var nomeSerie = input.nextLine();

        var url = ENDERECO + nomeSerie.replace(" ", "+") + API_KEY;
        var jsonRequest = consumoAPI.getRequestData(url);

        DadosSerie dadosSerie = converteDados.parseData(jsonRequest, DadosSerie.class);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            url = ENDERECO + nomeSerie.replace(" ", "+") + "&Season=" + i + API_KEY;
            jsonRequest = consumoAPI.getRequestData(url);
            DadosTemporada dadosTemporada = converteDados.parseData(jsonRequest, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        System.out.println("\n" + dadosSerie + "\n");
        temporadas.forEach(System.out::println);
    }
}
