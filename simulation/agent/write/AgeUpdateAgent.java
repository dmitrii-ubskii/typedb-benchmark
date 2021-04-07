/*
 * Copyright (C) 2020 Grakn Labs
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

package grakn.benchmark.simulation.agent.write;

import grakn.benchmark.simulation.action.Action;
import grakn.benchmark.simulation.action.ActionFactory;
import grakn.benchmark.simulation.action.write.UpdateAgesOfPeopleInCityAction;
import grakn.benchmark.simulation.agent.base.SimulationContext;
import grakn.benchmark.simulation.agent.region.CityAgent;
import grakn.benchmark.simulation.driver.Client;
import grakn.benchmark.simulation.driver.Session;
import grakn.benchmark.simulation.driver.Transaction;
import grakn.benchmark.simulation.world.World;

import java.util.List;
import java.util.Random;

public class AgeUpdateAgent<TX extends Transaction> extends CityAgent<TX> {

    public AgeUpdateAgent(Client<TX> dbDriver, ActionFactory<TX, ?> actionFactory, SimulationContext context) {
        super(dbDriver, actionFactory, context);
    }

    @Override
    protected void run(Session<TX> session, World.City region, List<Action<?, ?>.Report> reports, Random random) {
        try (TX tx = session.newTransaction(region.tracker(), context.iteration(), isTracing())) {
            UpdateAgesOfPeopleInCityAction<TX> updateAgesOfAllPeopleInCityAction = actionFactory().updateAgesOfPeopleInCityAction(tx, context.today(), region);
            runAction(updateAgesOfAllPeopleInCityAction, context.isTest(), reports);
            tx.commit();
        }
    }
}
