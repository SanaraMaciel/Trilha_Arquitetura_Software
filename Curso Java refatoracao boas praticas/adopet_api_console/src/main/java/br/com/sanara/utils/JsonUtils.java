package br.com.sanara.utils;

import br.com.sanara.domain.Abrigo;
import com.google.gson.JsonObject;

public class JsonUtils {

    //transforma a classe Abrigo em um objeto Json
    public static JsonObject toJson(Abrigo abrigo) {
        JsonObject json = new JsonObject();
        json.addProperty("nome", abrigo.getNome());
        json.addProperty("telefone", abrigo.getTelefone());
        json.addProperty("email", abrigo.getEmail());
        return json;
    }
}
