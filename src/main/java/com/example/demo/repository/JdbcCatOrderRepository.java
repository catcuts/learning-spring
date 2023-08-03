package com.example.demo.repository;

/*  
    无需 JdbcCatOrderRepository 类，因为：
      - CatOrderRepository 继承了 CrudRepository 接口，该接口已定义常见的数据库操作方法。
      - Spring Data 会在运行时自动实现 CatOrderRepository 继承的接口方法，即常见数据库操作方法。
      - 因此无需 JdbcCatOrderRepository 类来实现 CatOrderRepository 继承的接口方法（除非要自定义实现）。

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.asm.Type;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.CatOrder;
import com.example.demo.domain.Cat;
import com.example.demo.domain.CatIngredient;

import com.example.demo.repository.CatOrderRepository;

@Repository
public class JdbcCatOrderRepository implements CatOrderRepository {

    private JdbcOperations jdbcOperations;

    public JdbcCatOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    @Transactional
    public CatOrder save(CatOrder catOrder) {
        PreparedStatementCreatorFactory pscf = 
            new PreparedStatementCreatorFactory(
                "INSERT INTO Cat_Order "
                +"(delivery_name, delivery_street, delivery_city, "
                +"delivery_state, delivery_zip, cc_number, "
                +"cc_expiration, cc_cvv, placed_at) "
                +"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
            );
        pscf.setReturnGeneratedKeys(true);

        catOrder.setPlacedAt(new Date());
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
            Arrays.asList(
                catOrder.getDeliveryName(),
                catOrder.getDeliveryStreet(),
                catOrder.getDeliveryCity(),
                catOrder.getDeliveryState(),
                catOrder.getDeliveryZip(),
                catOrder.getCcNumber(),
                catOrder.getCcExpiration(),
                catOrder.getCcCVV(),
                catOrder.getPlacedAt()
            )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);

        long catOrderId = keyHolder.getKey().longValue();
        catOrder.setId(catOrderId);

        List<Cat> cats = catOrder.getCats();
        int i = 0;
        for (Cat cat : cats) {
            saveCat(cat, catOrderId, i++);
        }

        return catOrder;
    }

    private long saveCat(Cat cat, long catOrderId, int orderKey) {
        cat.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscf = 
            new PreparedStatementCreatorFactory(
                "INSERT INTO Cat "
                +"(name, cat_order_id, cat_order_key, createdAt) "
                +"VALUES (?, ?, ?, ?)",
                Types.VARCHAR, Type.LONG, Type.LONG, Types.TIMESTAMP
            );
        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
            Arrays.asList(
                cat.getName(),
                catOrderId,
                orderKey,
                cat.getCreatedAt()
            )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long catId = keyHolder.getKey().longValue();
        cat.setId(catId);

        saveCatIngredients(catId, cat.getIngredients());

        return catId;
    }

    private void saveCatIngredients(long catId, List<CatIngredient> catIngredients) {
        int i = 0;
        for (CatIngredient catIngredient : catIngredients) {
            jdbcOperations.update(
                "INSERT INTO Cat_Ingredient (cat_id, cat_key, ingredient_id) "
                +"VALUES (?, ?, ?)",
                catId, i++, catIngredient.getIngredientId()
            );
        }
    }

}
*/