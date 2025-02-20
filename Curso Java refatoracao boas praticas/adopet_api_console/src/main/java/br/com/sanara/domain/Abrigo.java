package br.com.sanara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

//essa anotacao  faz com que o Jackson ignore qualquer campo que nao esteja
// declarado na classe, evitando a excecao, no caso o erro em serializar Pets
@JsonIgnoreProperties(ignoreUnknown = true)
public class Abrigo {

    private Long id;

    private String nome;

    private String telefone;

    private String email;

    private List<Pet> pets;

    public Abrigo() {
        // Construtor vazio
    }

    public Abrigo(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public Abrigo(Long id, String nome, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
