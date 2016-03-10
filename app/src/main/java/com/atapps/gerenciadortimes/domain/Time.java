package com.atapps.gerenciadortimes.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANDRE on 05/03/2016.
 */
public class Time {
    private Long id;
    private String nome;
    private String dia;
    List<String> semana = new ArrayList<String>();
    private String hora;
    private String local;
    private String fotoPath;
    private String circlePath;

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

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
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

    public List<String> getSemana() {
        semana.add("Domingo");
        semana.add("Segunda-Feira");
        semana.add("Terça-Feira");
        semana.add("Quarta-Feira");
        semana.add("Quinta-Feira");
        semana.add("Sexta-Feira");
        semana.add("Sábado");
        return semana;
    }

    public void setSemana(List<String> semana) {
        this.semana = semana;
    }
}
