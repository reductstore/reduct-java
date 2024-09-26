package store.reduct.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;
import store.reduct.model.bucket.Bucket;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {BucketSettingsMapper.class, EntryInfoMapper.class}, mappingControl = DeepClone.class)
public interface BucketMapper {
    BucketMapper INSTANCE = Mappers.getMapper(BucketMapper.class);

    @Mapping(target = "reductClient" ,ignore = true)
    @Mapping(target = "bucketSettings", source = "bucketSettings")
    @Mapping(target = "settings", ignore = true)
    void copy(@MappingTarget Bucket target, Bucket source);
}
