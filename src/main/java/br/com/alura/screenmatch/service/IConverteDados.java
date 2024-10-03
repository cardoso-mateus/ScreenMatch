package br.com.alura.screenmatch.service;

public interface IConverteDados {
    <T> T parseData(String json, Class<T> classe);
}
