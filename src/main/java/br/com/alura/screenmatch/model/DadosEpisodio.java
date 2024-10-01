package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(
        @JsonAlias() String titulo,
        @JsonAlias() Integer numeroEpisodio,
        @JsonAlias() String avaliacao,
        @JsonAlias() String dataLancamento
) {}
