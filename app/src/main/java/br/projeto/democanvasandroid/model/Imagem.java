package br.projeto.democanvasandroid.model;

/**
 * Created by jonathan on 17/05/16.
 */
public class Imagem {

    private String id;
    private String imagem;

    public Imagem() {
    }

    public Imagem(String id, String imagem) {
        this.id = id;
        this.imagem = imagem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
