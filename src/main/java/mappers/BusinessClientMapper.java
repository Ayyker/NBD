package mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import daos.BusinessClientDao;

@Mapper
public interface BusinessClientMapper {
    @DaoFactory
    BusinessClientDao getBusinessClientDao();
}
