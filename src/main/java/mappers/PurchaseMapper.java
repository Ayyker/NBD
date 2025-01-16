package mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import daos.PurchaseDao;

@Mapper
public interface PurchaseMapper {
    @DaoFactory
    PurchaseDao getPurchaseDao();
}
