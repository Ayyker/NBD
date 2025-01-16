package mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import daos.ItemDao;

@Mapper
public interface ItemMapper {
    @DaoFactory
    ItemDao getItemDao();
}
