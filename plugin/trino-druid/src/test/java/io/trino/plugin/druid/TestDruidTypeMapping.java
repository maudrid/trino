/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.plugin.druid;

import com.google.common.collect.ImmutableMap;
import io.trino.spi.type.VarcharType;
import io.trino.testing.AbstractTestQueryFramework;
import io.trino.testing.QueryRunner;
import io.trino.testing.datatype.DataSetup;
import io.trino.testing.datatype.SqlDataTypeTest;
import io.trino.testing.sql.TrinoSqlExecutor;
import org.testng.annotations.Test;
import static io.trino.spi.type.TimestampType.TIMESTAMP_MILLIS;

public class TestDruidTypeMapping
        extends AbstractTestQueryFramework
{
    private static final String DRUID_DOCKER_IMAGE = "apache/druid:0.18.0";
    protected TestingDruidServer druidServer;

    @Override
    protected QueryRunner createQueryRunner()
            throws Exception
    {
        this.druidServer = new TestingDruidServer(DRUID_DOCKER_IMAGE);
        return DruidQueryRunner.createDruidQueryRunnerTpch(druidServer, ImmutableMap.of());
    }

    @Test
    public void testTimeCoercion()
    {
        SqlDataTypeTest.create()
                .addRoundTrip("string", "dummy_varchar", VarcharType.VARCHAR, "dummy_varchar")
                .addRoundTrip("timestamp(3)", "1970-01-01 00:00:00", TIMESTAMP_MILLIS, "1970-01-01 00:00:00")
                .execute(getQueryRunner(), trinoCreateAsSelect("time_coercion_test"));
    }

    private DataSetup trinoCreateAsSelect(String dataSourceName)
    {
        return new DruidCreateAndInsertDataSetup(new TrinoSqlExecutor(getQueryRunner()), this.druidServer, dataSourceName);
    }
}
