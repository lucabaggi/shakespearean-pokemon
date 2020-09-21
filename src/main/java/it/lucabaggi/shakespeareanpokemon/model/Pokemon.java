package it.lucabaggi.shakespeareanpokemon.model;

public class Pokemon {

    private String name;
    private String description;

    public Pokemon(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final class PokemonBuilder {

        private String name;
        private String description;

        public PokemonBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public PokemonBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Pokemon build() {
            return new Pokemon(name, description);
        }

    }

}
