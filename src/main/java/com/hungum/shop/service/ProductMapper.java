package com.hungum.shop.service;

import com.hungum.shop.dto.ProductDto;
import com.hungum.shop.dto.ProductRatingDto;
import com.hungum.shop.model.Product;
import com.hungum.shop.model.ProductRating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    ProductRating mapProductRatingDto(ProductRatingDto productRatingDto);

    @Mapping(source = "id", target = "ratingId")
    ProductRatingDto mapProductRating(ProductRating productRating);
}
