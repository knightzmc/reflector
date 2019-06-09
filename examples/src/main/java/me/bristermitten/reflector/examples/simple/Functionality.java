package me.bristermitten.reflector.examples.simple;

import me.bristermitten.reflector.Reflector;
import me.bristermitten.reflector.config.Options;
import me.bristermitten.reflector.config.OptionsBuilder;
import me.bristermitten.reflector.property.valued.ValuedClassStructure;
import me.bristermitten.reflector.searcher.NameDecider;

public class Functionality {
    public static void main(String[] args) {
        Options options = new OptionsBuilder().nameDecider(NameDecider.NoChange).build();
        Reflector reflector = new Reflector(options); //create a new Reflector with default options
        SimpleDataClass data = new SimpleDataClass(3, "Timmy");
        ValuedClassStructure structure = reflector.getValuedStructure(data);
        structure.searchProperties().byName("name").search().forEach(p -> {
            System.out.println(p.getValue()); //prints "Timmy"
        });
    }
}
