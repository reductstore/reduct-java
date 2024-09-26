package store.reduct.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import store.reduct.model.bucket.BucketSettings;

@Mapper
public interface BucketSettingsMapper {
	BucketSettingsMapper INSTANCE = Mappers.getMapper(BucketSettingsMapper.class);
	void copy(@MappingTarget BucketSettings target, BucketSettings source);
}
