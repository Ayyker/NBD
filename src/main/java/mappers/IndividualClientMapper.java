package mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import model.IndividualClient;

@Mapper
public interface IndividualClientMapper {
    @DaoFactory
    IndividualClient individualClientDao();
}
