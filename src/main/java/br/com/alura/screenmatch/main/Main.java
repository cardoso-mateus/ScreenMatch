package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodios;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.util.*;
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

        List<DadosTemporada> temporadas = inicializaListaDadosTemporada(dadosSerie, nomeSerie);
//        imprimeListaDadosTemporada(temporadas);
//        imprimeListaGetTituloDadosEpisodio(dadosSerie, temporadas);
        List<DadosEpisodio> dadosEpisodios = iniciazilaListaDadosEpisodio(temporadas);
//        imprimeTop5EpisodiosPorAvaliacao(dadosEpisodios);
        List<Episodios> episodios = inicializaListaEpisodios(temporadas);
//        buscaEpisodioPorTrechoDoTitulo(episodios);
//        buscaEpisodiosDesdeDataDeBusca(episodios);
//        imprimeNotaMediaPorTemporada(episodios);
//        imprimeEstatisticas(episodios);
    }

    private static void imprimeEstatisticas(List<Episodios> episodios) {
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodios::getAvaliacao));
        System.out.println("Quatidade de episódios avaliados: " + est.getCount()
                            + "\nMaior avaliação: " + est.getMax()
                            + "\nMenor avaliação: " + est.getMin()
                            + "\nMédia de todos os episódios: " + est.getAverage());
    }

    private static void imprimeNotaMediaPorTemporada(List<Episodios> episodios) {
        Map<Integer, Double> mediaTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodios::getTemporada, Collectors.averagingDouble(Episodios::getAvaliacao)));
        System.out.println(mediaTemporada);
    }

    private void buscaEpisodiosDesdeDataDeBusca(List<Episodios> episodios) {
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

    private void buscaEpisodioPorTrechoDoTitulo(List<Episodios> episodios) {
        System.out.println("Digite um trecho do episódio que deseja buscar:");
        var trechoBuscado = input.nextLine();

        Optional<Episodios> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toLowerCase().contains(trechoBuscado.toLowerCase()))
                .findFirst();

        if (episodioBuscado.isPresent()) {
            System.out.println("Primeiro resultado da busca:"
                    + "\nEpisódio: " + episodioBuscado.get().getTitulo()
                    + "\nTemporada: " + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Nenhum episódio encontrado nessa busca.");
        }
    }

    private List<Episodios> inicializaListaEpisodios(List<DadosTemporada> temporadas) {
        return temporadas.stream()
                .flatMap(t -> t.episodios().stream().map(d -> new Episodios(t.numeroTemporada(), d)))
                .collect(Collectors.toList());
    }

    private void imprimeTop5EpisodiosPorAvaliacao(List<DadosEpisodio> dadosEpisodios) {
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("n/a"))
                /*.peek(e -> System.out.println("Filtro de avaliação \"N/A\" " + e))*/
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                /*.peek(e -> System.out.println("Ordenação em ordem lexicografica invertida " + e))*/
                .limit(5)
                /*.peek(e -> System.out.println("Limitando 5 itens " + e))*/
                .forEach(System.out::println);
    }

    private List<DadosEpisodio> iniciazilaListaDadosEpisodio(List<DadosTemporada> temporadas) {
        return temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());
    }

    private void imprimeListaGetTituloDadosEpisodio(DadosSerie dadosSerie, List<DadosTemporada> temporadas) {
/*
//        laço 'for' simples
        for (int i = 0; i < dadosSerie.totalTemporadas(); i++) {
            List<DadosEpisodio> list = temporadas.get(i).episodios();
            for (int j = 0; j < list.size(); j++) {
                System.out.println(list.get(j).titulo());
            }
        }
*/

/*
//        laço 'forEach'
        for (DadosTemporada t : temporadas) {
            List<DadosEpisodio> list = t.episodios();
            for (DadosEpisodio e : list) {
                System.out.println(e.titulo());
            }
        }
*/

//        laço com o método 'forEach()'
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
    }

    private void imprimeListaDadosTemporada(List<DadosTemporada> temporadas) {
        temporadas.forEach(System.out::println);
    }

    private List<DadosTemporada> inicializaListaDadosTemporada(DadosSerie dadosSerie, String nomeSerie) {
        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            String url = ENDERECO + nomeSerie.replace(" ", "+") + "&Season=" + i + API_KEY;
            String jsonRequest = consumoAPI.getRequestData(url);
            DadosTemporada dadosTemporada = converteDados.parseData(jsonRequest, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        return temporadas;
    }

}
