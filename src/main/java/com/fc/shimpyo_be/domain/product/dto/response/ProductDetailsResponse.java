package com.fc.shimpyo_be.domain.product.dto.response;

import com.fc.shimpyo_be.domain.room.dto.response.RoomResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
public record ProductDetailsResponse(Long productId, String category, String address,
                                     String productName, String desc, Float starAvg,

                                     List<String> images,

                                     List<RoomResponse> rooms

) {

}
