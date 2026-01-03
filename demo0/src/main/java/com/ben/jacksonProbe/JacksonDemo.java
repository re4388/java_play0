package com.ben.jacksonProbe;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Map;

public class JacksonDemo {
    public static void main(String[] args) {
        // With default settings can use
        ObjectMapper mapper = new ObjectMapper(); // create once, reuse

        mapper = JsonMapper.builder()
                // configuration
                .build();


//        MyValue value = mapper.readValue(new File("data.json"), MyValue.class);
//        value = mapper.readValue(new URL("http://some.com/api/entry.json"), MyValue.class);
//        MyValue value = mapper.readValue("{\"name\":\"Bob\", \"age\":13}", MyValue.class);
//        System.out.println(value);


//        MyValue myResultObject = new MyValue("Bob", 13);
//        mapper.writeValue(new File("result.json"), myResultObject);
//        byte[] jsonBytes = mapper.writeValueAsBytes(myResultObject);
//        String jsonString = mapper.writeValueAsString(myResultObject);


//        Map<String, Integer> scoreByName = mapper.readValue(jsonSource, Map.class);
//        List<String> names = mapper.readValue(jsonSource, List.class);


//        Map<String, ResultValue> results = mapper.readValue(jsonSource,
//                new TypeReference<Map<String, ResultValue>>() { } );
// why extra work? Java Type Erasure will prevent type detection otherwise


//        JsonNode root = mapper.readTree("{ \"name\": \"Joe\", \"age\": 13 }");
//        String name = root.get("name").asString();
//        int age = root.get("age").asInt();
//
//        // can modify as well: this adds child Object as property 'other', set property 'type'
//        root.withObject("/other").put("type", "student");
//        String json = mapper.writeValueAsString(root); // prints below
//        System.out.println(json);

/*
with above, we end up with something like as 'json' String:
{
  "name" : "Bob",
  "age" : 13,
  "other" : {
    "type" : "student"
  }
}
*/

//
//        JsonNode root = mapper.readTree(complexJson);
//        Person p = mapper.treeToValue(root.get("person"), Person.class); // known single pojo
//        Map<String, Object> dynamicmetadata = mapper.treeToValue(root.get("dynamicmetadata"), Map.class); // unknown smallish subfield, convert all to collections
//        int singledeep = root.get("deep").get("large").get("hiearchy").get("important").intValue(); // single value in very deep optional subfield, ignoring the rest
//        int singledeeppath = root.at("/deep/large/hiearchy/important").intValue(); // json path
//        int singledeeppathunique = root.findValue("important").intValue(); // by unique field name



       // Send an aggregate json from heterogenous sources
        ObjectNode root = mapper.createObjectNode();
        root.putPOJO("person", new Person("Joe")); // simple pojo
        root.putPOJO("friends", List.of(new Person("Jane"), new Person("Jack"))); // generics

        Map<String, Object> dynamicMetadata = Map.of("Some", "Metadata");
        root.putPOJO("dynamicMetadata", dynamicMetadata);  // collections
//        root.putPOJO("dynamicMetadata", mapper.valueToTree(dynamicMetadata)); // same thing
//        root.set("dynamicMetadata", mapper.valueToTree(dynamicMetadata)); // same thing
        root.withObject("deep")
                .withObject("large")
                .withObject("hierarchy")
                .put("important", 42); // create as you go
        root.withObject("/deep/large/hiearchy").put("important", 42); // json path
        String s = mapper.writeValueAsString(root);
        System.out.println(s);


    }
}
