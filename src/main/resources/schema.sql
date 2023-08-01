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
  delivery_name    VARCHAR(50) NOT NULL,
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
  id             IDENTITY,
  name           VARCHAR(50) NOT NULL,
  cat_order_id   BIGINT NOT NULL,
  cat_order_key  BIGINT NOT NULL,
  createdAt      TIMESTAMP NOT NULL
);

-- 创建 Cat_Ingredient 表
CREATE TABLE IF NOT EXISTS Cat_Ingredient (
  cat_id         BIGINT NOT NULL,
  cat_key        BIGINT NOT NULL,
  ingredient_id  VARCHAR(50) NOT NULL
);

-- 创建 Ingredient 表
CREATE TABLE IF NOT EXISTS Ingredient (
  id    VARCHAR(50) NOT NULL PRIMARY KEY,
  name  VARCHAR(50) NOT NULL,
  type  VARCHAR(50) NOT NULL
);

-- 修改 Cat 表，添加一个名为 FK_Cat_Cat_Order 的外键约束。
-- 这个外键约束的作用是：Cat 表中的 cat_order_id 列的值（作为外键）必须引用 Cat_Order 表中 id 列的值。
ALTER TABLE Cat 
    ADD CONSTRAINT FK_Cat_Cat_Order 
    FOREIGN KEY (cat_order_id) REFERENCES Cat_Order (id);

-- 修改 Cat_Ingredient 表，添加一个名为 FK_Cat_Ingredient_Ingredient 的外键约束。
-- 这个外键约束的作用是：Cat_Ingredient 表中的 ingredient 列的值（作为外键）必须引用 Ingredient 表中 name 列的值。
ALTER TABLE Cat_Ingredient 
    ADD CONSTRAINT FK_Cat_Ingredient_Ingredient 
    FOREIGN KEY (ingredient_id) REFERENCES Ingredient (id);
