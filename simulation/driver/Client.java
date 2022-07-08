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

package com.vaticle.typedb.benchmark.simulation.driver;

import com.vaticle.typedb.benchmark.common.concept.Region;

public interface Client<SESSION extends Session<TX>, TX extends Transaction> extends AutoCloseable {

    SESSION session(Region region);

    void closeSessions();

    String printStatistics();

    @Override
    void close();

    enum TracingLabel {
        OPEN_CLIENT("open_client"),
        CLOSE_CLIENT("close_client"),
        OPEN_SESSION("open_session"),
        CLOSE_SESSION("close_session"),
        OPEN_TRANSACTION("open_tx"),
        CLOSE_TRANSACTION("close_tx"),
        COMMIT_TRANSACTION("commit_tx"),
        EXECUTE("execute"),
        SORTED_EXECUTE("sorted_execute"),
        EXECUTE_ASYNC("execute_async");

        private String name;

        TracingLabel(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
