package com.atapps.gerenciadortimes.domain;

/**
 * Created by ANDRECANDIDO on 07/03/2016.
 */
public class Jogador {
    private Long id;
    private String nome;
    private Long idTime;
    private String telefone;
    private boolean goleiro;
    private float forca;
    private String fotoPath;
    private String circlePath;
    private boolean escolhido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getIdTime() {
        return idTime;
    }

    public void setIdTime(Long idTime) {
        this.idTime = idTime;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isGoleiro() {
        return goleiro;
    }

    public void setGoleiro(boolean goleiro) {
        this.goleiro = goleiro;
    }

    public float getForca() {
        return forca;
    }

    public void setForca(float forca) {
        this.forca = forca;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public String getCirclePath() {
        return circlePath;
    }

    public void setCirclePath(String circlePath) {
        this.circlePath = circlePath;
    }

}
