package com.fc.shimpyo_be.domain.product.dto.response;

import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record ProductDetailsResponse(Long productId, String category, String address,
                                     String productName, String desc, Float starAvg,

                                     List<String> images,

                                     List<RoomResponse> rooms

) {

}
