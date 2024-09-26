package store.reduct.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import store.reduct.model.bucket.EntryInfo;

@Mapper
public interface EntryInfoMapper {
	EntryInfoMapper INSTANCE = Mappers.getMapper(EntryInfoMapper.class);
	void copy(@MappingTarget EntryInfo target, EntryInfo source);
}
