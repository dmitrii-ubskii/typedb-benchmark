/*
 * Copyright (C) 2022 Vaticle
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.vaticle.typedb.benchmark.neo4j.agent;

import com.vaticle.typedb.benchmark.common.concept.City;
import com.vaticle.typedb.benchmark.common.concept.Gender;
import com.vaticle.typedb.benchmark.common.concept.Person;
import com.vaticle.typedb.benchmark.common.params.Context;
import com.vaticle.typedb.benchmark.neo4j.driver.Neo4jClient;
import com.vaticle.typedb.benchmark.neo4j.driver.Neo4jTransaction;
import com.vaticle.typedb.benchmark.simulation.agent.PersonAgent;
import com.vaticle.typedb.common.collection.Pair;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vaticle.typedb.benchmark.neo4j.Labels.ADDRESS;
import static com.vaticle.typedb.benchmark.neo4j.Labels.BIRTH_DATE;
import static com.vaticle.typedb.benchmark.neo4j.Labels.CITY;
import static com.vaticle.typedb.benchmark.neo4j.Labels.CODE;
import static com.vaticle.typedb.benchmark.neo4j.Labels.EMAIL;
import static com.vaticle.typedb.benchmark.neo4j.Labels.FIRST_NAME;
import static com.vaticle.typedb.benchmark.neo4j.Labels.GENDER;
import static com.vaticle.typedb.benchmark.neo4j.Labels.LAST_NAME;
import static com.vaticle.typedb.benchmark.neo4j.Labels.PERSON;
import static com.vaticle.typedb.common.collection.Collections.pair;

public class Neo4jPersonAgent extends PersonAgent<Neo4jTransaction> {

    public Neo4jPersonAgent(Neo4jClient client, Context context) {
        super(client, context);
    }

    @Override
    protected Optional<Pair<Person, City>> insertPerson(Neo4jTransaction tx, String email, String firstName, String lastName,
                                                        String address, Gender gender, LocalDateTime birthDate, City city) {
        String query = "MATCH (c:City {code: $code}) " +
                "CREATE (person:Person {" +
                "email: $email, " +
                "firstName: $firstName, " +
                "lastName: $lastName, " +
                "address: $address, " +
                "gender: $gender, " +
                "birthDate: $birthDate" +
                "})-[:BORN_IN]->(c), " +
                "(person)-[:RESIDES_IN]->(c)";
        HashMap<String, Object> parameters = new HashMap<>() {{
            put(CODE, city.code());
            put(EMAIL, email);
            put(FIRST_NAME, firstName);
            put(LAST_NAME, lastName);
            put(ADDRESS, address);
            put(GENDER, gender.value());
            put(BIRTH_DATE, birthDate);
        }};
        tx.execute(new Query(query, parameters));
        if (context.isReporting()) return report(tx, email);
        else return Optional.empty();
    }

    private Optional<Pair<Person, City>> report(Neo4jTransaction tx, String email) {
        List<Record> answers = tx.execute(new Query(
                "MATCH (person:Person {email: '" + email + "'})-[:BORN_IN]->(city:City), " +
                        "(person)-[:RESIDES_IN]->(city) " +
                        "RETURN person.email, person.firstName, person.lastName, person.address, " +
                        "person.gender, person.birthDate, city.code"
        ));
        assert answers.size() == 1;
        Map<String, Object> inserted = answers.get(0).asMap();
        Person person = new Person((String) inserted.get(PERSON + "." + EMAIL),
                                   (String) inserted.get(PERSON + "." + FIRST_NAME),
                                   (String) inserted.get(PERSON + "." + LAST_NAME),
                                   (String) inserted.get(PERSON + "." + ADDRESS),
                                   Gender.of((String) inserted.get(PERSON + "." + GENDER)),
                                   (LocalDateTime) inserted.get(PERSON + "." + BIRTH_DATE));
        City city = new City((String) inserted.get(CITY + "." + CODE));
        return Optional.of(pair(person, city));
    }
}
