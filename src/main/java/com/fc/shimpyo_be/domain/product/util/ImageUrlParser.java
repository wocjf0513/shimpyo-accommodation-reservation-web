package com.fc.shimpyo_be.domain.product.util;

import java.util.Arrays;
import java.util.List;

public class ImageUrlParser {

    public static List<String> parse(String imageUrl) {
        return Arrays.stream(imageUrl.split(",")).toList();
    }

    public static String pareseThumbnail(String imageUrl) {
        String[] images = imageUrl.split(",");
        return images[0];
    }
}
