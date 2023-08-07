/*

注：
  这是放在 src\main\resources 目录下的 schema.sql 文件，用于初始化数据库。
  Spring Boot 在启动时会自动执行这个文件中的 SQL 语句。

Cat Cloud 应用的数据库模式图图下：

|-----------------------------|     |---------------------------|     |-------------------------|     |----------------|
|          Cat_Order          |     |          Cat              |     |      Cat_Ingredient     |     |   Ingredient   |
|-----------------------------|     |---------------------------|     |-------------------------|     |----------------|
| id              : identity  |     | id            : identity  |     | cat_id        : bigint  |     | id   : varchar | 
| delivery_name   : varchar   |     | name          : varchar   |     | cat_key       : bigint  |     | name : varchar | 
| delivery_street : varchar   |     | cat_order_id  : bigint    |     | ingredient_id : varchar |     | type : varchar | 
| delivery_city   : varchar   |     | cat_order_key : bigint    |     |                         |     |                | 
| delivery_state  : varchar   +----*| createdAt     : timestamp +----*|                         |*----+                |
| delivery_zip    : varchar   |     |                           |     |                         |     |                | 
| cc_number       : varchar   |     |                           |     |                         |     |                | 
| cc_expiration   : varchar   |     |                           |     |                         |     |                |
| cc_cvv          : varchar   |     |                           |     |                         |     |                | 
| placed_at       : timestamp |     |                           |     |                         |     |                |
|-----------------------------|     |---------------------------|     |-------------------------|     |----------------|

说明：
  - 它的表如下：
    - Cat_Order 表，用于存储 Cat 订单信息；
    - Cat 表，用于存储 Cat 信息；
    - Ingredient 表，用于 Cat 配料信息；
    - Cat_Ingredient 表，用于存储 Cat 和 配料（Ingredient）之间的关系。
  - 它的表关系如下：
    - Cat_Order 和 Cat 是一对多的关系，一个 Cat_Order 可以有多个 Cat，一个 Cat 只能属于一个 Cat_Order；
    - Cat 和 Ingredient 是多对多的关系，一个 Cat 可以有多个 Ingredient，一个 Ingredient 可以属于多个 Cat。
      此时引入 Cat_Ingredient 表，于是 Cat 和 Ingredient 之间的多对多关系就转换成：
        - Cat 与 Cat_Ingredient 的一对多关系；
        - Ingredient 与 Cat_Ingredient 的一对多关系。
用领域驱动设计的概念描述：
  - Cat_Order 和 Cat 被视为同一个聚合（aggregate），
    其中 Cat_Order 是聚合根（aggregate root），
    Cat 是聚合的一部分，这意味着：
      - Cat 不能在没有 Cat_Order 的情况下存在；
      - 只需定义一个 Cat_Order 的 Repository，即也可对 Cat 进行持久化操作。
  - Ingredient 是这个聚合的唯一成员，
    通过 Cat_Ingredient 与这个聚合关联。

*/

-- 创建 Cat_Order 表
CREATE TABLE IF NOT EXISTS Cat_Order (
  id               IDENTITY,
  customer_name    VARCHAR(50) NOT NULL,  /* 替换原来的 delivery_name */
  delivery_street  VARCHAR(50) NOT NULL,
  delivery_city    VARCHAR(50) NOT NULL,
  delivery_state   VARCHAR(2)  NOT NULL,
  delivery_zip     VARCHAR(10) NOT NULL,
  cc_number        VARCHAR(20) NOT NULL,
  cc_expiration    VARCHAR(5)  NOT NULL,
  cc_cvv           VARCHAR(3)  NOT NULL,
  placed_at        TIMESTAMP   NOT NULL
);

