package com.listaai.list.adapter.outbound.api.mapper;

import com.listaai.list.adapter.outbound.api.response.RecipeApiResponse;
import com.listaai.list.domain.enums.ItemUnit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApiWebMapperTest {

    private final ApiWebMapper apiWebMapper = new ApiWebMapper();

    @Test
    void shouldReturnEmptyListWhenResponseIsNull() {
        assertThat(apiWebMapper.toDomain(null)).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenResponseItemsAreNull() {
        RecipeApiResponse response = new RecipeApiResponse(null);

        assertThat(apiWebMapper.toDomain(response)).isEmpty();
    }

    @Test
    void shouldMapApiResponseToDomainItemsNormalizingUnit() {
        RecipeApiResponse response = new RecipeApiResponse(List.of(
                new RecipeApiResponse.RecipeItemResponse("Tomate", 2, " kg ", false)
        ));

        var result = apiWebMapper.toDomain(response);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Tomate");
        assertThat(result.getFirst().getQuantity()).isEqualTo(2);
        assertThat(result.getFirst().getUnit()).isEqualTo(ItemUnit.KG);
        assertThat(result.getFirst().isPurchased()).isFalse();
    }

    @Test
    void shouldThrowWhenUnitIsInvalid() {
        RecipeApiResponse response = new RecipeApiResponse(List.of(
                new RecipeApiResponse.RecipeItemResponse("Tomate", 2, "invalid", false)
        ));

        assertThrows(IllegalArgumentException.class, () -> apiWebMapper.toDomain(response));
    }
}
