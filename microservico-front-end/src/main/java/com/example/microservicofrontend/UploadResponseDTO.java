package com.example.microservicofrontend;

public class UploadResponseDTO {


    private boolean sucesso;
    private String nome;
    private float tamanho;
    private String hash;

    private String bytes;
    public UploadResponseDTO() {
    }

    // Exemplo de construtor com parâmetros
    public UploadResponseDTO(boolean sucesso, String nome, float tamanho, String hash, String bytes) {
        this.sucesso = sucesso;
        this.nome = nome;
        this.tamanho = tamanho;
        this.hash = hash;
        this.bytes=bytes;
    }

    // Getters e Setters
    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getTamanho() {
        return tamanho;
    }

    public void setTamanho(float tamanho) {
        this.tamanho = tamanho;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }
}
