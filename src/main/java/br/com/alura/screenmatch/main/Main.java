package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodios;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
        System.out.println("\n" + dadosSerie + "\n");

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            url = ENDERECO + nomeSerie.replace(" ", "+") + "&Season=" + i + API_KEY;
            jsonRequest = consumoAPI.getRequestData(url);
            DadosTemporada dadosTemporada = converteDados.parseData(jsonRequest, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);System.out.println();

//        laço 'for' simples
//        for (int i = 0; i < dadosSerie.totalTemporadas(); i++) {
//            List<DadosEpisodio> list = temporadas.get(i).dadosEpisodios();
//            for (int j = 0; j < list.size(); j++) {
//                System.out.println(list.get(j).titulo());
//            }
//        }

//        laço 'forEach'
//        for (DadosTemporada t : temporadas) {
//            List<DadosEpisodio> list = t.dadosEpisodios();
//            for (DadosEpisodio e : list) {
//                System.out.println(e.titulo());
//            }
//        }

//        laço com o método 'forEach()'
//        temporadas.forEach(t -> t.dadosEpisodios().forEach(e -> System.out.println(e.titulo())));

//        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());

//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("n/a"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);

        List<Episodios> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream().map(d -> new Episodios(t.numeroTemporada(), d)))
                .collect(Collectors.toList());

        System.out.println("Buscar a partir de qual ano?");
        var anoBusca = input.nextInt();
        input.nextLine();

        LocalDate dataBusca = LocalDate.of(anoBusca, 1, 1);
        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> {
                    System.out.println(
                            "Temporada: " + e.getTemporada()
                                    + " Episódio: " + e.getNumeroEpisodio()
                                    + " Lançamento: " + e.getDataLancamento());});
    }
}
