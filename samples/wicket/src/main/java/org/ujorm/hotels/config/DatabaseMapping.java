/*
 *  Copyright 2009-2013 Pavel Ponec
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.ujorm.hotels.config;

import org.ujorm.hotels.domains.Booking;
import org.ujorm.hotels.domains.Customer;
import org.ujorm.hotels.domains.Hotel;
import org.ujorm.implementation.orm.OrmTable;
import org.ujorm.implementation.orm.RelationToMany;
import org.ujorm.orm.annot.Db;
import org.ujorm.orm.annot.Table;
import org.ujorm.orm.dialect.*;

/**
 * A class mapping to a database (sample of usage)
 * @hidden
 */
@Db(schema="db1", dialect=H2Dialect.class, user="sa", password="", jdbcUrl="jdbc:h2:mem:db1")
//@Db(schema="db1", dialect=H2Dialect.class, user="sa", password="", jdbcUrl="jdbc:h2:~/ujorm/db1")
//@Db(schema="db1", dialect=PostgreSqlDialect.class, user="sa", password="sa", jdbcUrl="jdbc:postgresql://127.0.0.1:5432/db1")
//@Db(schema="db1", dialect=MySqlDialect.class, user="sa", password="sa", jdbcUrl="jdbc:mysql://127.0.0.1:3306/")
//@Db(schema="db1", dialect=DerbyDialect.class, user="sa", password="", jdbcUrl="jdbc:derby:C:\\temp\\derby-sample;create=true")
//@Db(schema="db1", dialect=HsqldbDialect.class, user="sa", password="", jdbcUrl="jdbc:hsqldb:mem:db1")
//@Db(schema= ""  , dialect=FirebirdDialect.class, user="sysdba", password="masterkey", jdbcUrl="jdbc:firebirdsql:localhost/3050:c:\\progra~1\\firebird\\db\\db1.fdb?lc_ctype=UTF8")
//@Db(schema="db1", dialect=OracleDialect.class, user="sa", password="", jdbcUrl="jdbc:oracle:thin:@myhost:1521:orcl")
//@Db(schema="db1", dialect=org.ujorm.orm.dialect.MSSqlDialect.class, user="sa", password="datamaster", jdbcUrl="jdbc:sqlserver://127.0.0.1:1433")
public class DatabaseMapping extends OrmTable<DatabaseMapping> {

    /** Hotel entity */
    @Table("demo_hotel")
    public static final RelationToMany<DatabaseMapping,Hotel> HOTEL = newRelation();

    /** Customer of the appliction. */
    @Table("demo_customer")
    public static final RelationToMany<DatabaseMapping,Customer> CUSTOMER = newRelation();

    /** Booling (a relation between Customers and Hotels type of many to many. */
    @Table("demo_booking")
    public static final RelationToMany<DatabaseMapping,Booking> BOOKING = newRelation();

}