-- 创建 Cat 表
CREATE TABLE IF NOT EXISTS Cat (
  id            IDENTITY,
  name          VARCHAR(50) NOT NULL,
  cat_order     BIGINT NOT NULL,  /* 将原来的 cat_order_id 改名为 cat_order 因为：
                                     - 它将作为一个外键，引用 Cat_Order 表的 id 列
                                       （见 ALTER TABLE Cat ... ADD FOREIGN KEY (外键字段名) REFERENCES Cat_Order (id);）；
                                     - Spring Data 会根据 Cat 的外键约束，
                                       每当 Cat_Order 表创建一条记录，它就会在 Cat 表自动创建一条记录，
                                       而在创建这条 Cat 表记录时，
                                           如果约束中的“外键字段”没有在实体类/领域类/数据类中找到对应的属性
                                           （例如：如果外键字段名=cat_order_id，那么对应的属性应为 CatOrderId 或 cat_order_id），
                                           那么它会认为其外键值（Cat_Order 表的 id 列的值）应放在其 cat_order 列中。
                                  因此，如果：
                                         - 保持原来的 cat_order_id 列名，
                                         - 并且在 Cat 实体类/领域类/数据类中没有定义对应的属性（CatOrderId 或 cat_order_id）
                                       那么Spring Data 在创建这条 Cat 表记录时，就会报错说 cat_order 列不存在。
                                  例如：Caused by: org.springframework.jdbc.BadSqlGrammarException: PreparedStatementCallback; 
                                        bad SQL grammar [INSERT INTO "CAT" ("CAT_ORDER", "CAT_ORDER_KEY", "CREATED_AT", "NAME") VALUES (?, ?, ?, ?)]; 
                                        nested exception is org.h2.jdbc.JdbcSQLSyntaxErrorException: Column "CAT_ORDER" not found; SQL statement:
                                        INSERT INTO "CAT" ("CAT_ORDER", "CAT_ORDER_KEY", "CREATED_AT", "NAME") VALUES (?, ?, ?, ?) [42122-214]
                                  */  
  cat_order_key  BIGINT NOT NULL,
  created_at     TIMESTAMP NOT NULL  /* createdAt 改为 created_at 因为 Spring Data 会将驼峰命名转为下划线命名来作为列名 */
);

-- 创建 Cat_Ingredient 表
CREATE TABLE IF NOT EXISTS Cat_Ingredient (
  cat         BIGINT NOT NULL,  /* cat_id 改为 cat。参考 Cat 表的 cat_order 字段说明。 */
  cat_key     BIGINT NOT NULL,
  ingredient  VARCHAR(50) NOT NULL  /* ingredient_id 改为 ingredient。参考 Cat 表的 cat_order 字段说明。 
                                       同时注意：CatIngredient 类中，原来命名为 ingredientId 的属性名应改为 ingredient。
                                                否则会报错 ingredient_id 列不存在。
                                    */
);

-- 创建 Ingredient 表
CREATE TABLE IF NOT EXISTS Ingredient (
  id    VARCHAR(50) NOT NULL PRIMARY KEY,
  name  VARCHAR(50) NOT NULL,
  type  VARCHAR(50) NOT NULL
);

-- 修改 Cat 表，添加一个名为 FK_Cat_Cat_Order 的外键约束。
-- 这个外键约束的作用是：Cat 表中的 cat_order 列的值（作为外键）必须引用 Cat_Order 表中 id 列的值。
ALTER TABLE Cat 
    ADD CONSTRAINT FK_Cat_Cat_Order 
    FOREIGN KEY (cat_order) REFERENCES Cat_Order (id);

-- 修改 Cat_Ingredient 表，添加一个名为 FK_Cat_Ingredient_Ingredient 的外键约束。
-- 这个外键约束的作用是：Cat_Ingredient 表中的 ingredient 列的值（作为外键）必须引用 Ingredient 表中 name 列的值。
ALTER TABLE Cat_Ingredient 
    ADD CONSTRAINT FK_Cat_Ingredient_Ingredient 
    FOREIGN KEY (ingredient) REFERENCES Ingredient (id);
